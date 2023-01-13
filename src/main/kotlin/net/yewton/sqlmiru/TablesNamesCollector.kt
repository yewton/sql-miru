package net.yewton.sqlmiru

interface TablesNamesCollector {
    fun collect(body: String): List<String>
}
