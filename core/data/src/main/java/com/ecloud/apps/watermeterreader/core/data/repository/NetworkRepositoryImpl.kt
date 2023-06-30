package com.ecloud.apps.watermeterreader.core.data.repository

import com.ecloud.apps.watermeterreader.core.data.models.ConnectivityStatus
import com.ecloud.apps.watermeterreader.core.network.Dispatcher
import com.ecloud.apps.watermeterreader.core.network.WmrNetworkDataSource
import com.ecloud.apps.watermeterreader.core.network.WmrDispatcher
import com.ecloud.apps.watermeterreader.core.network.observers.ConnectivityObserver
import com.ecloud.apps.watermeterreader.core.network.observers.NetworkConnectivityObserver
import javax.inject.Inject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class NetworkRepositoryImpl @Inject constructor(
    private val wmrNetworkDataSource: WmrNetworkDataSource,
    private val observer: NetworkConnectivityObserver,
    @Dispatcher(WmrDispatcher.IO) private val ioDispatcher: CoroutineDispatcher
) : NetworkRepository {
    override suspend fun testEndpoints(): Boolean = withContext(ioDispatcher) {
        val isOk = awaitAll(
            async {
                wmrNetworkDataSource.checkProjects()
            },
        ).all { it }

        isOk
    }

    override fun getNetworkConnectivityStream(): Flow<ConnectivityStatus> =
        observer.observer().map { status ->
            when (status) {
                ConnectivityObserver.Status.Available -> ConnectivityStatus.Available
                ConnectivityObserver.Status.Unavailable -> ConnectivityStatus.Unavailable
                ConnectivityObserver.Status.Losing -> ConnectivityStatus.Losing
                ConnectivityObserver.Status.Lost -> ConnectivityStatus.Lost
            }
        }
}
