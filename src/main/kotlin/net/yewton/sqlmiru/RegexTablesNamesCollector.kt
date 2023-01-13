package net.yewton.sqlmiru

abstract class RegexTablesNamesCollector : TablesNamesCollector {
    override fun collect(body: String): List<String> = doCollect(
        body.replace(Regex("--.*"), "")
            .replace(Regex("""/\*.*?\*/""", RegexOption.DOT_MATCHES_ALL), " ")
    )

    protected abstract fun doCollect(body: String): List<String>
}
