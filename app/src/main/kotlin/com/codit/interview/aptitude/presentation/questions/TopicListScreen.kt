package com.codit.interview.aptitude.presentation.questions

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
import androidx.compose.material.icons.rounded.Info
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.codit.interview.aptitude.domain.model.Topic
import com.codit.interview.aptitude.presentation.components.AptitudeCard
import com.codit.interview.aptitude.presentation.components.LoadingState
import com.codit.interview.aptitude.presentation.theme.Spacing
import com.codit.interview.aptitude.presentation.theme.statusColors

@Composable
fun TopicListRoute(
    onTopicSelected: (Topic, Int, Boolean) -> Unit,
    onBack: () -> Unit,
    viewModel: TopicListViewModel = hiltViewModel(),
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    if (state.isLoading) {
        LoadingState()
        return
    }

    TopicListScreen(
        state = state,
        onTopicSelected = { row ->
            onTopicSelected(row.topic, row.timerSeconds, row.isInfoOnly)
        },
    )

}

@Composable
fun TopicListScreen(
    state: TopicListUiState,
    onTopicSelected: (TopicRow) -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(Spacing.large),
        verticalArrangement = Arrangement.spacedBy(Spacing.medium),
    ) {
        items(state.rows, key = { it.topic.name }) { row ->
            AptitudeCard(onClick = { onTopicSelected(row) }) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Column(
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.spacedBy(Spacing.tiny),
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = row.topic.displayName,
                                style = MaterialTheme.typography.titleMedium,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                                modifier = Modifier.weight(1f, fill = false),
                            )
                            if (row.isInfoOnly) {
                                Icon(
                                    imageVector = Icons.Rounded.Info,
                                    contentDescription = "Reference only",
                                    tint = MaterialTheme.statusColors.accent,
                                    modifier = Modifier.padding(start = Spacing.small),
                                )
                            }
                        }
                        Text(
                            text = "Attempted: ${row.percent}%",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                        LinearProgressIndicator(
                            progress = { row.percent / 100f },
                            modifier = Modifier.fillMaxWidth(),
                            color = MaterialTheme.statusColors.accent,
                            trackColor = MaterialTheme.colorScheme.primaryContainer,
                        )
                    }

                }
            }
        }
    }
}

private const val MAX_TOPIC_MINUTES = 15
