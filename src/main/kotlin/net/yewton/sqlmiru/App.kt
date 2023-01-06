package net.yewton.sqlmiru

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import picocli.CommandLine
import picocli.CommandLine.Command
import picocli.CommandLine.Parameters
import java.nio.file.Files
import java.nio.file.Path
import java.util.concurrent.Callable
import kotlin.io.path.isDirectory
import kotlin.io.path.name
import kotlin.system.exitProcess

@Command(name = "sql-miru", mixinStandardHelpOptions = true,
        description = ["TBW"])
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
            return 1;
        }
        val fileVisitors: List<FileVisitor>
        runBlocking {
            withContext(Dispatchers.IO) {
                fileVisitors = paths.map {
                    async {
                        FileVisitor(it.name).apply {
                            println("${it.name}: start")
                            Files.walkFileTree(it, this)
                            println("${it.name}: done")
                        }
                    }
                }.awaitAll()
            }
        }

        val mutatingTableToModulesMap = mutableMapOf<String, Set<String>>()
        val tableToModulesMap = mutableMapOf<String, Set<String>>()
        fileVisitors.forEach { fileVisitor ->
            fileVisitor.mutatingTableInfoList.forEach {
                mutatingTableToModulesMap.merge(it.tableName, setOf(it.moduleName), Set<String>::plus)
            }
            fileVisitor.tableNames.forEach {
                tableToModulesMap.merge(it, setOf(fileVisitor.moduleName), Set<String>::plus)
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
}

fun main(args: Array<String>): Unit = exitProcess(CommandLine(App()).execute(*args))
