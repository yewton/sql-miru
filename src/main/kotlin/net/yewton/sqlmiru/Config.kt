package net.yewton.sqlmiru

data class Config(val tablesMetadata: List<TableMetadata>) {
    fun findTableMetadata(physicalName: String) = tablesMetadata.find {
        it.physicalName.lowercase() == physicalName.lowercase()
    }
}
