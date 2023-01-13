package net.yewton.sqlmiru

class RegexSelectedTablesNamesCollector : RegexTablesNamesCollector() {

    companion object {
        private val selectPattern: Regex = Regex(
            """(?:from|join)${RegexPatterns.singleTableNamePattern}""",
            RegexPatterns.tablesNamesRegexOptions
        )
    }

    override fun doCollect(body: String): List<String> =
        selectPattern.findAll(body).map { it.groupValues[1] }.toList()
}
