package net.yewton.sqlmiru

import java.nio.file.Path

data class SqlFileAnalyzeResult(
    val filePath: Path,
    val mutatingTableInfoList: List<TableInfo>,
    val allTableNames: List<String>
)
