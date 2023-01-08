package net.yewton.sqlmiru

import net.sf.jsqlparser.schema.Table
import net.sf.jsqlparser.statement.Statements
import net.sf.jsqlparser.statement.delete.Delete
import net.sf.jsqlparser.statement.insert.Insert
import net.sf.jsqlparser.statement.merge.Merge
import net.sf.jsqlparser.statement.replace.Replace
import net.sf.jsqlparser.statement.update.Update
import net.sf.jsqlparser.statement.upsert.Upsert
import java.nio.file.Path

class TableInfoCollector(private val moduleName: String, private val sqlFilePath: Path) : AbstractStatementVisitor() {
    val tableInfoList = mutableListOf<TableInfo>()

    override fun visit(stmts: Statements) {
        stmts.statements.forEach { it.accept(this) }
    }

    override fun visit(delete: Delete) {
        addToTableInfoList("DELETE", delete.table)
    }

    override fun visit(update: Update) {
        addToTableInfoList("UPDATE", update.table)
    }

    override fun visit(insert: Insert) {
        addToTableInfoList("INSERT", insert.table)
    }

    override fun visit(replace: Replace) {
        addToTableInfoList("REPLACE", replace.table)
    }

    override fun visit(merge: Merge) {
        addToTableInfoList("MERGE", merge.table)
    }

    override fun visit(upsert: Upsert) {
        addToTableInfoList("UPSERT", upsert.table)
    }

    private fun addToTableInfoList(statement: String, table: Table) {
        tableInfoList.add(TableInfo(moduleName, sqlFilePath, statement, table.name))
    }
}
