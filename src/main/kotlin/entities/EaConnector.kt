package entities

import com.fasterxml.jackson.annotation.JsonIgnore
import io.ebean.annotation.DbName
import javax.persistence.*

@Entity
@DbName("ea")
@Table(name = "T_CONNECTOR")
class EaConnector(
    @Id
    @Column(name = "CONNECTOR_ID", nullable = false)
    @GeneratedValue(
        strategy = GenerationType.SEQUENCE,
        generator = "t_connector_sequence_generator",
    )
    @SequenceGenerator(
        name = "t_connector_sequence_generator",
        sequenceName = "CONNECTOR_ID_SEQ",
        allocationSize = 1,
    )
    var id: Long = 0,

    // these are used only by the Db implementation
    @Column(name = "START_OBJECT_ID", nullable = false)
    var startObjectId: Long,

    @Column(name = "END_OBJECT_ID", nullable = false)
    var endObjectId: Long,
) {
    @JsonIgnore
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(
        name = "START_OBJECT_ID", referencedColumnName = "OBJECT_ID",
        nullable = false, insertable = false, updatable = false,
    )
    var startObject: EaObject? = null

    @JsonIgnore
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(
        name = "END_OBJECT_ID", referencedColumnName = "OBJECT_ID",
        nullable = false, insertable = false, updatable = false,
    )
    var endObject: EaObject? = null

    override fun equals(other: Any?): Boolean = other is EaConnector && other.id == this.id
    override fun hashCode(): Int = id.hashCode()
}
