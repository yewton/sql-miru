package net.yewton.sqlmiru

import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import java.nio.file.Path
import kotlin.io.path.readText

class SqlFileAnalyzer {

    companion object {
        private val mutatedTablesInfoCollectors: List<(String) -> List<TableInfo>>
        private val otherTablesInfoCollectors: List<(String) -> List<TableInfo>>

        init {
            fun tablesInfoCollector(
                tnc: TablesNamesCollector,
                collectedBy: String
            ): (String) -> List<TableInfo> = { sqlBody ->
                tnc.collect(sqlBody).map { TableInfo(TableName(it), setOf(collectedBy)) }
            }

            mutatedTablesInfoCollectors = listOf(
                tablesInfoCollector(JSQLParserMutatedTablesNamesCollector(), "jsqlParser"),
                tablesInfoCollector(RegexMutatedTablesNamesCollector(), "regex")
            )
            otherTablesInfoCollectors = listOf(
                tablesInfoCollector(JSQLParserAnyTablesNamesCollector(), "jsqlParser"),
                tablesInfoCollector(RegexSelectedTablesNamesCollector(), "regex")
            )
        }
    }

    suspend fun analyze(file: Path): SqlFileAnalyzeResult = coroutineScope {
        val sqlBody = file.readText().replace("\uFEFF", "") // BOM
        val mutatedTablesDeferred = mutatedTablesInfoCollectors.map { async { it(sqlBody) } }
        val otherTablesDeferred = otherTablesInfoCollectors.map { async { it(sqlBody) } }
        val mutatedTables = mutatedTablesDeferred.awaitAll().flatten()
        val anyTables = mutatedTables + otherTablesDeferred.awaitAll().flatten()
        SqlFileAnalyzeResult(file, aggregateCollectedBy(mutatedTables), aggregateCollectedBy(anyTables))
    }

    private fun aggregateCollectedBy(
        tableInfoList: List<TableInfo>
    ): List<TableInfo> = tableInfoList.groupingBy { it.tableName }
        .reduce { _, acc, tableInfo -> acc.copy(collectedBy = acc.collectedBy + tableInfo.collectedBy) }
        .values.toList()
}
