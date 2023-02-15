package cz.sentica.qwazar.auditing

import java.time.LocalDateTime

interface AuditTrace {
    val gid: String
    val timestamp: LocalDateTime
    val initiator: String
    val resourceAddress: Address
    val resourceGid: String
    val value: String?
    val oldValue: String?
    val action: ActionType
    val message: String
}
