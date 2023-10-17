package co.itstom.sqlite

import co.itstom.sqlite.model.Department
import co.itstom.sqlite.model.Employee
import org.ktorm.dsl.from
import org.ktorm.dsl.select
import org.slf4j.Logger
import org.slf4j.LoggerFactory

val logger: Logger = LoggerFactory.getLogger("Main")

private const val INIT_SCRIPT = "init-sqlite-data.sql"

fun main() {
    val sqliteDatabase = SqliteDatabase()
    val db = sqliteDatabase.connect()

    sqliteDatabase.execSqlScript(INIT_SCRIPT, db)

    for (row in db.from(Employee).select()) {
        logger.info("Employee " + (row[Employee.id]!!).toString() + " :: " + row[Employee.name])
    }

    for (row in db.from(Department).select()) {
        logger.info("Department " + row[Department.name] + " :: " + row[Department.location])
    }
}
