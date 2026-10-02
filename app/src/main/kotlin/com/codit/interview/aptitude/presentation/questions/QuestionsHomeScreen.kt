package com.codit.interview.aptitude.presentation.questions

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Timer
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
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
import com.codit.interview.aptitude.domain.model.ContentSection
import com.codit.interview.aptitude.presentation.components.AptitudeCard
import com.codit.interview.aptitude.presentation.components.CircularProgressIndicator
import com.codit.interview.aptitude.presentation.components.LoadingState
import com.codit.interview.aptitude.presentation.theme.Spacing
import com.codit.interview.aptitude.presentation.theme.statusColors

@Composable
fun QuestionsHomeRoute(
    onSectionSelected: (ContentSection) -> Unit,
    viewModel: QuestionsHomeViewModel = hiltViewModel(),
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    QuestionsHomeScreen(
        state = state,
        onTabSelected = viewModel::onTabSelected,
        onSectionSelected = onSectionSelected,
    )
}

@Composable
fun QuestionsHomeScreen(
    state: QuestionsHomeUiState,
    onTabSelected: (Int) -> Unit,
    onSectionSelected: (ContentSection) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier.fillMaxSize()) {
        TabRow(selectedTabIndex = if (state.isGkTabSelected) 1 else 0) {
            Tab(
                selected = !state.isGkTabSelected,
                onClick = { onTabSelected(0) },
                text = { Text("Aptitude") },
            )
            Tab(
                selected = state.isGkTabSelected,
                onClick = { onTabSelected(1) },
                text = { Text("GK") },
            )
        }

        if (state.isLoading) {
            LoadingState()
            return@Column
        }

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(Spacing.large),
            verticalArrangement = Arrangement.spacedBy(Spacing.medium),
        ) {
            items(state.cards) { card ->
                SectionCardView(card = card, onClick = { onSectionSelected(card.section) })
            }
        }
    }
}

@Composable
private fun SectionCardView(card: SectionCard, onClick: () -> Unit) {
    AptitudeCard(onClick = onClick) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            CircularProgressIndicator(
                percent = card.percent,
                size = 84.dp,
                strokeWidth = 9.dp,
                trackColor = MaterialTheme.colorScheme.primaryContainer,
                progressColor = MaterialTheme.statusColors.accent,
            )
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(start = Spacing.large),
                verticalArrangement = Arrangement.spacedBy(Spacing.small),
            ) {
                Text(
                    text = card.title,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.primary,
                )
                Text(
                    text = "Topics: ${card.topicCount}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
                Text(
                    text = "Questions: ${card.questionCount}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
                LinearProgressIndicator(
                    progress = { card.percent / 100f },
                    modifier = Modifier.fillMaxWidth(),
                    color = MaterialTheme.statusColors.accent,
                    trackColor = MaterialTheme.colorScheme.primaryContainer,
                )
            }
        }
    }
}
