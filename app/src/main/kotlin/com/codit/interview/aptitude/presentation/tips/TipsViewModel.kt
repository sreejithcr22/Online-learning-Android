package com.codit.interview.aptitude.presentation.tips

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.codit.interview.aptitude.domain.model.Tip
import com.codit.interview.aptitude.domain.repository.TipRepository
import com.codit.interview.aptitude.domain.usecase.AddTipToFavouritesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

/** A tab of the tips/formula/interview pager. */
data class TipTab(val title: String, val table: String, val favouritesTable: String)

data class TipsUiState(
    val isLoading: Boolean = true,
    val tabs: List<TipTab> = emptyList(),
    val selectedTab: Int = 0,
    val tips: List<Tip> = emptyList(),
    val currentIndex: Int = 0,
) {
    val currentTip: Tip? get() = tips.getOrNull(currentIndex)
    val isFavouritesTab: Boolean
        get() = tabs.getOrNull(selectedTab)?.table == tabs.getOrNull(selectedTab)?.favouritesTable
    val canGoNext: Boolean get() = currentIndex < tips.lastIndex
    val canGoPrevious: Boolean get() = currentIndex > 0
}

/**
 * Drives every "list of tips with next/previous" screen.
 *
 * `InterviewActivity`, `ConceptsActivity` and `FavActivity` each hosted their own
 * 650-line `Fragment` (`InterviewGeneral` / `ConceptFragment`) containing an identical
 * copy-paste of the same paging and animation code. One ViewModel now serves all three.
 */
@OptIn(kotlinx.coroutines.ExperimentalCoroutinesApi::class)
@HiltViewModel
class TipsViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val tips: TipRepository,
    private val addToFavourites: AddTipToFavouritesUseCase,
) : ViewModel() {

    private val tabSpec: String = checkNotNull(savedStateHandle.get<String>(ARG_KIND))

    private val selectedTab = MutableStateFlow(0)

    private val currentIndex = MutableStateFlow(0)

    private val tabs: List<TipTab> = TipCatalog.forKind(tabSpec)

    /** Re-subscribes to the tip table of whichever tab is active. */
    private val activeTips = selectedTab.flatMapLatest { index ->
        tips.observeTips(tabs[index.coerceIn(0, tabs.lastIndex)].table)
    }

    val uiState: StateFlow<TipsUiState> = combine(
        selectedTab,
        currentIndex,
        activeTips,
    ) { tab, index, list ->
        TipsUiState(
            isLoading = false,
            tabs = tabs,
            selectedTab = tab.coerceIn(0, tabs.lastIndex),
            tips = list,
            currentIndex = index.coerceIn(0, maxOf(list.lastIndex, 0)),
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(STOP_TIMEOUT_MILLIS),
        initialValue = TipsUiState(tabs = tabs),
    )

    fun onTabSelected(index: Int) {
        selectedTab.value = index.coerceIn(0, tabs.lastIndex)
        currentIndex.value = 0
    }

    fun next() {
        val last = uiState.value.tips.lastIndex
        if (currentIndex.value < last) currentIndex.value = currentIndex.value + 1
    }

    fun previous() {
        if (currentIndex.value > 0) currentIndex.value = currentIndex.value - 1
    }

    fun goTo(index: Int) {
        currentIndex.value = index.coerceIn(0, maxOf(uiState.value.tips.lastIndex, 0))
    }

    fun toggleFavourite() {
        val state = uiState.value
        val tip = state.currentTip ?: return
        val tab = tabs[state.selectedTab]
        if (tab.table == tab.favouritesTable) return
        viewModelScope.launch {
            addToFavourites(tab.table, tip.number, tab.favouritesTable)
        }
    }

    companion object {
        const val ARG_KIND = "kind"
        private const val STOP_TIMEOUT_MILLIS = 5_000L
    }
}
