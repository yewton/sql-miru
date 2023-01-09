package net.yewton.sqlmiru

import java.nio.file.Path

data class TableInfo(
    val sqlFilePath: Path,
    val statement: String,
    val tableName: String,
    val isInferred: Boolean = false
)
