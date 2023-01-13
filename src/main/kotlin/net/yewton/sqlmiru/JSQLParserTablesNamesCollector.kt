package net.yewton.sqlmiru

import net.sf.jsqlparser.parser.CCJSqlParserUtil
import net.sf.jsqlparser.statement.Statement

abstract class JSQLParserTablesNamesCollector : TablesNamesCollector {

    override fun collect(body: String): List<String> {
        return try {
            val statements = CCJSqlParserUtil.parseStatements(body) ?: return emptyList()
            statements.statements.flatMap {
                collect(it)
            }
        } catch (
            @Suppress(
                "TooGenericExceptionCaught",
                "SwallowedException"
            ) e: Exception
        ) {
            emptyList()
        }
    }

    protected abstract fun collect(statement: Statement): List<String>
}
