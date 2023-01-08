package net.yewton.sqlmiru

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

@Suppress("TooManyFunctions")
abstract class AbstractStatementVisitor: StatementVisitor {
    override fun visit(analyze: Analyze) {
        // no-op
    }

    override fun visit(savepointStatement: SavepointStatement) {
        // no-op
    }

    override fun visit(rollbackStatement: RollbackStatement) {
        // no-op
    }

    override fun visit(comment: Comment) {
        // no-op
    }

    override fun visit(commit: Commit) {
        // no-op
    }

    override fun visit(delete: Delete) {
        // no-op
    }

    override fun visit(update: Update) {
        // no-op
    }

    override fun visit(insert: Insert) {
        // no-op
    }

    override fun visit(replace: Replace) {
        // no-op
    }

    override fun visit(drop: Drop) {
        // no-op
    }

    override fun visit(truncate: Truncate) {
        // no-op
    }

    override fun visit(createIndex: CreateIndex) {
        // no-op
    }

    override fun visit(aThis: CreateSchema) {
        // no-op
    }

    override fun visit(createTable: CreateTable) {
        // no-op
    }

    override fun visit(createView: CreateView) {
        // no-op
    }

    override fun visit(alterView: AlterView) {
        // no-op
    }

    override fun visit(alter: Alter) {
        // no-op
    }

    override fun visit(stmts: Statements) {
        // no-op
    }

    override fun visit(execute: Execute) {
        // no-op
    }

    override fun visit(set: SetStatement) {
        // no-op
    }

    override fun visit(reset: ResetStatement) {
        // no-op
    }

    override fun visit(set: ShowColumnsStatement) {
        // no-op
    }

    override fun visit(showTables: ShowTablesStatement) {
        // no-op
    }

    override fun visit(merge: Merge) {
        // no-op
    }

    override fun visit(select: Select) {
        // no-op
    }

    override fun visit(upsert: Upsert) {
        // no-op
    }

    override fun visit(use: UseStatement) {
        // no-op
    }

    override fun visit(block: Block) {
        // no-op
    }

    override fun visit(values: ValuesStatement) {
        // no-op
    }

    override fun visit(describe: DescribeStatement) {
        // no-op
    }

    override fun visit(aThis: ExplainStatement) {
        // no-op
    }

    override fun visit(aThis: ShowStatement) {
        // no-op
    }

    override fun visit(aThis: DeclareStatement) {
        // no-op
    }

    override fun visit(grant: Grant) {
        // no-op
    }

    override fun visit(createSequence: CreateSequence) {
        // no-op
    }

    override fun visit(alterSequence: AlterSequence) {
        // no-op
    }

    override fun visit(createFunctionalStatement: CreateFunctionalStatement) {
        // no-op
    }

    override fun visit(createSynonym: CreateSynonym) {
        // no-op
    }

    override fun visit(alterSession: AlterSession) {
        // no-op
    }

    override fun visit(aThis: IfElseStatement) {
        // no-op
    }

    override fun visit(renameTableStatement: RenameTableStatement) {
        // no-op
    }

    override fun visit(purgeStatement: PurgeStatement) {
        // no-op
    }

    override fun visit(alterSystemStatement: AlterSystemStatement) {
        // no-op
    }

    override fun visit(unsupportedStatement: UnsupportedStatement) {
        // no-op
    }
}
