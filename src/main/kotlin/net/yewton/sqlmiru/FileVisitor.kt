package net.yewton.sqlmiru

import net.sf.jsqlparser.parser.CCJSqlParserUtil
import net.sf.jsqlparser.util.TablesNamesFinder
import java.nio.file.FileVisitResult
import java.nio.file.Path
import java.nio.file.SimpleFileVisitor
import java.nio.file.attribute.BasicFileAttributes
import kotlin.io.path.extension
import kotlin.io.path.readText

class FileVisitor(val moduleName: String) : SimpleFileVisitor<Path>() {
    val mutatingTableInfoList = mutableListOf<TableInfo>()
    val tableNames = mutableListOf<String>()

    private val fallbackPattern: Regex
    private val fallbackPattern2: Regex

    init {
        val regexOptions = setOf(RegexOption.IGNORE_CASE, RegexOption.DOT_MATCHES_ALL)
        // language=regexp
        val tableNamePattern = """\s+((?!(?:into|from|set|all|wait|nowait|of|dual))(?:\w+\.)?\w+)"""
        fallbackPattern = Regex("""(update|insert|delete|merge|upsert|into)$tableNamePattern""", regexOptions)
        fallbackPattern2 = Regex("""(from|join)$tableNamePattern""", regexOptions)
    }

    override fun visitFile(file: Path, attrs: BasicFileAttributes): FileVisitResult {
        if (file.extension != "sql") {
            return FileVisitResult.CONTINUE
        }
        val tableInfoCollector = TableInfoCollector(moduleName, file)
        val tablesNamesFinder = TablesNamesFinder()
        val sqlBody = file.readText()
            .replace(Regex("--.*$"), "")
            .replace(Regex("""/\*.*?\*/""", RegexOption.DOT_MATCHES_ALL), " ")
        try {
            val statements = CCJSqlParserUtil.parseStatements(sqlBody)
            statements.accept(tableInfoCollector)
            mutatingTableInfoList.addAll(tableInfoCollector.tableInfoList)
            tableNames.addAll(
                statements.statements.flatMap {
                    tablesNamesFinder.getTableList(it)
                }
            )
        } catch (
            @Suppress(
                "TooGenericExceptionCaught",
                "SwallowedException"
            ) e: Exception
        ) {
            fallbackPattern.findAll(sqlBody).forEach {
                val tableName = it.groupValues[2]
                mutatingTableInfoList.add(TableInfo(moduleName, file, it.groupValues[1].uppercase(), tableName, true))
                tableNames.add(tableName)
            }
            fallbackPattern2.findAll(sqlBody).forEach {
                tableNames.add(it.groupValues[2])
            }
        }
        return FileVisitResult.CONTINUE
    }
}
