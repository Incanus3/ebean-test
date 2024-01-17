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
    fun `child or contained`() {
        // FIXME: this generates AND instead of OR
        QEaObject(database)
            .alias("parent")
            .or()
            .exists(
                childSubQuery.alias("child")
                    .raw("child.PARENTID = parent.OBJECT_ID").query(),
            )
            .eaGuid.isIn(containingPackageSubQuery.select(QEaPackage._alias.eaGuid).query())
            .endOr()
            .select(QEaObject._alias.id)
            .findList()

        // -- produced query is
        // select parent.OBJECT_ID from T_OBJECT parent where (
        //   parent.EA_GUID in (
        //     select container.EA_GUID from T_PACKAGE container where exists (
        //       select 1 from T_OBJECT contained
        //       where contained.STEREOTYPE = ? and contained.PACKAGE_ID = container.PACKAGE_ID
        //     )
        //   )
        // ) AND exists (
        //   select 1 from T_OBJECT child
        //   where child.STEREOTYPE = ? and child.PARENTID = parent.OBJECT_ID
        // ); --bind(car,car)
    }

    @Test
    fun `contained or child`() {
        // FIXME: this generates AND instead of OR
        QEaObject(database)
            .alias("parent")
            .or()
            .eaGuid.isIn(containingPackageSubQuery.select(QEaPackage._alias.eaGuid).query())
            .exists(
                childSubQuery.alias("child")
                    .raw("child.PARENTID = parent.OBJECT_ID").query(),
            )
            .endOr()
            .select(QEaObject._alias.id)
            .findList()

        // -- produced query is
        // select parent.OBJECT_ID from T_OBJECT parent where (
        //   parent.EA_GUID in (
        //     select container.EA_GUID from T_PACKAGE container where exists (
        //       select 1 from T_OBJECT contained
        //       where contained.STEREOTYPE = ? and contained.PACKAGE_ID = container.PACKAGE_ID
        //     )
        //   )
        // ) AND exists (
        //   select 1 from T_OBJECT child
        //   where child.STEREOTYPE = ? and child.PARENTID = parent.OBJECT_ID
        // ); --bind(car,car)
        // -- same as above
    }

    @Test
    fun `no exists`() {
        // this correctly generates OR
        QEaObject(database)
            .alias("parent")
            .or()
            .name.isIn("some", "whitelisted", "names")
            .eaGuid.isIn(containingPackageSubQuery.select(QEaPackage._alias.eaGuid).query())
            .endOr()
            .select(QEaObject._alias.id)
            .findList()

        // -- produced query is
        // select parent.OBJECT_ID from T_OBJECT parent where (
        //   parent.NAME in (?,?,?) OR parent.EA_GUID in (
        //     select container.EA_GUID from T_PACKAGE container where exists (
        //       select 1 from T_OBJECT contained where contained.STEREOTYPE = ? and contained.PACKAGE_ID = container.PACKAGE_ID
        //     )
        //   )
        // ); --bind(Array[3]={some,whitelisted,names},car)
        // -- which is correct, both in junction used and in order
    }

    private val childSubQuery get() = QEaObject(database).stereotype.eq("car")
    private val containingPackageSubQuery get() = QEaPackage(database).alias("container").exists(
        childSubQuery.alias("contained")
            .raw("contained.PACKAGE_ID = container.PACKAGE_ID").query(),
    )
}
