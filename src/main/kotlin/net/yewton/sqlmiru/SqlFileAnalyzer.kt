package net.yewton.sqlmiru

import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import java.nio.file.Path
import kotlin.io.path.readText

class SqlFileAnalyzer {

    suspend fun analyze(file: Path): SqlFileAnalyzeResult = coroutineScope {
        val sqlBody = file.readText().replace("\uFEFF", "") // BOM
        val mutatedTables = listOf(
            async {
                JSQLParserMutatedTablesNamesCollector().collect(sqlBody).map(::jsqlTableInfo)
            },
            async {
                RegexMutatedTablesNamesCollector().collect(sqlBody).map(::regexTableInfo)
            }
        )
        val otherTables = listOf(
            async {
                JSQLParserAnyTablesNamesCollector().collect(sqlBody).map(::jsqlTableInfo)
            },
            async {
                RegexSelectedTablesNamesCollector().collect(sqlBody).map(::regexTableInfo)
            }
        )
        val mutatedTableMap = buildNameToTableInfoMap(mutatedTables, emptyMap())
        val anyTableMap = buildNameToTableInfoMap(otherTables, mutatedTableMap)

        SqlFileAnalyzeResult(file, mutatedTableMap.values.toList(), anyTableMap.values.toList())
    }
    private fun jsqlTableInfo(table: String): TableInfo = TableInfo(TableName(table), setOf("jsqlParser"))

    private fun regexTableInfo(table: String): TableInfo = TableInfo(TableName(table), setOf("regex"))

    private suspend fun buildNameToTableInfoMap(
        tableInfoList: List<Deferred<List<TableInfo>>>,
        initial: Map<String, TableInfo>
    ): Map<String, TableInfo> =
        tableInfoList.flatMap { it.await() }.fold(initial) { acc, tableInfo ->
            val (tableName, collectedBy) = tableInfo
            val newValue = acc[tableName.value]?.let {
                it.copy(collectedBy = it.collectedBy + collectedBy)
            } ?: run { tableInfo }
            acc + (tableName.value to newValue)
        }
}
