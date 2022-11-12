package cz.sentica.qwazar.ea.core.entities

import io.ebean.annotation.DbName
import io.ebean.annotation.JsonIgnore
import javax.persistence.*

@Entity
@DbName("ea")
@Table(name = "T_OBJECT")
class EaObject(
    @Id
    @Column(name = "OBJECT_ID", nullable = false)
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
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
