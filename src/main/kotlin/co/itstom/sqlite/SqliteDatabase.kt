package co.itstom.sqlite

import org.ktorm.database.Database

class SqliteDatabase {
    private val lineCommentRegex = Regex("--.*")

    fun connect(): Database {
        // in memory database
        // url = "jdbc:sqlite::memory:"

        // foreign_keys is off per-connection by default in SQLite; without it the FK is decorative
        // logger omitted: Ktorm auto-detects the SLF4J binding on the classpath
        return Database.connect(url = "jdbc:sqlite:sample.db?foreign_keys=on")
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
