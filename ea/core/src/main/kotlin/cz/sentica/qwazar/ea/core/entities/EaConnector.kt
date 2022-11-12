package cz.sentica.qwazar.ea.core.entities

import io.ebean.annotation.DbName
import io.ebean.annotation.JsonIgnore
import javax.persistence.*

@Entity
@DbName("ea")
@Table(name = "T_CONNECTOR")
class EaConnector(
    @Id
    @Column(name = "CONNECTOR_ID", nullable = false)
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    var id: Long = 0,

    @Column(name = "START_OBJECT_ID", nullable = false)
    var startObjectId: Long,

    @Column(name = "END_OBJECT_ID", nullable = false)
    var endObjectId: Long,
) {
    @JsonIgnore
    @ManyToOne(optional = false)
    @JoinColumn(name = "START_OBJECT_ID", nullable = false, insertable = false, updatable = false)
    var startObject: EaObject? = null

    @JsonIgnore
    @ManyToOne(optional = false)
    @JoinColumn(name = "END_OBJECT_ID", nullable = false, insertable = false, updatable = false)
    var endObject: EaObject? = null

    override fun equals(other: Any?): Boolean = other is EaConnector && other.id == this.id
    override fun hashCode(): Int = id.hashCode()
}
