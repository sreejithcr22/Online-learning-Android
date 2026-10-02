package com.codit.interview.aptitude.presentation.tips

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Favorite
import androidx.compose.material.icons.rounded.FavoriteBorder
import androidx.compose.material.icons.automirrored.rounded.NavigateBefore
import androidx.compose.material.icons.automirrored.rounded.NavigateNext
import androidx.compose.material.icons.rounded.Share
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.codit.interview.aptitude.domain.model.Tip
import com.codit.interview.aptitude.presentation.navigation.shareTip
import com.codit.interview.aptitude.presentation.components.AptitudeCard
import com.codit.interview.aptitude.presentation.components.EmptyState
import com.codit.interview.aptitude.presentation.components.LoadingState
import com.codit.interview.aptitude.presentation.theme.Spacing
import com.codit.interview.aptitude.presentation.theme.statusColors

@Composable
fun TipsRoute(
    onBack: () -> Unit,
    viewModel: TipsViewModel = hiltViewModel(),
) {
    val context = LocalContext.current
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    TipsScreen(
        state = state,
        onTabSelected = viewModel::onTabSelected,
        onNext = viewModel::next,
        onPrevious = viewModel::previous,
        onToggleFavourite = viewModel::toggleFavourite,
        onShare = { tip ->
            if (tip != null) context.shareTip(tip.title, tip.body)
        },
    )
}

@Composable
fun TipsScreen(
    state: TipsUiState,
    onTabSelected: (Int) -> Unit,
    onNext: () -> Unit,
    onPrevious: () -> Unit,
    onToggleFavourite: () -> Unit,
    onShare: (Tip?) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier.fillMaxSize()) {
        if (state.isLoading) {
            LoadingState()
            return@Column
        }

        ScrollableTabRow(
            selectedTabIndex = state.selectedTab,
            edgePadding = Spacing.small,
        ) {
            state.tabs.forEachIndexed { index, tab ->
                Tab(
                    selected = index == state.selectedTab,
                    onClick = { onTabSelected(index) },
                    text = { Text(tab.title) },
                )
            }
        }

        val tip = state.currentTip
        if (tip == null) {
            EmptyState("No records found!")
            return@Column
        }

        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
                .padding(Spacing.large),
        ) {
            AptitudeCard {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    if (!state.isFavouritesTab) {
                        IconButton(onClick = onToggleFavourite, enabled = !tip.isFavourite) {
                            Icon(
                                imageVector = if (tip.isFavourite) {
                                    Icons.Rounded.Favorite
                                } else {
                                    Icons.Rounded.FavoriteBorder
                                },
                                contentDescription = "Add to favourites",
                                tint = if (tip.isFavourite) {
                                    MaterialTheme.statusColors.wrong
                                } else {
                                    MaterialTheme.colorScheme.onSurfaceVariant
                                },
                            )
                        }
                        IconButton(onClick = { onShare(tip) }) {
                            Icon(Icons.Rounded.Share, contentDescription = "Share")
                        }
                    }
                }
                Spacer(Modifier.height(Spacing.small))
                AnimatedContent(
                    targetState = tip,
                    transitionSpec = {
                        (slideInHorizontally { it / 3 } + fadeIn(tween(220)))
                            .togetherWith(slideOutHorizontally { -it / 3 } + fadeOut(tween(160)))
                    },
                    label = "tip",
                ) { target ->
                    Column {
                        Text(
                            text = target.title.ifBlank { "Tip #${target.number}" },
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.primary,
                        )
                        Spacer(Modifier.height(Spacing.medium))
                        Text(
                            text = target.body,
                            style = MaterialTheme.typography.bodyLarge,
                        )
                    }
                }
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(Spacing.large),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            NavigationButton(
                label = "Previous",
                icon = { Icon(Icons.AutoMirrored.Rounded.NavigateBefore, contentDescription = null) },
                enabled = state.canGoPrevious,
                onClick = onPrevious,
            )
            Text(
                text = "${state.currentIndex + 1} / ${maxOf(state.tips.size, 1)}",
                style = MaterialTheme.typography.labelLarge,
                textAlign = TextAlign.Center,
            )
            NavigationButton(
                label = "Next",
                icon = { Icon(Icons.AutoMirrored.Rounded.NavigateNext, contentDescription = null) },
                enabled = state.canGoNext,
                onClick = onNext,
            )
        }
    }
}

@Composable
private fun NavigationButton(
    label: String,
    icon: @Composable () -> Unit,
    enabled: Boolean,
    onClick: () -> Unit,
) {
    androidx.compose.material3.TextButton(onClick = onClick, enabled = enabled) {
        icon()
        Text("  $label")
    }
}
