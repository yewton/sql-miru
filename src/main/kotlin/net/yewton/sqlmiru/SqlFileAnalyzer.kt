package net.yewton.sqlmiru

import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import net.sf.jsqlparser.parser.CCJSqlParserUtil
import java.nio.file.Path
import kotlin.io.path.readText

class SqlFileAnalyzer {

    companion object {
        private val fallbackPattern: Regex
        private val fallbackPattern2: Regex

        init {
            val regexOptions = setOf(RegexOption.IGNORE_CASE, RegexOption.DOT_MATCHES_ALL)
            // language=regexp
            val nonTableNamePattern = """\s+((?!into|from|set|all|wait|nowait|of|dual|when)(?:\w+\.)?\w+)"""
            fallbackPattern = Regex("""(update|insert|delete|merge|upsert|into)$nonTableNamePattern""", regexOptions)
            fallbackPattern2 = Regex("""(from|join)$nonTableNamePattern""", regexOptions)
        }
    }

    suspend fun analyze(file: Path): SqlFileAnalyzeResult = coroutineScope {
        val tableInfoCollector = TableInfoCollector(file)
        val tablesNamesFinder = MyTablesNamesFinder()
        val sqlBody = file.readText()
            .replace("\uFEFF", "") // BOM
            .replace(Regex("--.*"), "")
            .replace(Regex("""/\*.*?\*/""", RegexOption.DOT_MATCHES_ALL), " ")
        try {
            val statements = CCJSqlParserUtil.parseStatements(sqlBody)
            val job = launch { statements.accept(tableInfoCollector) }
            val tableNames = statements.statements.map {
                async { tablesNamesFinder.getTableList(it).map { it.split(".").last() } }
            }.awaitAll().flatten()
            job.join()
            SqlFileAnalyzeResult(file, tableInfoCollector.tableInfoList, tableNames)
        } catch (
            @Suppress(
                "TooGenericExceptionCaught",
                "SwallowedException"
            ) e: Exception
        ) {
            doFallbackAnalyze(sqlBody, file)
        }
    }

    private fun doFallbackAnalyze(sqlBody: String, file: Path): SqlFileAnalyzeResult {
        val mutatingTableInfoList = fallbackPattern.findAll(sqlBody).map {
            TableInfo(file, it.groupValues[1].uppercase(), it.groupValues[2], true)
        }.toList()
        val allTableNames = fallbackPattern2.findAll(sqlBody).flatMap {
            listOf(it.groupValues[2]) + mutatingTableInfoList.map { info -> info.tableName }
        }.toList()

        return SqlFileAnalyzeResult(file, mutatingTableInfoList, allTableNames)
    }
}
