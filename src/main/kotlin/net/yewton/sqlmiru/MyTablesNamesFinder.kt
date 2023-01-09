package net.yewton.sqlmiru

import net.sf.jsqlparser.statement.select.TableFunction
import net.sf.jsqlparser.util.TablesNamesFinder

class MyTablesNamesFinder : TablesNamesFinder() {
    override fun visit(tableFunction: TableFunction) {
        tableFunction.function.accept(this)
    }
}
