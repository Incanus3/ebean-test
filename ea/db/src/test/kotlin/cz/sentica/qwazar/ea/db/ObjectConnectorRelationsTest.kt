package cz.sentica.qwazar.ea.db

import cz.sentica.qwazar.ea.core.entities.EaConnector
import cz.sentica.qwazar.ea.core.entities.EaObject
import io.ebean.Database
import io.ebean.DatabaseFactory
import io.ebean.config.DatabaseConfig
import io.ebean.datasource.DataSourceConfig
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class ObjectConnectorRelationsTest {
    private fun dbConfig() = DatabaseConfig().apply {
        name = "ea"
        packages = listOf("cz.sentica.qwazar.ea.core.entities")

        isDdlRun = true
        isDdlGenerate = true
        isDefaultServer = false

        dataSourceConfig = DataSourceConfig().apply {
            username = "test"
            password = "test"
            url = "jdbc:h2:mem:test"
            driver = "org.h2.Driver"
        }
    }

    private val database: Database = DatabaseFactory.create(dbConfig())

    @Test
    fun testFetchingRelations() {
        val object1 = EaObject(name = "object1")
        val object2 = EaObject(name = "object2")

        database.saveAll(object1, object2)

        val connection = EaConnector(startObjectId = object1.id, endObjectId = object2.id)

        database.save(connection)

        // relations aren't populated on save
        assertNull(connection.startObject)
        assertNull(connection.endObject)
        assertEquals(object1.outgoingConnections, emptyList())

        // this has the same problem
        // val reloadedObject1 = database.createQuery(EaObject::class.java).where().idEq(object1.id).findOne()!!
        val reloadedObject1 = database.find(EaObject::class.java, object1.id)!!
        val reloadedConnection = database.find(EaConnector::class.java, connection.id)!!

        // relations are populated on reload
        assertEquals(reloadedConnection.startObject, object1)
        assertEquals(reloadedConnection.endObject, object2)
        assertEquals(reloadedObject1.outgoingConnections, listOf(connection))
    }
}
