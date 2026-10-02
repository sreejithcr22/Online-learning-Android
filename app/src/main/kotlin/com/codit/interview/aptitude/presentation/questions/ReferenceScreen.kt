package com.codit.interview.aptitude.presentation.questions

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.codit.interview.aptitude.presentation.theme.Spacing

/**
 * Static reference content.
 *
 * Replaces the `InterviewGeneral(GK_INFO)` fragment, which was the "Info" tab shown
 * beside a handful of GK topics and was otherwise identical to the tip viewer.
 */
@Composable
fun ReferenceScreen(
    title: String,
    body: String,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(Spacing.large),
        verticalArrangement = Arrangement.spacedBy(Spacing.medium),
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.primary,
        )
        Text(text = body, style = MaterialTheme.typography.bodyLarge)
    }
}
