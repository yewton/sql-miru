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

        val dirToResultList = analyze(dirs)
        val initial = emptyMap<TableName, Set<String>>()
        val (mutatedTableToDirsMap, anyTableToDirsMap) =
            dirToResultList.fold(Pair(initial, initial)) { (mutated, any), (dir, result) ->
                Pair(
                    tableNameToDirMap(dir.name, result.mutatedTableInfoList, mutated),
                    tableNameToDirMap(dir.name, result.anyTableInfoList, any)
                )
            }
        println("---------- 参照 ----------")
        anyTableToDirsMap.forEach { (k, v) ->
            if (1 < v.size) {
                println("${v.size}\t${k.value}\t${v.joinToString("\t")}")
            }
        }
        println("---------- 変更 ----------")
        mutatedTableToDirsMap.forEach { (k, v) ->
            if (1 < v.size) {
                println("${v.size}\t${k.value}\t${v.joinToString("\t")}")
            }
        }
        return 0
    }

    private fun analyze(dirs: List<Path>): List<Pair<Path, SqlFileAnalyzeResult>> = runBlocking {
        val sqlFileAnalyzer = SqlFileAnalyzer()
        dirs.flatMap { dir ->
            async {
                SqlPathFinder().perform(dir).map { sqlPath ->
                    async { dir to sqlFileAnalyzer.analyze(sqlPath) }
                }.awaitAll()
            }.await()
        }
    }

    private fun tableNameToDirMap(
        dirName: String,
        tableInfoList: List<TableInfo>,
        initial: Map<TableName, Set<String>>
    ): Map<TableName, Set<String>> =
        tableInfoList.fold(initial) { acc, (tableName, _) ->
            val newValue = acc.getOrElse(tableName) { emptySet() } + dirName
            acc + (tableName to newValue)
        }
}

@Suppress("SpreadOperator")
fun main(args: Array<String>): Unit = exitProcess(CommandLine(App()).execute(*args))
