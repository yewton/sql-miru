package net.yewton.sqlmiru

class RegexMutatedTablesNamesCollector : RegexTablesNamesCollector() {

    companion object {
        private val mutatePattern: Regex = Regex(
            """(?:update|insert|delete|merge|upsert|into)""" +
                RegexPatterns.multipleTableNamesPattern,
            RegexPatterns.tablesNamesRegexOptions
        )
    }

    override fun doCollect(body: String): List<String> =
        mutatePattern.findAll(body).flatMap {
            it.groupValues.drop(1).filterNot(String::isBlank)
        }.toList()
}
