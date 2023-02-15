package cz.sentica.qwazar.auditing

import io.ebean.annotation.DbName
import java.time.LocalDateTime
import java.util.*
import javax.persistence.*

typealias ActionType = String

@Converter(autoApply = true)
class AddressConverter : AttributeConverter<Address, String> {
    override fun convertToDatabaseColumn(attribute: Address): String = attribute.serialize()
    override fun convertToEntityAttribute(dbData: String): Address = Address.parse(dbData)
}

@Entity
@DbName("rest")
@Table(name = "corlog_log")
data class DatabaseAuditTrace(
    @Id
    @Column(name = "log_gid")
    override var gid: String = UUID.randomUUID().toString(),

    @Column(name = "log_timestamp")
    override var timestamp: LocalDateTime = LocalDateTime.now(),

    @Column(name = "log_initiator")
    override var initiator: String = "",

    @Convert(converter = AddressConverter::class)
    @Column(name = "log_resource")
    override var resourceAddress: Address = NULL_ADDRESS,

    @Column(name = "log_resource_gid")
    override var resourceGid: String = "",

    @Column(name = "log_value", nullable = true, length = maxValueLength)
    override var value: String? = "",

    @Column(name = "log_old_value", nullable = true, length = maxValueLength)
    override var oldValue: String? = "",

    @Column(name = "log_action")
    override var action: ActionType = "",

    @Column(name = "log_message", length = maxMessageLength)
    override var message: String = "",
) : AuditTrace {
    companion object {
        const val maxMessageLength = 1000
        const val maxValueLength = 255
    }

    override fun hashCode(): Int = gid.hashCode()
    override fun equals(other: Any?): Boolean = other is DatabaseAuditTrace && other.gid == gid
}
