package cz.sentica.qwazar.ea.db

import cz.sentica.qwazar.ea.db.test.TestEntity
import io.ebean.Database
import io.ebean.DatabaseFactory
import io.ebean.config.DatabaseConfig
import io.ebean.datasource.DataSourceConfig
import org.junit.jupiter.api.Test

class TestEntityEnhancementTest {
    private fun dbConfig() = DatabaseConfig().apply {
        name = "ea"
        packages = listOf("cz.sentica.qwazar.ea.db.test")

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
        val object1 = TestEntity(name = "object1")
        val object2 = TestEntity(name = "object2")

        database.saveAll(object1, object2)

        println(object1.id)
        println(database.find(TestEntity::class.java, object1.id))
    }
}
