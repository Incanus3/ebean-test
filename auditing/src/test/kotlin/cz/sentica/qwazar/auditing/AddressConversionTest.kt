package cz.sentica.qwazar.auditing

import cz.sentica.qwazar.auditing.query.QDatabaseAuditTrace
import io.ebean.Database
import io.ebean.DatabaseFactory
import io.ebean.config.DatabaseConfig
import io.ebean.datasource.DataSourceConfig
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import kotlin.test.assertContains
import kotlin.test.assertEquals

class AddressConversionTest {
    private fun dbConfig() = DatabaseConfig().apply {
        name = "ea"
        packages = listOf("cz.sentica.qwazar.auditing")

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

    private lateinit var nullTrace: DatabaseAuditTrace
    private lateinit var registryTrace: DatabaseAuditTrace

    @BeforeEach
    fun setUp() {
        nullTrace = DatabaseAuditTrace()
        registryTrace = DatabaseAuditTrace(
            resourceAddress = Address.forRegistry("test", "Test"),
        )

        database.saveAll(nullTrace, registryTrace)
    }

    @AfterEach
    fun tearDown() {
        QDatabaseAuditTrace(database).delete()
    }

    @Test
    fun testSavingAndFetching() {
        val reloaded = QDatabaseAuditTrace(database).setId(registryTrace.gid).findOne()!!

        assertEquals(registryTrace.resourceAddress, reloaded.resourceAddress)
    }

    @Test
    fun queryingByAddress() {
        val nullTraces = QDatabaseAuditTrace(database)
            .where().resourceAddress.equalTo(NULL_ADDRESS)
            .findList()

        assertEquals(1, nullTraces.size)
        assertContains(nullTraces, nullTrace)
    }
}
