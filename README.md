# ktorm-sqlite

A Kotlin sample application demonstrating how to use [Ktorm](https://github.com/kotlin-orm/ktorm), a lightweight, expressive Kotlin ORM framework, with the [SQLite](https://sqlite.org/) database engine via the [SQLite JDBC driver](https://github.com/xerial/sqlite-jdbc).

## ✨ Features

* 🔗 **Ktorm + SQLite integration** - connects Ktorm's idiomatic Kotlin DSL to a local SQLite database file
* 📋 **Entity mapping** - `Entity` interfaces bound to type-safe `Table` definitions via Ktorm's `bindTo` API
* 🧩 **Automatic joins** - `department_id` is mapped with `references`, so reading `employee.department` generates the join
* 🔐 **Enforced foreign keys** - SQLite leaves foreign keys off per connection; this project turns them on and verifies it at startup
* 🗃️ **SQL script execution** - loads and runs `.sql` initialization scripts from the classpath at startup
* 📝 **Structured logging** - async logging via Apache Log4j 2 with SLF4J and the LMAX Disruptor

## 📦 Dependencies

| Library | Version | Purpose |
|---|---|---|
| [Kotlin](https://kotlinlang.org/) | 2.4.10 | JVM language |
| [Ktorm Core](https://www.ktorm.org/) | 4.2.1 | Kotlin ORM framework |
| [Ktorm SQLite Support](https://www.ktorm.org/en/dialects-and-native-sql.html) | 4.2.1 | SQLite dialect for Ktorm |
| [SQLite JDBC](https://github.com/xerial/sqlite-jdbc) | 3.53.2.1 | SQLite JDBC driver |
| [Apache Log4j 2](https://logging.apache.org/log4j/2.x/) | 2.26.1 | Logging framework |
| [LMAX Disruptor](https://lmax-exchange.github.io/disruptor/) | 4.0.0 | High-performance async logging |

> **Java toolchain:** JDK 25 is required to build and run this project.

## 🛠️ Installation

### Prerequisites

* **JDK 25** or later - use [Adoptium](https://adoptium.net/) or [WinGet](https://learn.microsoft.com/en-us/windows/package-manager/winget/)
* **Gradle** - included via the Gradle wrapper (`./gradlew`), no separate installation needed

### Clone the repository

```bash
git clone https://github.com/tomconder/ktorm-sqlite.git
cd ktorm-sqlite
```

### Build the project

```bash
./gradlew build
```

## 🚀 Usage

### Running the application

```bash
./gradlew run
```

This will:

1. Connect to (or create) a local SQLite database file named `sample.db` in the working directory, with `foreign_keys=on`
2. Verify that foreign keys are actually enforced, failing fast if they are not
3. Execute `init-sqlite-data.sql` from the classpath to drop, recreate, and seed the `t_department` and `t_employee` tables
4. Query every employee and log it with its department, joined automatically through the entity reference

### Expected output

```
INFO  Main - Employee :: Vince | Engineer | Tech | Memphis | 2021-01-02
INFO  Main - Employee :: Sandy | Trainee | Tech | Memphis | 2022-03-04
INFO  Main - Employee :: Tom | Director | Finance | Dallas | 2023-05-06
INFO  Main - Employee :: Penny | Assistant | Finance | Dallas | 2024-06-07
INFO  Main - Employee :: Peggy | Sales Rep | Sales | Tampa | 2026-07-08
```

## 🗂️ Project Structure

```
src/
└── main/
    ├── kotlin/co/itstom/sqlite/
    │   ├── Main.kt               # Application entry point
    │   ├── SqliteDatabase.kt     # Database connection and SQL script runner
    │   └── model/
    │       ├── Department.kt     # Department entity and t_department table
    │       └── Employee.kt       # Employee entity and t_employee table
    └── resources/
        ├── init-sqlite-data.sql  # Schema creation and seed data
        └── log4j2.xml            # Log4j 2 configuration
```

## 🗄️ Database Schema

```sql
-- Teardown, with foreign keys off so a half-built database still drops
PRAGMA foreign_keys = OFF;
DROP TABLE IF EXISTS t_employee;
DROP TABLE IF EXISTS t_department;
PRAGMA foreign_keys = ON;

-- Departments
CREATE TABLE t_department (
    id       INTEGER PRIMARY KEY AUTOINCREMENT,
    name     TEXT    NOT NULL,
    location TEXT    NOT NULL
);

-- Employees
CREATE TABLE t_employee (
    id            INTEGER PRIMARY KEY AUTOINCREMENT,
    name          TEXT    NOT NULL,
    job           TEXT    NOT NULL,
    manager_id    INTEGER NULL     REFERENCES t_employee(id)   ON DELETE SET NULL,
    hire_date     TEXT    NOT NULL,
    salary        INTEGER NOT NULL,
    department_id INTEGER NOT NULL REFERENCES t_department(id) ON DELETE CASCADE
);
```

The two delete rules differ on purpose. An employee cannot exist without a department, so deleting a department deletes its employees. An employee without a manager is a normal state, so deleting a manager leaves the reports in place with `manager_id` set to null.

> \[!NOTE]
> `hire_date` is stored as ISO-8601 text, not as integer milliseconds. Integers are read back through `java.sql.Date`, which converts using the JVM's local time zone and can shift the date by a day.

> \[!NOTE]
> The initialization script intentionally drops and recreates all tables on every run. This fully resets the database each time the application starts for demonstration purposes. The teardown runs with foreign keys disabled so that a database left half-built by a failed run can still be dropped.

## 📝 License

This project is licensed under the MIT License. See [LICENSE](LICENSE) for details.
