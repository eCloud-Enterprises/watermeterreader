package com.ecloud.apps.watermeterreader.core.data.repository

import com.ecloud.apps.watermeterreader.core.data.models.ConnectivityStatus
import kotlinx.coroutines.flow.Flow

interface NetworkRepository {

    suspend fun testEndpoints(): Boolean

    fun getNetworkConnectivityStream(): Flow<ConnectivityStatus>
}
