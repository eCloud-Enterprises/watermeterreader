package com.ecloud.apps.watermeterreader

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.metrics.performance.JankStats
import com.ecloud.apps.watermeterreader.core.WhileUiSubscribed
import com.ecloud.apps.watermeterreader.core.data.repository.UserDataRepository
import com.ecloud.apps.watermeterreader.core.designsystem.theme.WmrTheme
import com.ecloud.apps.watermeterreader.core.result.Result
import com.ecloud.apps.watermeterreader.core.result.asResult
import com.ecloud.apps.watermeterreader.navigation.NavGraphs
import com.ecloud.apps.watermeterreader.ui.App
import com.ramcosta.composedestinations.spec.Route
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    /**
     * Lazily inject [JankyStats], which is used to track jank throught the app
     */
    @Inject
    lateinit var lazyStats: dagger.Lazy<JankStats>

    @Inject
    lateinit var userDataRepository: UserDataRepository

    private var loading = true


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        installSplashScreen().setKeepOnScreenCondition { loading }

        val uiState =
            userDataRepository.userDataStream.asResult()
                .flowWithLifecycle(lifecycle)
                .map { result ->
                    when (result) {
                        is Result.Error,
                        Result.Loading -> UiState.Loading

                        is Result.Success -> {
                            val user = result.data
                            val route = if (user.selectedUrl.isEmpty()) {
                                NavGraphs.onboarding
                            } else if (user.id.isEmpty()) {
                                NavGraphs.auth
                            } else {
                                NavGraphs.root
                            }
                            UiState.Success(NavGraphs.reader)
                        }
                    }
                }
                .stateIn(
                    scope = lifecycleScope,
                    started = WhileUiSubscribed,
                    initialValue = UiState.Loading
                )

        lifecycleScope.launch {
            uiState.collect { state ->
                when (state) {
                    UiState.Loading -> loading = true
                    is UiState.Success -> {
                        loading = false
                        setContent {
                            WmrTheme {
                                App(startRoute = state.startRoute)
                            }
                        }
                    }
                }
            }
        }


    }
}

sealed interface UiState {
    data class Success(val startRoute: Route) : UiState
    object Loading : UiState
}