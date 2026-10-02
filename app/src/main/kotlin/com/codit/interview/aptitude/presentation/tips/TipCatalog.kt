package com.codit.interview.aptitude.presentation.tips

import com.codit.interview.aptitude.domain.model.FormulaTopic
import com.codit.interview.aptitude.domain.model.InterviewTopic

/** The three families of "list of tips" screens. */
enum class TipKind(val routeValue: String) {
    INTERVIEW("interview"),
    FORMULAS("formulas"),
    FAVOURITES("favourites"),
    ;

    companion object {
        fun from(value: String): TipKind =
            entries.firstOrNull { it.routeValue == value } ?: INTERVIEW
    }
}

/**
 * Tab layouts for the tips screens.
 *
 * Each entry lists the source table and where its favourites are copied to, so the
 * favourites tab is just another tab reading a different table.
 */
object TipCatalog {

    fun forKind(kind: String): List<TipTab> = when (TipKind.from(kind)) {
        TipKind.INTERVIEW -> InterviewTopic.entries.map {
            TipTab(
                title = it.displayName,
                table = it.table,
                favouritesTable = InterviewTopic.FAVOURITES_TABLE,
            )
        } + TipTab(
            title = "Saved",
            table = InterviewTopic.FAVOURITES_TABLE,
            favouritesTable = InterviewTopic.FAVOURITES_TABLE,
        )

        TipKind.FORMULAS -> FormulaTopic.entries.map {
            TipTab(
                title = it.displayName,
                table = it.table,
                favouritesTable = FormulaTopic.FAVOURITES_TABLE,
            )
        } + TipTab(
            title = "Saved",
            table = FormulaTopic.FAVOURITES_TABLE,
            favouritesTable = FormulaTopic.FAVOURITES_TABLE,
        )

        // The Favourites screen hosts four independent lists: interview tips, GK
        // questions, formulas and aptitude questions. The two question lists are not
        // "tips", so they are handled by the practice screen; this catalog covers the
        // two tip-based lists.
        TipKind.FAVOURITES -> listOf(
            TipTab(
                title = "Interview",
                table = InterviewTopic.FAVOURITES_TABLE,
                favouritesTable = InterviewTopic.FAVOURITES_TABLE,
            ),
            TipTab(
                title = "Formulas",
                table = FormulaTopic.FAVOURITES_TABLE,
                favouritesTable = FormulaTopic.FAVOURITES_TABLE,
            ),
        )
    }
}
