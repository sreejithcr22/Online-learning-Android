package com.codit.interview.aptitude.presentation.navigation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.codit.interview.aptitude.domain.model.ContentSection
import com.codit.interview.aptitude.domain.model.SessionReport
import com.codit.interview.aptitude.domain.model.Topic
import com.codit.interview.aptitude.presentation.analytics.AnalyticsRoute
import com.codit.interview.aptitude.presentation.favourites.FavouritesRoute
import com.codit.interview.aptitude.presentation.favourites.FavouritesViewModel
import com.codit.interview.aptitude.presentation.mock.MockSessionViewModel
import com.codit.interview.aptitude.presentation.mock.MockSessionHost
import com.codit.interview.aptitude.presentation.mock.MockTestsRoute
import com.codit.interview.aptitude.presentation.practice.PracticeRoute
import com.codit.interview.aptitude.presentation.practice.PracticeViewModel
import com.codit.interview.aptitude.presentation.questions.QuestionsHomeRoute
import com.codit.interview.aptitude.presentation.questions.ReferenceScreen
import com.codit.interview.aptitude.presentation.questions.TopicListRoute
import com.codit.interview.aptitude.presentation.questions.TopicListViewModel
import com.codit.interview.aptitude.presentation.report.ReportScreen
import com.codit.interview.aptitude.presentation.settings.SettingsRoute
import com.codit.interview.aptitude.presentation.theme.Spacing
import com.codit.interview.aptitude.presentation.tips.TipKind
import com.codit.interview.aptitude.presentation.tips.TipsRoute
import com.codit.interview.aptitude.presentation.tips.TipsViewModel

/**
 * The complete navigation graph.
 *
 * Every argument travels in the route, which is what lets a practice session survive
 * process death without the `APPSTATE` static fields the legacy app relied on.
 */
