package com.app.hatli

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
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import com.app.hatli.core.data.firebase.AuthActivityHolder
import com.app.hatli.core.designsystem.theme.HatliTheme
import com.app.hatli.core.designsystem.theme.HatliThemeMode
import com.app.hatli.navigation.HatliNavHost
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var authActivityHolder: AuthActivityHolder

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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
            DisposableEffect(Unit) {
                authActivityHolder.set(this@MainActivity)
                onDispose { authActivityHolder.set(null) }
            }
            // FollowSystem applies light + dark from device setting.
            HatliTheme(themeMode = HatliThemeMode.FollowSystem) {
                val snackbarHostState = remember { SnackbarHostState() }
                val scope = rememberCoroutineScope()
                // Zero insets here — each destination owns status/nav bar padding once.
                // Nesting Scaffold + TopAppBar insets caused a large empty band above Home.
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    contentWindowInsets = WindowInsets(0, 0, 0, 0),
                    containerColor = MaterialTheme.colorScheme.background,
                    contentColor = MaterialTheme.colorScheme.onBackground,
                    snackbarHost = { SnackbarHost(snackbarHostState) },
                ) {
                    HatliNavHost(
                        modifier = Modifier.fillMaxSize(),
                        onMessage = { message ->
                            scope.launch { snackbarHostState.showSnackbar(message) }
                        },
                    )
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        if (::authActivityHolder.isInitialized) {
            authActivityHolder.set(this)
        }
    }

    override fun onPause() {
        if (::authActivityHolder.isInitialized) {
            authActivityHolder.set(null)
        }
        super.onPause()
    }
}
