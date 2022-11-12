package cz.sentica.qwazar.ea.core.utils

import java.util.*

object GuidGenerator {
    @JvmStatic
    val newEaGuid: String
        get() = "{${UUID.randomUUID()}}"
}
