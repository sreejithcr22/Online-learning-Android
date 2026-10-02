package com.codit.interview.aptitude.presentation.navigation

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Info
import androidx.compose.material.icons.rounded.QueryStats
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavDestination
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.codit.interview.aptitude.domain.model.ContentSection
import com.codit.interview.aptitude.domain.model.Topic

/**
 * App shell.
 *
 * One `Activity` and one `Scaffold` now host every screen. The legacy app had eleven
 * `Activity` classes each with its own copy of a `DrawerLayout` + `BottomNavigationView`
 * + `TabLayout` and its own "navigate by `startActivity`" transitions.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AptitudeApp(
    onOpenStore: () -> Unit,
    onShareApp: () -> Unit,
    onSendFeedback: () -> Unit,
    navController: NavHostController = rememberNavController(),
) {
    val backStackEntry by navController.currentBackStackEntryAsState()
    val destination = backStackEntry?.destination
    val showBottomBar = TopLevelDestination.entries.any { it.matches(destination) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(titleFor(backStackEntry)) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                    actionIconContentColor = MaterialTheme.colorScheme.onPrimary,
                    navigationIconContentColor = MaterialTheme.colorScheme.onPrimary,
                ),
                navigationIcon = {
                    if (navController.previousBackStackEntry != null) {
                        IconButton(onClick = { navController.popBackStack() }) {
                            Icon(
                                Icons.AutoMirrored.Rounded.ArrowBack,
                                contentDescription = "Navigate up",
                            )
                        }
                    }
                },
                actions = {
                    IconButton(onClick = { navController.navigate(Routes.ANALYTICS) }) {
                        Icon(Icons.Rounded.QueryStats, contentDescription = "Analytics")
                    }
                    IconButton(onClick = { navController.navigate(Routes.SETTINGS) }) {
                        Icon(Icons.Rounded.Settings, contentDescription = "Settings")
                    }
                    IconButton(onClick = { navController.navigate(Routes.ABOUT) }) {
                        Icon(Icons.Rounded.Info, contentDescription = "About")
                    }
                },
            )
        },
        bottomBar = {
            AnimatedVisibility(
                visible = showBottomBar,
                enter = slideInVertically { it },
                exit = slideOutVertically { it },
            ) {
                NavigationBar {
                    TopLevelDestination.entries.forEach { entry ->
                        val selected = entry.matches(destination)
                        NavigationBarItem(
                            selected = selected,
                            onClick = { navController.navigateTopLevel(entry.navRoute) },
                            icon = {
                                Icon(
                                    imageVector = if (selected) {
                                        entry.selectedIcon
                                    } else {
                                        entry.unselectedIcon
                                    },
                                    contentDescription = entry.label,
                                )
                            },
                            label = { Text(entry.label) },
                        )
                    }
                }
            }
        },
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
        ) {
            NavGraph(navController = navController)
        }
    }
}

/**
 * A destination pattern matches when the current back stack entry's route equals it.
 *
 * `NavDestination.route` reports the registered pattern (for example
 * `interview?kind={kind}`), so a plain string comparison is enough here.
 */
private fun TopLevelDestination.matches(destination: NavDestination?): Boolean =
    destination?.route == route

private fun NavHostController.navigateTopLevel(route: String) {
    navigate(route) {
        popUpTo(graph.findStartDestination().id) { saveState = true }
        launchSingleTop = true
        restoreState = true
    }
}

private val titles = listOf(
    Routes.ANALYTICS to "Analytics",
    Routes.QUESTIONS to "Questions",
    Routes.INTERVIEW to "Interview",
    Routes.FORMULAS to "Formulas",
    Routes.MOCK_TESTS to "Mock Tests",
    Routes.FAVOURITES to "Favourites",
    Routes.SETTINGS to "Settings",
    Routes.ABOUT to "About",
)

/**
 * Title for the current destination.
 *
 * Screens that navigate with an argument (a topic, a section, a report) show that
 * argument rather than a generic label, which is why the legacy code needed each
 * activity to call `setTitle` from its own `onCreate`.
 */
