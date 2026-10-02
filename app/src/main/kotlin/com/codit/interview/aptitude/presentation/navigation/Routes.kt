package com.codit.interview.aptitude.presentation.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Quiz
import androidx.compose.material.icons.outlined.School
import androidx.compose.material.icons.outlined.SquareFoot
import androidx.compose.material.icons.outlined.Timer
import androidx.compose.material.icons.rounded.Favorite
import androidx.compose.material.icons.rounded.Quiz
import androidx.compose.material.icons.rounded.School
import androidx.compose.material.icons.rounded.SquareFoot
import androidx.compose.material.icons.rounded.Timer
import androidx.compose.ui.graphics.vector.ImageVector
import com.codit.interview.aptitude.domain.model.ContentSection
import com.codit.interview.aptitude.domain.model.Topic
import com.codit.interview.aptitude.presentation.tips.TipKind

/**
 * Every destination in the single-`Activity` Compose navigation graph.
 *
 * The legacy app declared eleven `Activity` classes plus a `ViewPager` per screen, and
 * passed state between them through mutable `static` fields on `APPSTATE`. Passing
 * route arguments explicitly is what makes those transitions testable and reversible.
 */
object Routes {

    const val ANALYTICS = "analytics"
    const val SETTINGS = "settings"
    const val ABOUT = "about"

    /** `kind` selects which tab list the tips screen shows. */
    const val INTERVIEW = "interview?kind={kind}"
    const val FORMULAS = "formulas?kind={kind}"
    const val FAVOURITES = "favourites?tab={tab}"

    const val QUESTIONS = "questions"
    const val MOCK_TESTS = "mock"

    const val TOPIC_LIST = "topics/{section}"
    const val PRACTICE = "practice?topic={topic}&section={section}&seconds={seconds}&mode={mode}"
    const val GK_INFO = "gk-info?topic={topic}"
    const val MOCK_TEST = "mock-test?title={title}&table={table}&seconds={seconds}"
    const val REPORT =
        "report?title={title}&correct={correct}&wrong={wrong}&skipped={skipped}" +
            "&total={total}&showTime={showTime}&times={times}"

    fun topicList(section: ContentSection) = "topics/${section.name}"

    fun practice(topic: Topic, seconds: Int, mode: PracticeMode) =
        "practice?topic=${topic.name}&section=${topic.section.name}&seconds=$seconds&mode=${mode.name}"

    /**
     * Practice over the favourites table of [section].
     *
     * Reuses the practice route because the favourites table has the same schema as any
     * question table; only the [com.codit.interview.aptitude.domain.model.QuestionSource]
     * differs, so no topic argument is needed.
     */
    fun favourites(section: ContentSection) =
        "practice?topic=&section=${section.name}&seconds=0&mode=${PracticeMode.FAVOURITES.name}"

    fun gkInfo(topic: Topic) = "gk-info?topic=${topic.name}"

    fun mockTest(title: String, table: String, seconds: Int) =
        "mock-test?title=$title&table=$table&seconds=$seconds"

    fun report(report: ReportArgs) = report.toRoute()
}

/** Which practice session is running. */
enum class PracticeMode {
    /** Timed, per-question timer, notes + calculator enabled, progress is tracked. */
    PRACTICE,

    /** GK: no timer, but favourites and explanations work. */
    GENERAL_KNOWLEDGE,

    /** A read-only pass over the favourites list. */
    FAVOURITES,
}

/**
 * Bottom-navigation destinations.
 *
 * [route] is the pattern registered in the graph (used to match the current
 * destination); [navRoute] is the concrete route to navigate to.
 */
enum class TopLevelDestination(
    val route: String,
    val navRoute: String,
    val label: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
) {
    QUESTIONS(Routes.QUESTIONS, Routes.QUESTIONS, "Questions", Icons.Rounded.Quiz, Icons.Outlined.Quiz),
    INTERVIEW(
        Routes.INTERVIEW,
        "interview?kind=${TipKind.INTERVIEW.routeValue}",
        "Interview",
        Icons.Rounded.School,
        Icons.Outlined.School,
    ),
    FORMULAS(
        Routes.FORMULAS,
        "formulas?kind=${TipKind.FORMULAS.routeValue}",
        "Formulas",
        Icons.Rounded.SquareFoot,
        Icons.Outlined.SquareFoot,
    ),
    MOCK(Routes.MOCK_TESTS, Routes.MOCK_TESTS, "Mock", Icons.Rounded.Timer, Icons.Outlined.Timer),
    FAVOURITES(
        Routes.FAVOURITES,
        "favourites?tab=",
        "Saved",
        Icons.Rounded.Favorite,
        Icons.Outlined.FavoriteBorder,
    ),
}

/** Navigation arguments for the report screen, encoded in a single route string. */
data class ReportArgs(
    val title: String,
    val correct: Int,
    val wrong: Int,
    val notAttempted: Int,
    val total: Int,
    val showTime: Boolean,
    val timePerQuestion: List<Int> = emptyList(),
) {
    fun toRoute(): String = buildString {
        append("report")
        append("?title=").append(title.encode())
        append("&correct=").append(correct)
        append("&wrong=").append(wrong)
        append("&skipped=").append(notAttempted)
        append("&total=").append(total)
        append("&showTime=").append(showTime)
        append("&times=").append(timePerQuestion.joinToString(","))
    }

    companion object {
        fun parse(
            title: String,
            correct: Int,
            wrong: Int,
            skipped: Int,
            total: Int,
            showTime: Boolean,
            times: String,
        ) = ReportArgs(
            title = title.decode(),
            correct = correct,
            wrong = wrong,
            notAttempted = skipped,
            total = total,
            showTime = showTime,
            timePerQuestion = times.split(",").mapNotNull { it.toIntOrNull() },
        )
    }
}

private fun String.encode(): String = java.net.URLEncoder.encode(this, "UTF-8")

private fun String.decode(): String = java.net.URLDecoder.decode(this, "UTF-8")
