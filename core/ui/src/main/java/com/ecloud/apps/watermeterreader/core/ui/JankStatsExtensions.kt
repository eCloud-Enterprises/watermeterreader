package com.ecloud.apps.watermeterreader.core.ui

import androidx.compose.foundation.gestures.ScrollableState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.DisposableEffectResult
import androidx.compose.runtime.DisposableEffectScope
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.platform.LocalView
import androidx.metrics.performance.PerformanceMetricsState
import kotlinx.coroutines.CoroutineScope

/**
 * Retrieves [PerformanceMetricsState.MetricsStateHolder] from current [LocalView] and
 * remembers it until the View changes.
 * @see PerformanceMetricsState.getForHierarchy
 */
@Composable
fun rememberMetricsStateHolder(): PerformanceMetricsState.Holder {
    val localView = LocalView.current

    return remember(localView) {
        PerformanceMetricsState.getHolderForHierarchy(localView)
    }
}

/**
 * Convenience function to work with [PerformanceMetricsState] state. The side effect is
 * re-launched if any of the [keys] value is not equal to the previous composition.
 * @see JankMetricDisposableEffect if you need to work with DisposableEffect to cleanup added state.
 */
@Composable
fun JankMetricEffect(
    vararg keys: Any?,
    reportMetric: suspend CoroutineScope.(state: PerformanceMetricsState.Holder) -> Unit
) {
    val metrics = rememberMetricsStateHolder()
    LaunchedEffect(metrics, *keys) {
        reportMetric(metrics)
    }
}

typealias ReportMetricListener =
    DisposableEffectScope.(state: PerformanceMetricsState.Holder) -> DisposableEffectResult

/**
 * Convenience function to work with [PerformanceMetricsState] state that needs to be cleaned up.
 * The side effect is re-launched if any of the [keys] value is not equal to the previous composition.
 */
@Composable
fun JankMetricDisposableEffect(
    vararg keys: Any?,
    reportMetric: ReportMetricListener
) {
    val metrics = rememberMetricsStateHolder()
    DisposableEffect(metrics, *keys) {
        reportMetric(this, metrics)
    }
}

@Composable
fun TrackScrollJank(scrollableState: ScrollableState, stateName: String) {
    JankMetricEffect(scrollableState) { metricsHolder ->
        snapshotFlow { scrollableState.isScrollInProgress }.collect { isScrollInProgress ->
            metricsHolder.state?.apply {
                if (isScrollInProgress) {
                    putState(stateName, "Scrolling=true")
                } else {
                    removeState(stateName)
                }
            }
        }
    }
}
