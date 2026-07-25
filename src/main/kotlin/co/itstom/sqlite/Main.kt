package co.itstom.sqlite

import co.itstom.sqlite.model.Employees
import org.ktorm.entity.sequenceOf
import org.slf4j.Logger
import org.slf4j.LoggerFactory

val logger: Logger = LoggerFactory.getLogger("Main")

private const val INIT_SCRIPT = "init-sqlite-data.sql"

fun main() {
    val sqliteDatabase = SqliteDatabase()
    val db = sqliteDatabase.connect()

    sqliteDatabase.execSqlScript(INIT_SCRIPT, db)

    for (e in db.sequenceOf(Employees)) {
        logger.info("Employee :: ${e.name} | ${e.job} | ${e.department.name} | ${e.department.location} | ${e.hireDate}")
    }
}
