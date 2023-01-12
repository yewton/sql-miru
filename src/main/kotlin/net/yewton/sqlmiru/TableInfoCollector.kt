package net.yewton.sqlmiru

import net.sf.jsqlparser.schema.Table
import net.sf.jsqlparser.statement.Statements
import net.sf.jsqlparser.statement.delete.Delete
import net.sf.jsqlparser.statement.insert.Insert
import net.sf.jsqlparser.statement.merge.Merge
import net.sf.jsqlparser.statement.replace.Replace
import net.sf.jsqlparser.statement.update.Update
import net.sf.jsqlparser.statement.upsert.Upsert

class TableInfoCollector: AbstractStatementVisitor() {
    val tableInfoList = mutableListOf<TableInfo>()

    override fun visit(stmts: Statements) {
        stmts.statements.forEach { it.accept(this) }
    }

    override fun visit(delete: Delete) {
        addToTableInfoList(delete.table)
    }

    override fun visit(update: Update) {
        addToTableInfoList(update.table)
    }

    override fun visit(insert: Insert) {
        addToTableInfoList(insert.table)
    }

    override fun visit(replace: Replace) {
        addToTableInfoList(replace.table)
    }

    override fun visit(merge: Merge) {
        addToTableInfoList(merge.table)
    }

    override fun visit(upsert: Upsert) {
        addToTableInfoList(upsert.table)
    }

    private fun addToTableInfoList(table: Table) {
        tableInfoList.add(TableInfo(table.name.split(".").last()))
    }
}
