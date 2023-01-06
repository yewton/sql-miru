package net.yewton.sqlmiru

import net.sf.jsqlparser.statement.Statements
import net.sf.jsqlparser.util.TablesNamesFinder

class TablesNamesCollector : TablesNamesFinder() {
    override fun visit(statements: Statements) {
        statements.statements.forEach { it.accept(this) }
    }
}
