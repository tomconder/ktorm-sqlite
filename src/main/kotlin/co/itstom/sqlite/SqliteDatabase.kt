package co.itstom.sqlite

import org.ktorm.database.Database

class SqliteDatabase {
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
                        for (sql in splitSqlStatements(reader.readText())) {
                            statement.executeUpdate(sql)
                        }
                    }
            }
        }
    }
}

// Index just past the end of a `--` line comment starting at `from`.
private fun endOfLineComment(script: String, from: Int): Int =
    script.indexOf('\n', from).let { if (it == -1) script.length else it + 1 }

// Index just past the end of a `/* */` block comment whose body starts at `from`.
private fun endOfBlockComment(script: String, from: Int): Int =
    script.indexOf("*/", from).let { if (it == -1) script.length else it + 2 }

// Appends a quoted literal/identifier (opening `quote` already consumed, body
// starts at `from`) to this builder, honoring '' / "" as an escaped quote.
// Returns the index just past the closing quote.
private fun StringBuilder.appendQuoted(script: String, quote: Char, from: Int): Int {
    append(quote)
    var i = from
    while (i < script.length) {
        append(script[i])
        if (script[i] != quote) {
            i++
            continue
        }
        i++
        if (script.getOrNull(i) == quote) append(script[i++]) else return i
    }
    return i
}

// Splits a SQL script into individual statements on top-level `;`, skipping
// `--` line comments, `/* */` block comments, and `;`/`--` inside quoted
// strings or identifiers.
internal fun splitSqlStatements(script: String): List<String> {
    val statements = mutableListOf<String>()
    val current = StringBuilder()
    var i = 0
    while (i < script.length) {
        val c = script[i]
        when {
            c == '-' && script.getOrNull(i + 1) == '-' -> i = endOfLineComment(script, i)
            c == '/' && script.getOrNull(i + 1) == '*' -> i = endOfBlockComment(script, i + 2)
            c == '\'' || c == '"' -> i = current.appendQuoted(script, c, i + 1)
            c == ';' -> {
                statements.add(current.toString())
                current.clear()
                i++
            }

            else -> {
                current.append(c)
                i++
            }
        }
    }
    statements.add(current.toString())
    return statements.filter { it.any(Char::isLetterOrDigit) }
}