private fun titleFor(backStackEntry: NavBackStackEntry?): String {
    if (backStackEntry == null) return "Aptitude & GK Guide"
    val destination = backStackEntry.destination
    val arguments: Bundle = backStackEntry.arguments ?: Bundle()

    return when (destination.route) {
        Routes.TOPIC_LIST -> arguments.sectionName() ?: "Topics"
        Routes.PRACTICE -> arguments.topicName() ?: "Practice"
        Routes.GK_INFO -> arguments.topicName() ?: "Reference"
        Routes.MOCK_TEST -> arguments.getString("title").orEmpty().ifBlank { "Mock Test" }
        Routes.REPORT -> arguments.getString("title")?.let(::decode) ?: "Report"
        else -> titles.firstOrNull { it.first == destination.route }?.second
            ?: "Aptitude & GK Guide"
    }
}

private fun Bundle.sectionName(): String? =
    getString("section")?.let { name ->
        runCatching { ContentSection.valueOf(name).displayName }.getOrNull()
    }

private fun Bundle.topicName(): String? =
    getString("topic")?.takeIf { it.isNotBlank() }?.let { name ->
        runCatching { Topic.valueOf(name).displayName }.getOrNull()
    } ?: sectionName()

private fun decode(value: String): String = java.net.URLDecoder.decode(value, "UTF-8")

// ---------------------------------------------------------------- share helpers

fun Context.openPlayStore() {
    val intent = Intent(
        Intent.ACTION_VIEW,
        Uri.parse("https://play.google.com/store/apps/details?id=$packageName"),
    )
    startSafely(intent)
}

fun Context.shareApp() {
    val intent = Intent(Intent.ACTION_SEND).apply {
        type = "text/plain"
        putExtra(
            Intent.EXTRA_TEXT,
            "Download the free all in one offline guide containing Aptitude, Verbal " +
                "reasoning, Logical reasoning and GK questions and answers along with " +
                "detailed explanations, concepts and formulas plus Technical, HR and " +
                "many more Interview tips and tricks.\n" +
                "https://play.google.com/store/apps/details?id=$packageName",
        )
    }
    startSafely(Intent.createChooser(intent, "Share app"))
}

fun Context.sendFeedback() {
    val intent = Intent(
        Intent.ACTION_SENDTO,
        Uri.fromParts("mailto", FEEDBACK_EMAIL, null),
    ).putExtra(Intent.EXTRA_SUBJECT, "Feedback")
    startSafely(Intent.createChooser(intent, "Send feedback"))
}

fun Context.shareQuestion(questionText: String, options: List<String>) {
    val body = buildString {
        appendLine("Hey, can you answer this question?")
        appendLine()
        appendLine(questionText)
        options.forEachIndexed { index, option -> appendLine("${('A' + index)}) $option") }
        appendLine()
        append("For more solved interview questions and answers, download the free all-in-one ")
        append("offline interview preparation guide: https://play.google.com/store/apps/details?id=$packageName")
    }
    startSafely(
        Intent.createChooser(
            Intent(Intent.ACTION_SEND).apply {
                type = "text/plain"
                putExtra(Intent.EXTRA_TEXT, body)
            },
            "Share question",
        )
    )
}

private fun Context.startSafely(intent: Intent) {
    try {
        startActivity(intent)
    } catch (e: ActivityNotFoundException) {
        android.widget.Toast.makeText(this, "No app found to handle that", android.widget.Toast.LENGTH_SHORT)
            .show()
    }
}

fun Context.shareTip(tipTitle: String, tipBody: String) {
    val intent = Intent(Intent.ACTION_SEND).apply {
        type = "text/plain"
        putExtra(
            Intent.EXTRA_TEXT,
            "$tipTitle\n\n-$tipBody\n\n" +
                "https://play.google.com/store/apps/details?id=$packageName",
        )
    }
    startSafely(Intent.createChooser(intent, "Share tip"))
}

private const val FEEDBACK_EMAIL = "codit.apps@gmail.com"
