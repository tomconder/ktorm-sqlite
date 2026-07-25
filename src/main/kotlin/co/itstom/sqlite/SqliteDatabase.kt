package co.itstom.sqlite

import org.ktorm.database.Database
import org.ktorm.logging.Slf4jLoggerAdapter
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class SqliteDatabase {
    private val logger: Logger = LoggerFactory.getLogger(this.javaClass)
    private val lineCommentRegex = Regex("--.*")

    fun connect(): Database {
        // in memory database
        // url = "jdbc:sqlite::memory:"

        // foreign_keys is off per-connection by default in SQLite; without it the FK is decorative
        return Database.connect(
            url = "jdbc:sqlite:sample.db?foreign_keys=on",
            logger = Slf4jLoggerAdapter(logger.name)
        )
    }

    fun execSqlScript(filename: String, database: Database) {
        database.useConnection { conn ->
            conn.createStatement().use { statement ->
                javaClass.classLoader
                    ?.getResourceAsStream(filename)
                    ?.bufferedReader()
                    ?.use { reader ->
                        // strip line comments before splitting, so a `;` inside one does not cut
                        // a statement in half. A `--` inside a string literal would still be
                        // mistaken for a comment
                        val script = reader.readText().replace(lineCommentRegex, "")

                        for (sql in script.split(';')) {
                            if (sql.any { it.isLetterOrDigit() }) {
                                statement.executeUpdate(sql)
                            }
                        }
                    }
            }
        }
    }
}