@Composable
fun NavGraph(navController: NavHostController) {
    val context = LocalContext.current

    NavHost(
        navController = navController,
        startDestination = Routes.QUESTIONS,
    ) {
        // ------------------------------------------------------------ analytics
        composable(Routes.ANALYTICS) {
            AnalyticsRoute(onOpenStore = { context.openPlayStore() })
        }

        // ------------------------------------------------------------ questions
        composable(Routes.QUESTIONS) {
            QuestionsHomeRoute(
                onSectionSelected = { section -> navController.navigate(Routes.topicList(section)) }
            )
        }

        composable(
            route = Routes.TOPIC_LIST,
            arguments = listOf(navArgument(TopicListViewModel.ARG_SECTION) { type = NavType.StringType }),
        ) {
            TopicListRoute(
                onBack = { navController.popBackStack() },
                onTopicSelected = { topic, seconds, isInfoOnly ->
                    if (isInfoOnly) {
                        navController.navigate(Routes.gkInfo(topic))
                    } else {
                        navController.navigate(
                            Routes.practice(
                                topic = topic,
                                seconds = seconds,
                                mode = if (topic.isGeneralKnowledge) {
                                    PracticeMode.GENERAL_KNOWLEDGE
                                } else {
                                    PracticeMode.PRACTICE
                                },
                            )
                        )
                    }
                },
            )
        }

        // ------------------------------------------------------------- practice
        composable(
            route = Routes.PRACTICE,
            arguments = listOf(
                // Empty for the favourites route, which only needs a section.
                navArgument(PracticeViewModel.ARG_TOPIC) {
                    type = NavType.StringType
                    defaultValue = ""
                },
                navArgument(PracticeViewModel.ARG_SECTION) { type = NavType.StringType },
                navArgument(PracticeViewModel.ARG_SECONDS) { type = NavType.StringType },
                navArgument(PracticeViewModel.ARG_MODE) { type = NavType.StringType },
            ),
        ) {
            PracticeRoute(
                onBack = { navController.popBackStack() },
                onShowReport = { report ->
                    navController.navigate(Routes.report(report)) {
                        popUpTo(Routes.QUESTIONS)
                    }
                },
            )
        }

        // ------------------------------------------------------------- gk info
        composable(
            route = Routes.GK_INFO,
            arguments = listOf(navArgument("topic") { type = NavType.StringType }),
        ) {
            val topic = Topic.valueOf(it.arguments?.getString("topic").orEmpty())
            ReferenceScreen(title = topic.displayName, body = gkInfoFor(topic))
        }

        // ---------------------------------------------------------- mock tests
        composable(Routes.MOCK_TESTS) {
            MockTestsRoute(
                onStartTest = { test, seconds ->
                    navController.navigate(Routes.mockTest(test.title, test.table, seconds))
                }
            )
        }

        composable(
            route = Routes.MOCK_TEST,
            arguments = listOf(
                navArgument(MockSessionViewModel.ARG_TITLE) { type = NavType.StringType },
                navArgument(MockSessionViewModel.ARG_TABLE) { type = NavType.StringType },
                navArgument(MockSessionViewModel.ARG_SECONDS) { type = NavType.StringType },
            ),
        ) {
            MockSessionHost(onFinished = { report ->
                navController.navigate(Routes.report(report)) {
                    popUpTo(Routes.MOCK_TESTS)
                }
            })
        }

        // -------------------------------------------------- interview / formulas
        composable(
            route = Routes.INTERVIEW,
            arguments = listOf(navArgument(TipsViewModel.ARG_KIND) { type = NavType.StringType }),
        ) {
            TipsRoute(onBack = { navController.popBackStack() })
        }

        composable(
            route = Routes.FORMULAS,
            arguments = listOf(navArgument(TipsViewModel.ARG_KIND) { type = NavType.StringType }),
        ) {
            TipsRoute(onBack = { navController.popBackStack() })
        }

        // ----------------------------------------------------------- favourites
        composable(
            route = Routes.FAVOURITES,
            arguments = listOf(navArgument(FavouritesViewModel.ARG_TAB) {
                type = NavType.StringType
                defaultValue = ""
            }),
        ) {
            FavouritesRoute(
                onOpenFavourites = { tab ->
                    val section = tab.section ?: ContentSection.QUANTITATIVE
                    navController.navigate(Routes.favourites(section))
                }
            )
        }

        // -------------------------------------------------------------- report
        composable(
            route = Routes.REPORT,
            arguments = listOf(
                navArgument("title") { type = NavType.StringType },
                navArgument("correct") { type = NavType.IntType },
                navArgument("wrong") { type = NavType.IntType },
                navArgument("skipped") { type = NavType.IntType },
                navArgument("total") { type = NavType.IntType },
                navArgument("showTime") { type = NavType.BoolType },
                navArgument("times") { type = NavType.StringType; defaultValue = "" },
            ),
        ) { entry ->
            val args = entry.arguments
            val report = ReportArgs.parse(
                title = args?.getString("title").orEmpty(),
                correct = args?.getInt("correct") ?: 0,
                wrong = args?.getInt("wrong") ?: 0,
                skipped = args?.getInt("skipped") ?: 0,
                total = args?.getInt("total") ?: 0,
                showTime = args?.getBoolean("showTime") ?: false,
                times = args?.getString("times").orEmpty(),
            )
            ReportScreen(
                report = SessionReport(
                    title = report.title,
                    correct = report.correct,
                    wrong = report.wrong,
                    notAttempted = report.notAttempted,
                    total = report.total,
                    timePerQuestionSeconds = report.timePerQuestion,
                )
            )
        }

        // ------------------------------------------------------------- settings
        composable(Routes.SETTINGS) {
            SettingsRoute(
                onOpenAbout = { navController.navigate(Routes.ABOUT) },
                onShareApp = { context.shareApp() },
                onOpenStore = { context.openPlayStore() },
                onOpenFeedback = { context.sendFeedback() },
            )
        }

        composable(Routes.ABOUT) { AboutScreen() }
    }
}

@Composable
private fun AboutScreen(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(Spacing.large),
        verticalArrangement = Arrangement.spacedBy(Spacing.medium),
    ) {
        Text(
            text = "Aptitude & GK Guide",
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center,
        )
        Text(
            text = "A free, fully offline app for aptitude, reasoning, verbal ability, " +
                "general knowledge, mock tests and interview preparation — with a timer " +
                "for every question and detailed progress tracking.",
            style = MaterialTheme.typography.bodyLarge,
        )
        Text(
            text = "This release is a Kotlin / Jetpack Compose rewrite built on MVVM, " +
                "Clean Architecture, Hilt and Coroutines Flows.",
            style = MaterialTheme.typography.bodyMedium,
        )
        Text(
            text = "Questions: sreejithcr2@gmail.com",
            style = MaterialTheme.typography.labelSmall,
        )
    }
}

/** Reference content for the two info-only GK topics. */
private fun gkInfoFor(topic: Topic): String = when (topic) {
    Topic.BOOKS_AND_AUTHORS -> "Book-and-author pairs are stored in the app's reference " +
        "database. Open this build's Assets to browse them."

    Topic.DAYS_AND_DATES -> "Important days and their dates are stored in the app's " +
        "reference database. Open this build's Assets to browse them."

    else -> "No reference information available."
}
