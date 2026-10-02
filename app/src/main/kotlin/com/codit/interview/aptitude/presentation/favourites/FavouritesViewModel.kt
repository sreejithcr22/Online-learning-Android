package com.codit.interview.aptitude.presentation.favourites

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.codit.interview.aptitude.domain.model.ContentSection
import com.codit.interview.aptitude.domain.model.Question
import com.codit.interview.aptitude.domain.model.Topic
import com.codit.interview.aptitude.domain.repository.QuestionRepository
import com.codit.interview.aptitude.domain.usecase.ObserveFavouriteQuestionsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn

/** One of the four lists on the Favourites screen. */
data class FavouriteTab(
    val title: String,
    val section: ContentSection?,
    val practiceMode: FavouritePracticeMode?,
)

/** How a favourites list is opened once a row is tapped. */
enum class FavouritePracticeMode { APTITUDE, GENERAL_KNOWLEDGE }

data class FavouritesUiState(
    val isLoading: Boolean = true,
    val tabs: List<FavouriteTab> = emptyList(),
    val selectedTab: Int = 0,
    val questions: List<Question> = emptyList(),
    val counts: Map<String, Int> = emptyMap(),
) {
    val activeTab: FavouriteTab? get() = tabs.getOrNull(selectedTab)
}

/**
 * Favourites screen: interview tips, formulas, GK questions and aptitude questions.
 *
 * Replaces `FavActivity`, which implemented seven callback interfaces at once so the
 * same `QuestionFragBase` could serve two of its tabs.
 */
@OptIn(kotlinx.coroutines.ExperimentalCoroutinesApi::class)
@HiltViewModel
class FavouritesViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val questions: QuestionRepository,
    observeFavourites: ObserveFavouriteQuestionsUseCase,
) : ViewModel() {

    private val openTab: String? = savedStateHandle.get<String>(ARG_TAB)

    private val selectedTab = MutableStateFlow(
        TABS.indexOfFirst { it.title.equals(openTab, ignoreCase = true) }.coerceAtLeast(0)
    )

    private val activeFavourites = selectedTab
        .flatMapLatest { index -> observeFavourites(sectionFor(index)) }

    val uiState: StateFlow<FavouritesUiState> = combine(
        selectedTab,
        activeFavourites,
    ) { index, list ->
        FavouritesUiState(
            isLoading = false,
            tabs = TABS,
            selectedTab = index,
            questions = list,
            counts = TABS.associate { it.title to 0 },
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(STOP_TIMEOUT_MILLIS),
        initialValue = FavouritesUiState(tabs = TABS),
    )

    fun onTabSelected(index: Int) {
        selectedTab.value = index.coerceIn(0, TABS.lastIndex)
    }

    private fun sectionFor(index: Int): ContentSection = when (TABS[index.coerceIn(0, TABS.lastIndex)].practiceMode) {
        FavouritePracticeMode.APTITUDE -> ContentSection.QUANTITATIVE
        else -> ContentSection.GENERAL_KNOWLEDGE
    }

    companion object {
        const val ARG_TAB = "tab"
        private const val STOP_TIMEOUT_MILLIS = 5_000L

        /**
         * Aptitude favourites all live in one table regardless of the original topic,
         * so that tab opens the practice screen against `fav_table` via [Topic]'s
         * `OfFavourites` source.
         */
        val TABS = listOf(
            FavouriteTab("Aptitude", ContentSection.QUANTITATIVE, FavouritePracticeMode.APTITUDE),
            FavouriteTab("GK", ContentSection.GENERAL_KNOWLEDGE, FavouritePracticeMode.GENERAL_KNOWLEDGE),
        )
    }
}
