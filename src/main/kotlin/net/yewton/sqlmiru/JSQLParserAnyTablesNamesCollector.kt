package net.yewton.sqlmiru

import net.sf.jsqlparser.statement.Statement
import net.sf.jsqlparser.statement.select.TableFunction
import net.sf.jsqlparser.util.TablesNamesFinder

class JSQLParserAnyTablesNamesCollector : JSQLParserTablesNamesCollector() {
    override fun collect(statement: Statement): List<String> =
        InternalFinder().getTableList(statement)

    private inner class InternalFinder : TablesNamesFinder() {
        override fun visit(tableFunction: TableFunction) {
            tableFunction.function.accept(this)
        }
    }
}
