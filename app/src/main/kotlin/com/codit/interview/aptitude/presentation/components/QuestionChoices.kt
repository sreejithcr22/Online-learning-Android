package com.codit.interview.aptitude.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.codit.interview.aptitude.domain.model.AttemptStatus
import com.codit.interview.aptitude.domain.model.QuestionNavigator
import com.codit.interview.aptitude.presentation.theme.Spacing
import com.codit.interview.aptitude.presentation.theme.statusColors

/**
 * Reads as black or white depending on how bright the backdrop is, so the status
 * colours below can be used as solid fills without becoming unreadable.
 */
@Composable
private fun onColor(background: Color): Color =
    if (background.luminance() > 0.45f) Color(0xFF17202A) else Color.White

/**
 * One answer option.
 *
 * Selection has to be unmissable: before submitting, the chosen row is filled with the
 * primary colour, its letter badge inverts, and a check mark appears. A pale tint on a
 * white card was not enough to tell the user their tap had registered.
 */
@Composable
fun QuestionOptionRow(
    index: Int,
    text: String,
    isSelected: Boolean,
    revealed: Boolean,
    isCorrectOption: Boolean,
    enabled: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val status = MaterialTheme.statusColors
    val shape = MaterialTheme.shapes.small

    val background = when {
        revealed && isCorrectOption -> status.correct.copy(alpha = 0.18f)
        revealed && isSelected -> status.wrong.copy(alpha = 0.18f)
        revealed -> MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.35f)
        isSelected -> MaterialTheme.colorScheme.primary
        else -> MaterialTheme.colorScheme.surface
    }

    val borderColor = when {
        revealed && isCorrectOption -> status.correct
        revealed && isSelected -> status.wrong
        isSelected -> MaterialTheme.colorScheme.primary
        else -> MaterialTheme.colorScheme.outline
    }

    // Thicker outline while selected, so the state reads even in bright sunlight.
    val borderWidth = if (isSelected) 2.dp else 1.dp
    val contentColor = if (revealed) {
        MaterialTheme.colorScheme.onSurface
    } else if (isSelected) {
        MaterialTheme.colorScheme.onPrimary
    } else {
        MaterialTheme.colorScheme.onSurface
    }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(shape)
            .background(background)
            .border(borderWidth, borderColor, shape)
            .clickable(enabled = enabled && !revealed, onClick = onClick)
            .padding(Spacing.medium)
            .semantics {
                contentDescription = "Option ${'A' + index}: $text" +
                    if (isSelected) ", selected" else ""
            },
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(
            modifier = Modifier
                .size(28.dp)
                .clip(CircleShape)
                .background(
                    if (isSelected && !revealed) {
                        MaterialTheme.colorScheme.onPrimary
                    } else {
                        Color.Transparent
                    }
                )
                .border(
                    width = if (isSelected && !revealed) 0.dp else 1.dp,
                    color = if (isSelected && !revealed) Color.Transparent else borderColor,
                    shape = CircleShape,
                ),
            contentAlignment = Alignment.Center,
        ) {
            Text(
                text = ('A' + index).toString(),
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.Bold,
                color = if (isSelected && !revealed) {
                    MaterialTheme.colorScheme.primary
                } else {
                    borderColor
                },
            )
        }

        Text(
            text = text,
            style = MaterialTheme.typography.bodyLarge,
            color = contentColor,
            fontWeight = if (isSelected && !revealed) FontWeight.Medium else FontWeight.Normal,
            modifier = Modifier
                .weight(1f)
                .padding(start = Spacing.medium),
        )

        when {
            revealed && isCorrectOption -> Icon(
                imageVector = Icons.Rounded.Check,
                contentDescription = "Correct answer",
                tint = status.correct,
                modifier = Modifier.size(22.dp),
            )

            revealed && isSelected -> Icon(
                imageVector = Icons.Rounded.Close,
                contentDescription = "Your answer was wrong",
                tint = status.wrong,
                modifier = Modifier.size(22.dp),
            )

            isSelected -> Icon(
                imageVector = Icons.Rounded.Check,
                contentDescription = "Selected",
                tint = MaterialTheme.colorScheme.onPrimary,
                modifier = Modifier.size(22.dp),
            )
        }
    }
}

