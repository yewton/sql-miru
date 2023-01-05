package net.yewton.sqlmiru

import picocli.CommandLine
import picocli.CommandLine.Command
import picocli.CommandLine.Parameters
import java.nio.file.Path
import java.util.concurrent.Callable
import kotlin.io.path.isDirectory
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
        if ( paths.isEmpty() || nonDirectoryPaths.isNotEmpty() ) {
            System.err.println("ディレクトリを指定してね")
            nonDirectoryPaths.forEach { System.err.println("$it はディレクトリじゃないよ") }
            commandSpec.commandLine().usage(System.err)
            return 1;
        }
        println("以下のディレクトリが指定されたよ:")
        paths.forEach {
            println("${it.fileName}")
        }

        return 0
    }
}

fun main(args: Array<String>): Unit = exitProcess(CommandLine(App()).execute(*args))
