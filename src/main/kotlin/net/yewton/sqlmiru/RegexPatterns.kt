package net.yewton.sqlmiru

object RegexPatterns {
    /**
     * テーブル名らしきものを可能な限り拾うため、最大この数値分、
     * ステートメント直後だけでなくその先に続く予約語でない語にマッチさせる。
     * エイリアスにもマッチしてししまうが、漏れるよりマシ、という方針。
     */
    private const val MAX_CONSECUTIVE_TABLES = 10
    val singleTableNamePattern: String
    val multipleTableNamesPattern: String
    val tablesNamesRegexOptions: Set<RegexOption> = setOf(
        RegexOption.IGNORE_CASE,
        RegexOption.DOT_MATCHES_ALL
    )

    init {
        val reservedWordsPattern = listOf(
            "into", "from", "set", "all", "wait", "nowait", "of", "dual",
            "when", "values", "using", "limit", "is", "null", "union",
            "as", "with", "where", "select"
        ).joinToString("|")
        // language=regexp
        singleTableNamePattern = """\s+(?!$reservedWordsPattern)(?:\w+\.)?(\w+)"""
        multipleTableNamesPattern = singleTableNamePattern +
            generateSequence { "(?:$singleTableNamePattern)" }
                .take(MAX_CONSECUTIVE_TABLES - 1).joinToString("?") + "?"
    }
}
