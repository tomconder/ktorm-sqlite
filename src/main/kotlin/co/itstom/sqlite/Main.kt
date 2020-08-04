package co.itstom.sqlite

import co.itstom.sqlite.model.Department
import co.itstom.sqlite.model.Employee
import me.liuwj.ktorm.dsl.from
import me.liuwj.ktorm.dsl.select
import org.slf4j.Logger
import org.slf4j.LoggerFactory

val logger: Logger = LoggerFactory.getLogger("Main")

fun main() {
    val sqliteDatabase = SqliteDatabase()
    val db = sqliteDatabase.connect()

    sqliteDatabase.execSqlScript("init-sqlite-data.sql", db)

    for (row in db.from(Employee).select()) {
        logger.info(Integer.toString(row[Employee.id]!!) + " :: " + row[Employee.name])
    }

    for (row in db.from(Department).select()) {
        logger.info(row[Department.name] + " :: " + row[Department.location])
    }
}
