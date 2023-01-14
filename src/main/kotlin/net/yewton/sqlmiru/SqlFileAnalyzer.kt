package net.yewton.sqlmiru

import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import java.nio.file.Path
import kotlin.io.path.readText

class SqlFileAnalyzer {

    suspend fun analyze(file: Path): SqlFileAnalyzeResult = coroutineScope {
        val sqlBody = file.readText().replace("\uFEFF", "") // BOM
        val tableInfoCollectorBuilder = tableInfoCollectorBuilderFor(sqlBody)
        val collectJsqlTableInfo = tableInfoCollectorBuilder(::jsqlTableInfo)
        val collectRegexTableInfo = tableInfoCollectorBuilder(::regexTableInfo)
        val mutatedTablesDeferred = listOf(
            async { collectJsqlTableInfo(JSQLParserMutatedTablesNamesCollector()) },
            async { collectRegexTableInfo(RegexMutatedTablesNamesCollector()) }
        )
        val otherTablesDeferred = listOf(
            async { collectJsqlTableInfo(JSQLParserAnyTablesNamesCollector()) },
            async { collectRegexTableInfo(RegexSelectedTablesNamesCollector()) }
        )
        val mutatedTables = mutatedTablesDeferred.awaitAll().flatten()
        val anyTables = mutatedTables + otherTablesDeferred.awaitAll().flatten()
        SqlFileAnalyzeResult(file, aggregateCollectedBy(mutatedTables), aggregateCollectedBy(anyTables))
    }
    private fun jsqlTableInfo(table: String): TableInfo = TableInfo(TableName(table), setOf("jsqlParser"))

    private fun regexTableInfo(table: String): TableInfo = TableInfo(TableName(table), setOf("regex"))

    private fun tableInfoCollectorBuilderFor(sqlBody: String):
                ((String) -> TableInfo) -> (TablesNamesCollector) -> List<TableInfo> = { buildTableInfo ->
        { tc -> tc.collect(sqlBody).map(buildTableInfo) }
    }

    private fun aggregateCollectedBy(
        tableInfoList: List<TableInfo>
    ): List<TableInfo> = tableInfoList.groupingBy { it.tableName }
        .reduce { _, acc, tableInfo -> acc.copy(collectedBy = acc.collectedBy + tableInfo.collectedBy) }
        .values.toList()
}
