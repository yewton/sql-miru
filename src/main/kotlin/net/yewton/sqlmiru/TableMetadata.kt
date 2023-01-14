package net.yewton.sqlmiru

import java.net.URL

data class TableMetadata(
    val physicalName: String,
    val logicalName: String?,
    val referenceURL: URL?,
    val group: String?
)
