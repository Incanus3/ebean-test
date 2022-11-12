package entities

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import io.ebean.annotation.DbName
import java.io.Serializable
import javax.persistence.*

@Entity
@DbName("ea")
@Table(name = "T_OBJECT")
@JsonIgnoreProperties(value = ["createdAt", "updatedAt"], allowGetters = true)
class EaObject(
    @Id
    @Column(name = "OBJECT_ID", nullable = false)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "t_object_sequence_generator")
    @SequenceGenerator(
        name = "t_object_sequence_generator",
        sequenceName = "OBJECT_ID_SEQ",
        allocationSize = 1,
    )
    var id: Long = 0,

    @Column(name = "NAME")
    var name: String? = null,
) {
    @JsonIgnore
    @OneToMany(mappedBy = "startObject", cascade = [CascadeType.ALL])
    var outgoingConnections: List<EaConnector> = arrayListOf()

    @JsonIgnore
    @OneToMany(mappedBy = "endObject", cascade = [CascadeType.ALL])
    var incomingConnections: List<EaConnector> = arrayListOf()

    override fun equals(other: Any?): Boolean = other is EaObject && other.id == this.id
    override fun hashCode(): Int = id.hashCode()
}
