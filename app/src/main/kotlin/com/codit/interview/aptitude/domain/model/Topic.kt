package com.codit.interview.aptitude.domain.model

/** The four top-level content sections the app is organised around. */
enum class ContentSection(val displayName: String) {
    QUANTITATIVE("Quantitative Aptitude"),
    LOGICAL("Logical Reasoning"),
    VERBAL("Verbal Ability"),
    GENERAL_KNOWLEDGE("General Knowledge"),
}

/**
 * A single practice topic.
 *
 * The legacy app carried the display-name to table-name mapping as a 40-branch
 * `switch` in every activity. It is now a single value object, so a topic is the
 * only thing a screen has to know about.
 *
 * @param table          backing table inside `master.db`
 * @param favouriteTable table the row is copied into when favourited
 * @param isMock         mock tests are not backed by a topic but by `mockN`
 */
enum class Topic(
    val displayName: String,
    val table: String,
    val section: ContentSection,
    val isInfoOnly: Boolean = false,
) {
    // ---- Quantitative ----
    TIME_AND_WORK("Time And Work", "time_and_work", ContentSection.QUANTITATIVE),
    PROFIT_AND_LOSS("Profit And Loss", "profit", ContentSection.QUANTITATIVE),
    PROBLEMS_ON_AGE("Problems On Age", "age", ContentSection.QUANTITATIVE),
    AVERAGE("Average", "avg", ContentSection.QUANTITATIVE),
    PERMUTATION_AND_COMBINATION("Permutation And Combination", "permutation", ContentSection.QUANTITATIVE),
    PARTNERSHIP("Partnership", "partnership", ContentSection.QUANTITATIVE),
    HCF_AND_LCM("HCF And LCM", "hcf_and_lcm", ContentSection.QUANTITATIVE),
    TIME_AND_DISTANCE("Time And Distance", "time_and_distance", ContentSection.QUANTITATIVE),
    INTEREST("Interest", "interest", ContentSection.QUANTITATIVE),
    PROBLEMS_ON_TRAINS("Problems On Trains", "train", ContentSection.QUANTITATIVE),
    NUMBERS_AND_DECIMAL_FRACTIONS("Numbers And Decimal Fractions", "numbers", ContentSection.QUANTITATIVE),
    RATIO_AND_PROPORTION("Ratio And Proportion", "ratio", ContentSection.QUANTITATIVE),
    BOATS_AND_STREAMS("Boats And Streams", "boats", ContentSection.QUANTITATIVE),
    ALLIGATION_OR_MIXTURE("Alligation or Mixture", "alligation", ContentSection.QUANTITATIVE),
    PIPES_AND_CISTERN("Pipes and Cistern", "pipes", ContentSection.QUANTITATIVE),
    PERCENTAGE("Percentage", "percentage", ContentSection.QUANTITATIVE),

    // ---- Logical reasoning ----
    CALENDAR_AND_CLOCK("Calendar And Clock", "calendar", ContentSection.LOGICAL),
    BLOOD_RELATION("Blood Relation", "blood", ContentSection.LOGICAL),
    SEATING_ARRANGEMENT("Seating Arrangement", "seat", ContentSection.LOGICAL),
    CODING_AND_DECODING("Coding and Decoding", "decode", ContentSection.LOGICAL),
    DIRECTION_SENSE("Direction Sense", "direction", ContentSection.LOGICAL),
    ANALOGY("Analogy", "analogy", ContentSection.LOGICAL),
    CLASSIFICATION("Classification", "classification", ContentSection.LOGICAL),
    ARITHMETIC_REASONING("Arithmetic Reasoning", "arithmetic", ContentSection.LOGICAL),
    SERIES("Series", "series", ContentSection.LOGICAL),
    ESSENTIAL_PART("Essential Part", "essential", ContentSection.LOGICAL),
    VERIFICATION_OF_TRUTH("Verification of Truth", "verification", ContentSection.LOGICAL),
    STATEMENTS_AND_CONCLUSIONS("Statements and Conclusions", "statement_conclusion", ContentSection.LOGICAL),
    STATEMENTS_AND_ARGUMENTS("Statements and Arguments", "statement_arguement", ContentSection.LOGICAL),
    STATEMENTS_AND_ASSUMPTIONS("Statements and Assumptions", "statement_assumptions", ContentSection.LOGICAL),

    // ---- Verbal ability ----
    ANTONYMS("Antonyms", "antonyms", ContentSection.VERBAL),
    IDIOMS_AND_PHRASES("Idioms and Phrases", "idioms", ContentSection.VERBAL),
    PREPOSITIONS("Prepositions", "preposition", ContentSection.VERBAL),
    SYNONYMS("Synonyms", "synonyms", ContentSection.VERBAL),
    SPELLING("Spelling", "spelling", ContentSection.VERBAL),
    ONE_WORD_SUBSTITUTION("One Word Substitution", "substitute", ContentSection.VERBAL),
    CLOSET_TEST("Closet Test", "closet", ContentSection.VERBAL),
    PARAGRAPH_FORMATION("Paragraph Formation", "pharagraph", ContentSection.VERBAL),
    SENTENCE_ARRANGEMENT("Sentence Arrangement", "sentence_arrange", ContentSection.VERBAL),
    SENTENCE_COMPLETION("Sentence Completion", "sentence_complete", ContentSection.VERBAL),

    // ---- General knowledge ----
    INDIAN_POLITICS("Indian Politics", "politics", ContentSection.GENERAL_KNOWLEDGE),
    INDIAN_GEOGRAPHY("Indian Geography", "indian_geo", ContentSection.GENERAL_KNOWLEDGE),
    BOOKS_AND_AUTHORS("Books And Authors", "", ContentSection.GENERAL_KNOWLEDGE, isInfoOnly = true),
    DAYS_AND_DATES("Days And Dates", "", ContentSection.GENERAL_KNOWLEDGE, isInfoOnly = true),
    AWARDS_AND_HONORS("Awards And Honors", "awards", ContentSection.GENERAL_KNOWLEDGE),
    INDIA_AND_WORLD_HISTORY("India And World History", "indian_history", ContentSection.GENERAL_KNOWLEDGE),
    WORLD_GEOGRAPHY("World Geography", "world_geo", ContentSection.GENERAL_KNOWLEDGE),
    BASIC_GENERAL_KNOWLEDGE("Basic General Knowledge", "basic_gk", ContentSection.GENERAL_KNOWLEDGE),
    COMPUTER_AWARENESS("Computer Awareness", "computer", ContentSection.GENERAL_KNOWLEDGE),
    INVENTIONS_AND_DISCOVERIES("Inventions And Discoveries", "inventions", ContentSection.GENERAL_KNOWLEDGE),
    GENERAL_SCIENCE("General Science", "general_science", ContentSection.GENERAL_KNOWLEDGE),
    SPORTS("Sports", "sports", ContentSection.GENERAL_KNOWLEDGE),
    FAMOUS_PERSONALITIES("Famous Personalities", "famous", ContentSection.GENERAL_KNOWLEDGE),
    ;

    val isGeneralKnowledge: Boolean get() = section == ContentSection.GENERAL_KNOWLEDGE

    companion object {
        fun byDisplayName(name: String): Topic? = entries.firstOrNull { it.displayName == name }

        val quantitative: List<Topic> = entries.filter { it.section == ContentSection.QUANTITATIVE }
        val logical: List<Topic> = entries.filter { it.section == ContentSection.LOGICAL }
        val verbal: List<Topic> = entries.filter { it.section == ContentSection.VERBAL }
        val generalKnowledge: List<Topic> = entries.filter { it.isGeneralKnowledge }

        /** Formula tables are keyed by the parent section of the current practice session. */
        fun formulaTable(section: ContentSection): String = when (section) {
            ContentSection.QUANTITATIVE -> "quant_formula"
            ContentSection.LOGICAL -> "reasoning_formula"
            ContentSection.VERBAL -> "verbal_formula"
            ContentSection.GENERAL_KNOWLEDGE -> "quant_formula"
        }
    }
}
