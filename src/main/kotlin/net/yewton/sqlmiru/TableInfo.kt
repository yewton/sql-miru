package net.yewton.sqlmiru

@JvmInline
value class TableName(val value: String)

data class TableInfo(
    val tableName: TableName,
    val collectedBy: Set<String>
)
