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
    var paths: List<Path> = emptyList()

    override fun call(): Int {
        val nonDirectoryPaths = paths.filter { !it.isDirectory() }
        if (paths.isEmpty() || nonDirectoryPaths.isNotEmpty()) {
            System.err.println("ディレクトリを指定してね")
            nonDirectoryPaths.forEach { System.err.println("$it はディレクトリじゃないよ") }
            commandSpec.commandLine().usage(System.err)
            return 1
        }
        val pathToResultsMap = analyze(paths)

        val mutatingTableToModulesMap = mutableMapOf<String, Set<String>>()
        val tableToModulesMap = mutableMapOf<String, Set<String>>()

        pathToResultsMap.forEach { (path, results) ->
            results.forEach { result ->
                result.mutatingTableInfoList.forEach {
                    mutatingTableToModulesMap.merge(it.tableName, setOf(path.name), Set<String>::plus)
                }
                result.allTableNames.forEach {
                    tableToModulesMap.merge(it, setOf(path.name), Set<String>::plus)
                }
            }
        }
        tableToModulesMap.forEach { (k, v) ->
            if (1 < v.size) {
                println("${v.size}\t$k は $v から参照されているよ")
            }
        }

        mutatingTableToModulesMap.forEach { (k, v) ->
            if (1 < v.size) {
                println("${v.size}\t$k は $v から変更されているよ")
            }
        }

        return 0
    }

    private fun analyze(dirs: List<Path>): Map<Path, List<SqlFileAnalyzeResult>> = runBlocking {
        val sqlFileAnalyzer = SqlFileAnalyzer()
        dirs.map { dir ->
            async {
                dir to SqlPathFinder().perform(dir).map { sqlPath ->
                    async {
                        sqlFileAnalyzer.analyze(sqlPath)
                    }
                }.awaitAll()
            }
        }.awaitAll().toMap()
    }
}

@Suppress("SpreadOperator")
fun main(args: Array<String>): Unit = exitProcess(CommandLine(App()).execute(*args))
