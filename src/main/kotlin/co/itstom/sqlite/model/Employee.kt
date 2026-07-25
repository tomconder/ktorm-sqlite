package co.itstom.sqlite.model

import org.ktorm.entity.Entity
import org.ktorm.schema.*
import java.time.LocalDate

interface Employee : Entity<Employee> {
    companion object : Entity.Factory<Employee>()

    val id: Int
    var name: String
    var job: String
    var managerId: Int?
    var hireDate: LocalDate
    var salary: Long
    var department: Department
}

object Employees : Table<Employee>("t_employee") {
    val id = int("id").primaryKey().bindTo { it.id }
    val name = varchar("name").bindTo { it.name }
    val job = varchar("job").bindTo { it.job }
    val managerId = int("manager_id").bindTo { it.managerId }
    val hireDate = date("hire_date").bindTo { it.hireDate }
    val salary = long("salary").bindTo { it.salary }
    val departmentId = int("department_id").references(Departments) { it.department }
}
