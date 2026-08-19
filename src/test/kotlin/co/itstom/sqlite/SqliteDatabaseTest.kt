package co.itstom.sqlite

import kotlin.test.Test
import kotlin.test.assertEquals

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
}
