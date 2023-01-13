package net.yewton.sqlmiru

import java.nio.file.Path

data class SqlFileAnalyzeResult(
    val filePath: Path,
    val mutatedTableInfoList: List<TableInfo>,
    val anyTableInfoList: List<TableInfo>
)
