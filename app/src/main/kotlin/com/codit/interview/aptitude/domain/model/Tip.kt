package com.codit.interview.aptitude.domain.model

/** An interview tip, formula or GK fact — all share the same `interview.db` shape. */
data class Tip(
    val number: Int,
    val title: String,
    val body: String,
    val isFavourite: Boolean,
)

/** One of the twelve interview sections shown as horizontally scrollable tabs. */
enum class InterviewTopic(val displayName: String, val table: String) {
    HR("HR", "general"),
    DATA_STRUCTURE("Data Structure", "data"),
    JAVA("Java", "java"),
    C_CPP("C/C++", "cpp"),
    C_SHARP("C#", "chash"),
    PYTHON("Python", "python"),
    PHP("PHP", "php"),
    JAVASCRIPT("Java Script", "js"),
    SOFTWARE_TESTING("Software Testing", "testing"),
    NETWORK("Network", "network"),
    DBMS("DBMS", "sql"),
    OS("OS", "os"),
    ;

    companion object {
        const val FAVOURITES_TABLE = "fav"
        const val GK_INFO_TABLE = "gk_info"
    }
}

/** The three formula/concept sections. */
enum class FormulaTopic(val displayName: String, val table: String) {
    QUANTITATIVE("Quantitative", "quant_formula"),
    REASONING("Reasoning", "reasoning_formula"),
    VERBAL_ABILITY("Verbal Ability", "verbal_formula"),
    ;

    companion object {
        const val FAVOURITES_TABLE = "concept_fav"
    }
}
