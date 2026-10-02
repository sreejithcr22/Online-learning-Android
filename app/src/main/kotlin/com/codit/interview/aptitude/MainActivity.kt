package com.codit.interview.aptitude

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.codit.interview.aptitude.domain.model.AppTheme
import com.codit.interview.aptitude.domain.repository.AppStateRepository
import com.codit.interview.aptitude.domain.repository.SettingsRepository
import com.codit.interview.aptitude.presentation.navigation.AptitudeApp
import com.codit.interview.aptitude.presentation.navigation.openPlayStore
import com.codit.interview.aptitude.presentation.navigation.sendFeedback
import com.codit.interview.aptitude.presentation.navigation.shareApp
import com.codit.interview.aptitude.presentation.theme.AptitudeTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

/**
 * The app's single `Activity`.
 *
 * Replaces eleven `Activity` classes (and their `NavActivityBase` superclass with its
 * `DrawerLayout`, `BottomNavigationView` and `TabLayout` wiring) with one Compose
 * entry point and a navigation graph.
 */
@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var settingsRepository: SettingsRepository

    @Inject
    lateinit var appState: AppStateRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        requestNotificationPermissionIfNeeded()

        val themeFlow = settingsRepository.observeTheme()
            .stateIn(lifecycleScope, SharingStarted.Eagerly, AppTheme.LIGHT)

        setContent {
            val theme by themeFlow.collectAsStateWithLifecycle()
            AptitudeTheme(theme = theme) {
                AptitudeApp(
                    onOpenStore = { openPlayStore() },
                    onShareApp = { shareApp() },
                    onSendFeedback = { sendFeedback() },
                )
            }
        }
    }

    override fun onStart() {
        super.onStart()
        lifecycleScope.launch { appState.markVisited() }
    }

    private fun requestNotificationPermissionIfNeeded() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) return
        val permission = android.Manifest.permission.POST_NOTIFICATIONS
        if (checkSelfPermission(permission) != android.content.pm.PackageManager.PERMISSION_GRANTED) {
            requestPermissions(arrayOf(permission), NOTIFICATION_PERMISSION_REQUEST)
        }
    }

    private companion object {
        const val NOTIFICATION_PERMISSION_REQUEST = 1001
    }
}
