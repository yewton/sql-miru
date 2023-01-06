package net.yewton.sqlmiru

import net.sf.jsqlparser.schema.Table
import net.sf.jsqlparser.statement.Block
import net.sf.jsqlparser.statement.Commit
import net.sf.jsqlparser.statement.CreateFunctionalStatement
import net.sf.jsqlparser.statement.DeclareStatement
import net.sf.jsqlparser.statement.DescribeStatement
import net.sf.jsqlparser.statement.ExplainStatement
import net.sf.jsqlparser.statement.IfElseStatement
import net.sf.jsqlparser.statement.PurgeStatement
import net.sf.jsqlparser.statement.ResetStatement
import net.sf.jsqlparser.statement.RollbackStatement
import net.sf.jsqlparser.statement.SavepointStatement
import net.sf.jsqlparser.statement.SetStatement
import net.sf.jsqlparser.statement.ShowColumnsStatement
import net.sf.jsqlparser.statement.ShowStatement
import net.sf.jsqlparser.statement.StatementVisitor
import net.sf.jsqlparser.statement.Statements
import net.sf.jsqlparser.statement.UnsupportedStatement
import net.sf.jsqlparser.statement.UseStatement
import net.sf.jsqlparser.statement.alter.Alter
import net.sf.jsqlparser.statement.alter.AlterSession
import net.sf.jsqlparser.statement.alter.AlterSystemStatement
import net.sf.jsqlparser.statement.alter.RenameTableStatement
import net.sf.jsqlparser.statement.alter.sequence.AlterSequence
import net.sf.jsqlparser.statement.analyze.Analyze
import net.sf.jsqlparser.statement.comment.Comment
import net.sf.jsqlparser.statement.create.index.CreateIndex
import net.sf.jsqlparser.statement.create.schema.CreateSchema
import net.sf.jsqlparser.statement.create.sequence.CreateSequence
import net.sf.jsqlparser.statement.create.synonym.CreateSynonym
import net.sf.jsqlparser.statement.create.table.CreateTable
import net.sf.jsqlparser.statement.create.view.AlterView
import net.sf.jsqlparser.statement.create.view.CreateView
import net.sf.jsqlparser.statement.delete.Delete
import net.sf.jsqlparser.statement.drop.Drop
import net.sf.jsqlparser.statement.execute.Execute
import net.sf.jsqlparser.statement.grant.Grant
import net.sf.jsqlparser.statement.insert.Insert
import net.sf.jsqlparser.statement.merge.Merge
import net.sf.jsqlparser.statement.replace.Replace
import net.sf.jsqlparser.statement.select.Select
import net.sf.jsqlparser.statement.show.ShowTablesStatement
import net.sf.jsqlparser.statement.truncate.Truncate
import net.sf.jsqlparser.statement.update.Update
import net.sf.jsqlparser.statement.upsert.Upsert
import net.sf.jsqlparser.statement.values.ValuesStatement
import java.nio.file.Path

class TableInfoCollector(private val moduleName: String, private val sqlFilePath: Path) : StatementVisitor {
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

    override fun visit(select: Select) {
    }

    override fun visit(analyze: Analyze) {
    }

    override fun visit(savepointStatement: SavepointStatement) {
    }

    override fun visit(rollbackStatement: RollbackStatement) {
    }

    override fun visit(comment: Comment) {
    }

    override fun visit(commit: Commit) {
    }

    override fun visit(drop: Drop?) {
    }

    override fun visit(truncate: Truncate?) {
    }

    override fun visit(createIndex: CreateIndex?) {

    }

    override fun visit(aThis: CreateSchema?) {

    }

    override fun visit(createTable: CreateTable?) {

    }

    override fun visit(createView: CreateView?) {

    }

    override fun visit(alterView: AlterView?) {

    }

    override fun visit(alter: Alter?) {

    }


    override fun visit(execute: Execute?) {

    }

    override fun visit(set: SetStatement?) {

    }

    override fun visit(reset: ResetStatement?) {

    }

    override fun visit(set: ShowColumnsStatement?) {

    }

    override fun visit(showTables: ShowTablesStatement?) {

    }


    override fun visit(use: UseStatement?) {

    }

    override fun visit(block: Block?) {

    }

    override fun visit(values: ValuesStatement?) {

    }

    override fun visit(describe: DescribeStatement?) {

    }

    override fun visit(aThis: ExplainStatement?) {

    }

    override fun visit(aThis: ShowStatement?) {

    }

    override fun visit(aThis: DeclareStatement?) {

    }

    override fun visit(grant: Grant?) {

    }

    override fun visit(createSequence: CreateSequence?) {

    }

    override fun visit(alterSequence: AlterSequence?) {

    }

    override fun visit(createFunctionalStatement: CreateFunctionalStatement?) {

    }

    override fun visit(createSynonym: CreateSynonym?) {

    }

    override fun visit(alterSession: AlterSession?) {

    }

    override fun visit(aThis: IfElseStatement?) {

    }

    override fun visit(renameTableStatement: RenameTableStatement?) {

    }

    override fun visit(purgeStatement: PurgeStatement?) {

    }

    override fun visit(alterSystemStatement: AlterSystemStatement?) {

    }

    override fun visit(unsupportedStatement: UnsupportedStatement?) {

    }
}
