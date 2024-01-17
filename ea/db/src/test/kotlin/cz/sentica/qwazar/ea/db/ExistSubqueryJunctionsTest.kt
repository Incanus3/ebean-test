package cz.sentica.qwazar.ea.db

import cz.sentica.qwazar.ea.core.entities.query.QEaObject
import cz.sentica.qwazar.ea.core.entities.query.QEaPackage
import io.ebean.Database
import io.ebean.DatabaseFactory
import io.ebean.config.DatabaseConfig
import io.ebean.datasource.DataSourceConfig
import org.junit.jupiter.api.Test

class ExistSubqueryJunctionsTest {
    private fun dbConfig() = DatabaseConfig().also { databaseConfig ->
        databaseConfig.name = "ea"
        databaseConfig.packages = listOf("cz.sentica.qwazar.ea.core.entities")

        databaseConfig.isDdlRun = true
        databaseConfig.isDdlGenerate = true
        databaseConfig.isDefaultServer = false

        databaseConfig.setDataSourceConfig(
            DataSourceConfig().also {
                it.username = "test"
                it.password = "test"
                it.url = "jdbc:h2:mem:test"
                it.driver = "org.h2.Driver"
            },
        )
    }

    private val database: Database = DatabaseFactory.create(dbConfig())

    @Test
    fun `withChildOrDirectContainedObject(queryBuilder)`() {
        val childSubQuery = QEaObject(database).stereotype.eq("car")
        val containedSubQuery = QEaObject(database).stereotype.eq("car")
        val containerPackageSubQuery = QEaPackage(database).alias("container").exists(
            containedSubQuery.alias("contained")
                .raw("contained.PACKAGE_ID = container.PACKAGE_ID").query(),
        )

        // FIXME: for some reason, this generates AND instead of OR
        QEaObject(database)
            .or()
            .alias("parent").exists(
                childSubQuery.alias("child")
                    .raw("child.PARENTID = parent.OBJECT_ID").query(),
            )
            .eaGuid.isIn(containerPackageSubQuery.select(QEaPackage._alias.eaGuid).query())
            .endOr()
            .select(QEaObject._alias.id)
            .findList().forEach(::println)
    }
}
