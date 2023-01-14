package net.yewton.sqlmiru

import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.runBlocking
import picocli.CommandLine
import picocli.CommandLine.Command
import picocli.CommandLine.Parameters
import java.nio.file.Path
import java.util.concurrent.Callable
import kotlin.io.path.isDirectory
import kotlin.io.path.name
import kotlin.system.exitProcess

@Command(
    name = "sql-miru",
    mixinStandardHelpOptions = true,
    description = ["TBW"]
)
class App : Callable<Int> {

    @CommandLine.Spec
    lateinit var commandSpec: CommandLine.Model.CommandSpec

    @Parameters(description = ["TBW"])
    var dirs: List<Path> = emptyList()

    override fun call(): Int {
        val nonDirectoryPaths = dirs.filter { !it.isDirectory() }
        if (dirs.isEmpty() || nonDirectoryPaths.isNotEmpty()) {
            System.err.println("ディレクトリを指定してね")
            nonDirectoryPaths.forEach { System.err.println("$it はディレクトリじゃないよ") }
            commandSpec.commandLine().usage(System.err)
            return 1
        }

        buildPrintRows(dirs)
            .sortedWith(Row.comparator)
            .forEach {
                println(it)
            }
        return 0
    }

    private fun analyze(dirs: List<Path>): List<FlatData> = runBlocking {
        val sqlFileAnalyzer = SqlFileAnalyzer()
        dirs.flatMap { dir ->
            async {
                SqlPathFinder().perform(dir).map { sqlPath ->
                    async { sqlFileAnalyzer.analyze(sqlPath) }
                }.awaitAll()
            }.await().flatMap { result ->
                val categorized = result.mutatedTableInfoList.map { Pair("変更", it) } +
                    result.anyTableInfoList.map { Pair("参照", it) }
                categorized.map { (category, info) ->
                    FlatData(category, dir, result.filePath, info)
                }
            }
        }
    }

    private fun buildPrintRows(dirs: List<Path>): List<Row> =
        analyze(dirs).groupBy { Pair(it.category, it.table.tableName) }
            .map { (key, flatDataList) ->
                val (category, tableName) = key
                val dirSet = flatDataList.map { it.dir }.toSet()
                val dirCols = dirs.map {
                    if (dirSet.contains(it)) it.name else ""
                }
                val collectedBy = flatDataList.map { it.table.collectedBy }
                    .reduce { a, b -> a + b }
                Row(
                    category,
                    "未分類",
                    tableName.value,
                    "",
                    "",
                    dirCols,
                    collectedBy.sorted()
                )
            }
}

private data class FlatData(
    val category: String,
    val dir: Path,
    val file: Path,
    val table: TableInfo
)

private data class Row(
    val category: String,
    val group: String,
    val physicalName: String,
    val logicalName: String,
    val url: String,
    val dirs: List<String>,
    val collectedBy: List<String>
) {
    companion object {
        val comparator: Comparator<Row> = Comparator.comparing(Row::category)
            .thenComparing(Row::group)
            .thenComparingInt(Row::dirsNum)
            .reversed()
            .thenComparing(Row::physicalName)
    }

    val dirsNum = dirs.filter(String::isNotBlank).size

    override fun toString(): String {
        val cols = listOf(category, dirsNum, group, physicalName, logicalName, url) +
            dirs + collectedBy
        return cols.joinToString("\t")
    }
}

@Suppress("SpreadOperator")
fun main(args: Array<String>): Unit = exitProcess(CommandLine(App()).execute(*args))
