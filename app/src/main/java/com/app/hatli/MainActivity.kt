package com.app.hatli

import android.content.Context
import android.graphics.Color as AndroidColor
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.LayoutDirection
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.activity.viewModels
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.app.hatli.core.common.util.LocaleHelper
import com.app.hatli.core.data.firebase.AuthActivityHolder
import com.app.hatli.core.designsystem.theme.HatliTheme
import com.app.hatli.core.designsystem.theme.HatliThemeMode
import com.app.hatli.core.domain.repository.SessionPreferencesRepository
import com.app.hatli.core.model.AppThemeMode
import com.app.hatli.navigation.HatliNavHost
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val mainViewModel: MainViewModel by viewModels()

    @Inject
    lateinit var authActivityHolder: AuthActivityHolder

    @Inject
    lateinit var sessionPreferences: SessionPreferencesRepository

    override fun attachBaseContext(newBase: Context) {
        val prefs = newBase.getSharedPreferences("hatli_locale", Context.MODE_PRIVATE)
        val savedLang = prefs.getString("selected_language", "ar") ?: "ar"
        super.attachBaseContext(LocaleHelper.wrapContext(newBase, savedLang))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)

        splashScreen.setKeepOnScreenCondition {
            mainViewModel.startDestination.value == null
        }

        if (::authActivityHolder.isInitialized) {
            authActivityHolder.set(this)
        }
        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.auto(
                lightScrim = AndroidColor.TRANSPARENT,
                darkScrim = AndroidColor.TRANSPARENT,
            ),
            navigationBarStyle = SystemBarStyle.auto(
                lightScrim = AndroidColor.TRANSPARENT,
                darkScrim = AndroidColor.TRANSPARENT,
            ),
        )
        setContent {
            DisposableEffect(this) {
                authActivityHolder.set(this@MainActivity)
                onDispose {
                    // Do not clear activity reference on simple compose disposal if activity is still valid
                }
            }

            val startDestination by mainViewModel.startDestination.collectAsStateWithLifecycle()
            val initialDestination = startDestination ?: return@setContent

            val themeModeState by sessionPreferences.themeMode.collectAsStateWithLifecycle(
                initialValue = AppThemeMode.FollowSystem
            )
            val currentLanguage by sessionPreferences.appLanguage.collectAsStateWithLifecycle(
                initialValue = "ar"
            )

            val hatliTheme = when (themeModeState) {
                AppThemeMode.FollowSystem -> HatliThemeMode.FollowSystem
                AppThemeMode.Light -> HatliThemeMode.Light
                AppThemeMode.Dark -> HatliThemeMode.Dark
            }

            val layoutDirection = if (currentLanguage == "en") LayoutDirection.Ltr else LayoutDirection.Rtl

            HatliTheme(themeMode = hatliTheme) {
                CompositionLocalProvider(LocalLayoutDirection provides layoutDirection) {
                    val snackbarHostState = remember { SnackbarHostState() }
                    val scope = rememberCoroutineScope()
                    Scaffold(
                        modifier = Modifier.fillMaxSize(),
                        contentWindowInsets = WindowInsets(0, 0, 0, 0),
                        containerColor = MaterialTheme.colorScheme.background,
                        contentColor = MaterialTheme.colorScheme.onBackground,
                        snackbarHost = { SnackbarHost(snackbarHostState) },
                    ) {
                        HatliNavHost(
                            initialDestination = initialDestination,
                            modifier = Modifier.fillMaxSize(),
                            onMessage = { message ->
                                scope.launch { snackbarHostState.showSnackbar(message) }
                            },
                        )
                    }
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()
        if (::authActivityHolder.isInitialized) {
            authActivityHolder.set(this)
        }
    }

    override fun onResume() {
        super.onResume()
        if (::authActivityHolder.isInitialized) {
            authActivityHolder.set(this)
        }
    }

    override fun onDestroy() {
        if (::authActivityHolder.isInitialized) {
            authActivityHolder.set(null)
        }
        super.onDestroy()
    }
}
