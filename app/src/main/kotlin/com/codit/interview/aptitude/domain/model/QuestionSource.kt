package com.codit.interview.aptitude.domain.model

/**
 * Where a practice session reads its questions from.
 *
 * The legacy app carried a bare table-name string in the mutable static
 * `APPSTATE.CURRENT_TABLE` and branched on `currentFragment` string constants in
 * every fragment. A sealed type makes the three cases — a topic, a mock test and the
 * favourites list — explicit at the type level, so a favourites session can never be
 * handed a mock-test table by accident.
 */
sealed interface QuestionSource {

    val table: String
    val favouriteTable: String
    val section: ContentSection

    /** Title shown in the toolbar and on the report screen. */
    val displayTitle: String

    /** Stable key used for "resume where I left off". */
    val key: String

    /** A regular practice topic. */
    data class OfTopic(val topic: Topic) : QuestionSource {
        override val table: String get() = topic.table
        override val favouriteTable: String get() = topic.favouriteTable
        override val section: ContentSection get() = topic.section
        override val displayTitle: String get() = topic.displayName
        override val key: String get() = "topic:${topic.name}"
    }

    /** A timed mock test. */
    data class OfMockTest(val mockTest: MockTest) : QuestionSource {
        override val table: String get() = mockTest.table
        override val favouriteTable: String get() = "fav_table"
        override val section: ContentSection get() = ContentSection.QUANTITATIVE
        override val displayTitle: String get() = mockTest.title
        override val key: String get() = "mock:${mockTest.index}"
    }

    /**
     * The favourites list of a section.
     *
     * GK and aptitude questions are copied into separate tables, so this maps onto a
     * single read-only table. Writes go to that table too, so favouriting and
     * answering a saved question behave normally.
     */
    data class OfFavourites(override val section: ContentSection) : QuestionSource {
        override val table: String
            get() = if (section == ContentSection.GENERAL_KNOWLEDGE) GK_FAVOURITES else APTITUDE_FAVOURITES

        override val favouriteTable: String get() = table
        override val displayTitle: String get() = "Favourites"
        override val key: String get() = "favourites:${section.name}"
    }

    companion object {
        const val GK_FAVOURITES = "gk_fav_table"
        const val APTITUDE_FAVOURITES = "fav_table"
    }
}

val Topic.favouriteTable: String
    get() = if (isGeneralKnowledge) QuestionSource.GK_FAVOURITES else QuestionSource.APTITUDE_FAVOURITES
