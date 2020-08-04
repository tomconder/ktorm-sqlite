package co.itstom.sqlite.model

import me.liuwj.ktorm.schema.Table
import me.liuwj.ktorm.schema.int
import me.liuwj.ktorm.schema.varchar

object Department : Table<Nothing>("t_department") {
    val id = int("id").primaryKey()
    val name = varchar("name")
    val location = varchar("location")
}
