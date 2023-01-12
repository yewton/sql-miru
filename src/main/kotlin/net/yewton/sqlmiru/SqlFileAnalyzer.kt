package net.yewton.sqlmiru

import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import net.sf.jsqlparser.parser.CCJSqlParserUtil
import java.nio.file.Path
import kotlin.io.path.readText

class SqlFileAnalyzer {

    companion object {
        private const val MAX_CONSECUTIVE_TABLES = 10
        private val fallbackPattern: Regex
        private val fallbackPattern2: Regex

        init {
            val regexOptions = setOf(RegexOption.IGNORE_CASE, RegexOption.DOT_MATCHES_ALL)
            val reservedWordsPattern = listOf(
                "into", "from", "set", "all", "wait", "nowait", "of", "dual",
                "when", "values", "using", "limit", "is", "null", "union",
                "as", "with", "where", "select"
            ).joinToString("|")
            // language=regexp
            val singleTableNamePattern = """\s+(?!$reservedWordsPattern)(?:\w+\.)?(\w+)"""
            val multipleTableNamesPattern = singleTableNamePattern +
                generateSequence { "(?:$singleTableNamePattern)" }
                    .take(MAX_CONSECUTIVE_TABLES - 1).joinToString("?")
            fallbackPattern = Regex(
                """(?:update|insert|delete|merge|upsert|into)$multipleTableNamesPattern""",
                regexOptions
            )
            fallbackPattern2 = Regex("""(?:from|join)$singleTableNamePattern""", regexOptions)
        }
    }

    suspend fun analyze(file: Path): SqlFileAnalyzeResult {
        val tableInfoCollector = TableInfoCollector()
        val tablesNamesFinder = MyTablesNamesFinder()
        val sqlBody = file.readText()
            .replace("\uFEFF", "") // BOM
            .replace(Regex("--.*"), "")
            .replace(Regex("""/\*.*?\*/""", RegexOption.DOT_MATCHES_ALL), " ")
        try {
            return coroutineScope {
                val statements = CCJSqlParserUtil.parseStatements(sqlBody) ?: throw IllegalArgumentException("hoge")

                val tableInfoList = async {
                    statements.accept(tableInfoCollector)
                    tableInfoCollector.tableInfoList
                }
                val tableNames = statements.statements.map {
                    async { tablesNamesFinder.getTableList(it).map { it.split(".").last() } }
                }
                SqlFileAnalyzeResult(file, tableInfoList.await(), tableNames.awaitAll().flatten())
            }
        } catch (
            @Suppress(
                "TooGenericExceptionCaught",
                "SwallowedException"
            ) e: Exception
        ) {
            return doFallbackAnalyze(sqlBody, file)
        }
    }

    private fun doFallbackAnalyze(sqlBody: String, file: Path): SqlFileAnalyzeResult {
        val mutatingTableInfoList = fallbackPattern.findAll(sqlBody).flatMap {
            it.groupValues.drop(1)
                .filterNot(String::isBlank)
                .map { tableName -> TableInfo(tableName, true) }
        }.toList()
        val allTableNames = fallbackPattern2.findAll(sqlBody).flatMap {
            listOf(it.groupValues[1]) + mutatingTableInfoList.map { info -> info.tableName }
        }.toList()

        return SqlFileAnalyzeResult(file, mutatingTableInfoList, allTableNames)
    }
}
