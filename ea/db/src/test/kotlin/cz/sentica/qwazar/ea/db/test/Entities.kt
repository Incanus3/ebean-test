package cz.sentica.qwazar.ea.db.test // ktlint-disable filename

import io.ebean.annotation.DbName
import javax.persistence.*

@Entity
@DbName("ea")
class TestEntity(
    @Id
    @Column(nullable = false)
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    var id: Long = 0,

    @Column
    var name: String = "",
    @Column
    var price: Int = 0,
) {
    override fun toString(): String =
        "<${this::class.simpleName} id=$id name=\"$name\" price=$price>"
}

