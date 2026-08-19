package co.itstom.sqlite

import org.ktorm.database.Database
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class SqliteDatabaseTest {
    @Test
    fun `splits plain statements on semicolon`() {
        assertEquals(
            listOf("select 1", " select 2"),
            splitSqlStatements("select 1; select 2;")
        )
    }

    @Test
    fun `ignores line and block comments`() {
        assertEquals(
            listOf("select 1"),
            splitSqlStatements("-- comment; with fake terminator\nselect 1; /* block ; comment */")
        )
    }

    @Test
    fun `keeps semicolons and dashes inside quoted literals intact`() {
        assertEquals(
            listOf("insert into t values ('a;b--c')", " select 'd\"\"e'"),
            splitSqlStatements("insert into t values ('a;b--c'); select 'd\"\"e';")
        )
    }

    @Test
    fun `keeps semicolons inside bracket and backtick identifiers intact`() {
        assertEquals(
            listOf("select [odd;name]", " select `weird;col`"),
            splitSqlStatements("select [odd;name]; select `weird;col`;")
        )
    }

    @Test
    fun `reports a missing script instead of silently doing nothing`() {
        // in-memory, so the test does not leave a sample.db behind
        val db = Database.connect(url = "jdbc:sqlite::memory:")
        val error = assertFailsWith<IllegalStateException> {
            SqliteDatabase().execSqlScript("no-such-script.sql", db)
        }
        assertEquals("SQL script not found on the classpath: no-such-script.sql", error.message)
    }
}
