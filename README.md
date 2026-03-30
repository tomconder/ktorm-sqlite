# ktorm-sqlite

A Kotlin sample application demonstrating how to use [Ktorm](https://github.com/kotlin-orm/ktorm), a lightweight, expressive Kotlin ORM framework, with the [SQLite](https://sqlite.org/) database engine via the [SQLite JDBC driver](https://github.com/xerial/sqlite-jdbc).

## ✨ Features

* 🔗 **Ktorm + SQLite integration** - connects Ktorm's idiomatic Kotlin DSL to a local SQLite database file
* 📋 **Schema definition** - type-safe table and column definitions using Ktorm's `Table` API
* 🗃️ **SQL script execution** - loads and runs `.sql` initialization scripts from the classpath at startup
* 🔍 **Query DSL** - demonstrates fluent, type-safe SELECT queries across multiple tables
* 📝 **Structured logging** - async logging via Apache Log4j 2 with SLF4J and the LMAX Disruptor

## 📦 Dependencies

| Library | Version | Purpose |
|---|---|---|
| [Kotlin](https://kotlinlang.org/) | 2.3.20 | JVM language |
| [Ktorm Core](https://www.ktorm.org/) | 4.1.1 | Kotlin ORM framework |
| [Ktorm SQLite Support](https://www.ktorm.org/en/dialects-and-native-sql.html) | 4.1.1 | SQLite dialect for Ktorm |
| [SQLite JDBC](https://github.com/xerial/sqlite-jdbc) | 3.51.3.0 | SQLite JDBC driver |
| [Apache Log4j 2](https://logging.apache.org/log4j/2.x/) | 2.25.4 | Logging framework |
| [Guava](https://github.com/google/guava) | 33.5.0-jre | Google core Java libraries |
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

1. Connect to (or create) a local SQLite database file named `sample.db` in the working directory
2. Execute `init-sqlite-data.sql` from the classpath to drop, recreate, and seed the `t_department` and `t_employee` tables
3. Query and log all employees and departments to the console

### Expected output

```
INFO  Main - Employee 1 :: Vince
INFO  Main - Employee 2 :: Mary
INFO  Main - Employee 3 :: Tom
INFO  Main - Employee 4 :: Penny
INFO  Main - Department Tech :: Memphis
INFO  Main - Department Finance :: Dallas
```

## 🗂️ Project Structure

```
src/
└── main/
    ├── kotlin/co/itstom/sqlite/
    │   ├── Main.kt               # Application entry point
    │   ├── SqliteDatabase.kt     # Database connection and SQL script runner
    │   └── model/
    │       ├── Department.kt     # Ktorm table definition for t_department
    │       └── Employee.kt       # Ktorm table definition for t_employee
    └── resources/
        ├── init-sqlite-data.sql  # Schema creation and seed data
        └── log4j2.xml            # Log4j 2 configuration
```

## 🗄️ Database Schema

```sql
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
    manager_id    INTEGER NULL,
    hire_date     INTEGER NOT NULL,
    salary        INTEGER NOT NULL,
    department_id INTEGER NOT NULL
);
```

> \[!NOTE]
> The initialization script intentionally drops and recreates all tables on every run. This fully resets the database each time the application starts for demonstration purposes.

## 📝 License

This project is licensed under the MIT License. See [LICENSE](LICENSE) for details.