/**
 * Grid of every question in a session.
 *
 * Colour coding: answered correctly is green, answered wrongly is red, and the question
 * currently being shown is the theme's accent colour with a ring around it — the accent
 * is deliberately *not* green, which previously made the current question look like a
 * correct one.
 */
@Composable
fun QuestionNavigatorGrid(
    navigator: QuestionNavigator,
    currentNumber: Int,
    onSelect: (Int) -> Unit,
    modifier: Modifier = Modifier,
    columns: Int = 5,
    contentPadding: PaddingValues = PaddingValues(0.dp),
) {
    val status = MaterialTheme.statusColors
    val currentColor = MaterialTheme.colorScheme.secondary

    LazyVerticalGrid(
        columns = GridCells.Fixed(columns),
        modifier = modifier,
        contentPadding = contentPadding,
        horizontalArrangement = Arrangement.spacedBy(Spacing.small),
        verticalArrangement = Arrangement.spacedBy(Spacing.small),
    ) {
        items((1..navigator.total).toList()) { number ->
            val attemptStatus = navigator.statuses.getOrNull(number - 1) ?: AttemptStatus.NOT_ATTEMPTED
            val isCurrent = number == currentNumber

            val fill = when {
                isCurrent -> currentColor
                attemptStatus == AttemptStatus.CORRECT -> status.correct
                attemptStatus == AttemptStatus.WRONG -> status.wrong
                else -> status.notAttempted
            }
            val ringColor = if (isCurrent) onColor(currentColor) else Color.Transparent

            Box(
                modifier = Modifier
                    .size(46.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(fill)
                    .border(if (isCurrent) 2.dp else 0.dp, ringColor, RoundedCornerShape(10.dp))
                    .clickable { onSelect(number) }
                    .semantics {
                        contentDescription = buildString {
                            append("Question $number")
                            append(
                                when (attemptStatus) {
                                    AttemptStatus.CORRECT -> ", answered correctly"
                                    AttemptStatus.WRONG -> ", answered incorrectly"
                                    AttemptStatus.NOT_ATTEMPTED -> ", not attempted"
                                }
                            )
                            if (isCurrent) append(", current question")
                        }
                    },
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = "$number",
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = if (isCurrent) FontWeight.Bold else FontWeight.Medium,
                    color = onColor(fill),
                    textAlign = TextAlign.Center,
                    maxLines = 1,
                    overflow = TextOverflow.Clip,
                )
            }
        }
    }
}

/** Legend explaining the navigator's colour coding. */
@Composable
fun NavigatorLegend(modifier: Modifier = Modifier) {
    val status = MaterialTheme.statusColors
    val entries = listOf(
        "Correct" to status.correct,
        "Wrong" to status.wrong,
        "Current" to MaterialTheme.colorScheme.secondary,
        "Not done" to status.notAttempted,
    )
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly,
    ) {
        entries.forEach { (label, color) ->
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(10.dp)
                        .clip(CircleShape)
                        .background(color),
                )
                Text(
                    text = label,
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(start = Spacing.tiny),
                )
            }
        }
    }
}

/** Column layout used by the answer list; kept here so both screens match. */
@Composable
fun OptionList(
    options: List<String>,
    selectedIndex: Int?,
    revealed: Boolean,
    correctOptionIndex: Int,
    enabled: Boolean,
    onSelect: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(Spacing.small),
    ) {
        options.forEachIndexed { index, option ->
            QuestionOptionRow(
                index = index,
                text = option,
                isSelected = selectedIndex == index,
                revealed = revealed,
                isCorrectOption = index == correctOptionIndex,
                enabled = enabled,
                onClick = { onSelect(index) },
            )
        }
    }
}