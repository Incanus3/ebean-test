package cz.sentica.qwazar.auditing

internal const val ADDRESS_WILDCARD = "*"
const val ANY_NAMESPACE_STRING = ADDRESS_WILDCARD
const val ANY_ADDRESS_STRING = ADDRESS_WILDCARD

const val ANY_NAMESPACE_STRING_OLD = "_internal_any_namespace"
const val ANY_ADDRESS_STRING_OLD = "_internal_any_address"

const val NOT_RELEVANT_STRING_OLD = "_internal_not_relevant_parameter"
const val NOT_RELEVANT = ADDRESS_WILDCARD

val ANY_NAMESPACE = Namespace.parse(ANY_NAMESPACE_STRING)

const val RESOURCE_TYPE_SEPARATOR = ":" // separates resource type from the rest of address
const val NAMESPACE_SEPARATOR = "/" // separates namespace from identifier
const val SUBSEGMENT_SEPARATOR = "." // separates subsegments of namespace or identifier

@Suppress("ClassName")
object ANY_IDENTIFIER : Identifier(NOT_RELEVANT) {
    override val first = NOT_RELEVANT
    override val second = NOT_RELEVANT
    override val third = NOT_RELEVANT
}

val ANY_ADDRESS = Address(
    resourceType = ResourceType.Any,
    namespace = ANY_NAMESPACE,
    identifier = ANY_IDENTIFIER,
)
val NULL_ADDRESS = Address(
    resourceType = ResourceType.None,
    namespace = Namespace(),
    identifier = Identifier(),
)
interface CanBeSerialized {
    fun serialize(): String
}

class Namespace private constructor(val segments: List<String>) : CanBeSerialized {
    internal constructor(vararg segments: String) : this(segments.asList())

    companion object {
        fun parse(input: String) = Namespace(
            input.replace(ANY_NAMESPACE_STRING_OLD, ANY_NAMESPACE_STRING)
                .split(SUBSEGMENT_SEPARATOR, NAMESPACE_SEPARATOR),
        )
    }

    operator fun plus(segment: String) = Namespace(this.segments + segment)
    operator fun plus(segments: List<String>) = Namespace(this.segments + segments)
    operator fun plus(other: Namespace) = Namespace(this.segments + other.segments)

    override fun serialize() = segments.joinToString(SUBSEGMENT_SEPARATOR)

    override fun equals(other: Any?) = other is Namespace && other.segments == this.segments
    override fun hashCode() = segments.hashCode()
}

open class Identifier private constructor(private val segments: List<String>) : CanBeSerialized {
    class Invalid(message: String) : IllegalArgumentException(message)
    class MissingSegment(identifier: Identifier, index: Int) :
        NoSuchElementException("segment $index is missing in ${identifier.serialize()}")

    internal constructor(vararg segments: String) : this(segments.asList())

    companion object {
        fun parse(input: String): Identifier {
            if (input.isEmpty()) throw Invalid("Identifier must not be empty")
            val segments = input.replace(NOT_RELEVANT_STRING_OLD, NOT_RELEVANT).split(SUBSEGMENT_SEPARATOR)
            if (segments.any { it.isEmpty() }) throw Invalid("Identifier segments must not be empty")
            return Identifier(segments)
        }
    }

    operator fun plus(segment: String) = Identifier(this.segments + segment)
    operator fun plus(segments: List<String>) = Identifier(this.segments + segments)
    operator fun plus(other: Identifier) = Identifier(this.segments + other.segments)

    internal val segmentCount = segments.size

    override fun serialize() = segments.joinToString(SUBSEGMENT_SEPARATOR)

    open val first by lazy { this[0] }
    open val second by lazy { this[1] }
    open val third by lazy { this[2] }

    override fun toString(): String {
        return "${this::class.simpleName}(segments=$segments)"
    }

    override fun equals(other: Any?) = other is Identifier && other.segments == this.segments
    override fun hashCode() = segments.hashCode()

    private operator fun get(index: Int) = try {
        segments[index]
    } catch (error: IndexOutOfBoundsException) {
        throw MissingSegment(this, index)
    }
}

data class Address internal constructor(
    val resourceType: ResourceType,
    val namespace: Namespace,
    val identifier: Identifier,
) : CanBeSerialized {
    class CouldNotBeParsed(
        input: String,
        cause: Throwable? = null,
        additionalMessage: String? = null,
    ) : IllegalArgumentException(
        listOfNotNull(
            "$input could not be parsed as Address",
            additionalMessage,
        ).joinToString(": "),
        cause,
    )

    companion object {
        internal fun forRegistry(
            namespace: String,
            registryLid: String,
        ) = Address(
            resourceType = ResourceType.Registry,
            namespace = Namespace.parse(namespace),
            identifier = Identifier(registryLid),
        )

        fun parse(input: String): Address {
            if (input in listOf(ANY_ADDRESS_STRING_OLD, ANY_ADDRESS_STRING)) return ANY_ADDRESS

            try {
                var (resourceTypeName, rest) = input.split(RESOURCE_TYPE_SEPARATOR)
                if (NAMESPACE_SEPARATOR !in rest) rest = NAMESPACE_SEPARATOR + rest
                val namespaceString = rest.substringBeforeLast(NAMESPACE_SEPARATOR)
                val identifierString = rest.substringAfterLast(NAMESPACE_SEPARATOR)
                val resourceType = ResourceType.from(resourceTypeName)
                if (resourceType == ResourceType.None) return NULL_ADDRESS
                val identifier = Identifier.parse(identifierString)

                if (identifier.segmentCount < resourceType.requiredSegmentCount) {
                    throw CouldNotBeParsed(
                        input,
                        additionalMessage = "resource type $resourceTypeName must have " +
                            "${resourceType.requiredSegmentCount} identifier segments",
                    )
                }

                return Address(
                    resourceType = resourceType,
                    namespace = Namespace.parse(namespaceString),
                    identifier = identifier,
                )
            } catch (error: RuntimeException) {
                when (error) {
                    is ResourceType.Invalid, is Identifier.Invalid,
                    is IndexOutOfBoundsException, -> throw CouldNotBeParsed(input, cause = error)
                    else -> throw error
                }
            }
        }
    }

    override fun serialize() = when (resourceType) {
        ResourceType.Any -> ANY_ADDRESS_STRING
        else -> buildString {
            append(resourceType.name)
            append(RESOURCE_TYPE_SEPARATOR)
            append(namespace.serialize())
            append(NAMESPACE_SEPARATOR)
            append(identifier.serialize())
        }
    }
}
