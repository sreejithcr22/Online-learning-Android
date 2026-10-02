package com.codit.interview.aptitude.presentation.favourites

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material.icons.rounded.Cancel
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.codit.interview.aptitude.domain.model.AttemptStatus
import com.codit.interview.aptitude.domain.model.Question
import com.codit.interview.aptitude.presentation.components.AptitudeCard
import com.codit.interview.aptitude.presentation.components.EmptyState
import com.codit.interview.aptitude.presentation.components.LoadingState
import com.codit.interview.aptitude.presentation.theme.Spacing
import com.codit.interview.aptitude.presentation.theme.statusColors

@Composable
fun FavouritesRoute(
    onOpenFavourites: (FavouriteTab) -> Unit,
    viewModel: FavouritesViewModel = hiltViewModel(),
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    if (state.isLoading) {
        LoadingState()
        return
    }

    FavouritesScreen(
        state = state,
        onTabSelected = viewModel::onTabSelected,
        onOpenQuestions = { onOpenFavourites(state.activeTab ?: FavouritesViewModel.TABS.first()) },
    )
}

@Composable
fun FavouritesScreen(
    state: FavouritesUiState,
    onTabSelected: (Int) -> Unit,
    onOpenQuestions: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier.fillMaxSize()) {
        TabRow(selectedTabIndex = state.selectedTab) {
            state.tabs.forEachIndexed { index, tab ->
                Tab(
                    selected = index == state.selectedTab,
                    onClick = { onTabSelected(index) },
                    text = { Text(tab.title) },
                )
            }
        }

        if (state.questions.isEmpty()) {
            EmptyState("No favourites yet. Tap the heart on a question to save it here.")
            return@Column
        }

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(Spacing.large),
            verticalArrangement = Arrangement.spacedBy(Spacing.medium),
        ) {
            items(state.questions, key = { it.number }) { question ->
                FavouriteRow(question = question, onClick = onOpenQuestions)
            }
        }
    }
}

@Composable
private fun FavouriteRow(question: Question, onClick: () -> Unit) {
    val status = MaterialTheme.statusColors
    AptitudeCard(onClick = onClick) {
        Row(verticalAlignment = Alignment.Top) {
            Icon(
                imageVector = when (question.attemptStatus) {
                    AttemptStatus.CORRECT -> Icons.Rounded.CheckCircle
                    AttemptStatus.WRONG -> Icons.Rounded.Cancel
                    AttemptStatus.NOT_ATTEMPTED -> Icons.Rounded.Cancel
                },
                contentDescription = null,
                tint = when (question.attemptStatus) {
                    AttemptStatus.CORRECT -> status.correct
                    else -> status.notAttempted
                },
                modifier = Modifier.padding(end = Spacing.medium),
            )
            Column {
                Text(
                    text = question.text,
                    style = MaterialTheme.typography.bodyLarge,
                    maxLines = 3,
                )
                Text(
                    text = "Q. ${question.number}",
                    style = MaterialTheme.typography.labelSmall,
                    modifier = Modifier.padding(top = Spacing.tiny),
                )
            }
        }
    }
}
