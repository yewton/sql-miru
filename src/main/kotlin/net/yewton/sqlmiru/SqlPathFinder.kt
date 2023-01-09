package net.yewton.sqlmiru

import java.nio.file.FileVisitResult
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.SimpleFileVisitor
import java.nio.file.attribute.BasicFileAttributes
import kotlin.io.path.extension

class SqlPathFinder : SimpleFileVisitor<Path>() {

    private val paths = mutableListOf<Path>()

    fun perform(dir: Path): List<Path> {
        paths.clear()
        Files.walkFileTree(dir, this)
        return paths
    }

    override fun visitFile(file: Path, attrs: BasicFileAttributes): FileVisitResult {
        if (file.extension != "sql") {
            return FileVisitResult.CONTINUE
        }
        paths.add(file)
        return super.visitFile(file, attrs)
    }
}
