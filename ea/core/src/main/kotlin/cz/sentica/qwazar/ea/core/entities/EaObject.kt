package cz.sentica.qwazar.ea.core.entities

import cz.sentica.qwazar.ea.core.utils.GuidGenerator
import io.ebean.annotation.DbName
import io.ebean.annotation.JsonIgnore
import javax.persistence.*

@Entity
@DbName("ea")
@Table(name = "T_OBJECT")
@Suppress("unused")
class EaObject(
    @Id
    @Column(name = "OBJECT_ID", nullable = false)
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    var id: Long = 0,

    @Column(name = "PARENTID")
    var parentId: Long? = null,

    @Column(name = "PACKAGE_ID", nullable = false)
    var packageId: Long = 0L,

    @Column(name = "EA_GUID", nullable = false, unique = true, length = 250)
    var eaGuid: String = GuidGenerator.newEaGuid,

    @Column(name = "NAME")
    var name: String? = null,

    @Column(name = "STEREOTYPE")
    var stereotype: String? = null,
) {
    @JsonIgnore
    @OneToMany(mappedBy = "startObject", cascade = [CascadeType.ALL])
    var outgoingConnections: List<EaConnector> = arrayListOf()

    @JsonIgnore
    @OneToMany(mappedBy = "endObject", cascade = [CascadeType.ALL])
    var incomingConnections: List<EaConnector> = arrayListOf()

    override fun toString() = "EaObject(" +
        "id=$id, name='$name', guid='$eaGuid', stereotype='$stereotype', parentId=$parentId, packageId=$packageId)"

    override fun equals(other: Any?): Boolean = other is EaObject && other.id == this.id
    override fun hashCode(): Int = id.hashCode()
}
