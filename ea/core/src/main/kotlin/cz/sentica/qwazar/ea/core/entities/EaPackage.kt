package cz.sentica.qwazar.ea.core.entities

import cz.sentica.qwazar.ea.core.utils.GuidGenerator
import io.ebean.annotation.DbName
import java.io.Serializable
import javax.persistence.*

@Entity
@DbName("ea")
@Table(name = "T_PACKAGE")
@Suppress("unused")
class EaPackage(
    @Id
    @Column(name = "PACKAGE_ID", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long = 0,

    @Column(name = "NAME")
    var name: String? = null,

    @Column(name = "EA_GUID", nullable = false, unique = true, length = 40)
    var eaGuid: String = GuidGenerator.newEaGuid,

    @Column(name = "PARENT_ID", nullable = false)
    var parentId: Long = 0L,
) : Serializable {
    override fun toString() = String.format(
        "EaPackage(id=%d,  name='%s', guid='%s', parentId=%s)",
        id, name, eaGuid, parentId,
    )

    override fun equals(other: Any?): Boolean = other is EaPackage && other.id == this.id
    override fun hashCode(): Int = id.hashCode()
}
