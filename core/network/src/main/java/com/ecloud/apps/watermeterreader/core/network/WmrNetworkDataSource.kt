package com.ecloud.apps.watermeterreader.core.network

import com.ecloud.apps.watermeterreader.core.network.dto.LoginDto
import com.ecloud.apps.watermeterreader.core.network.dto.NetworkBranch
import com.ecloud.apps.watermeterreader.core.network.dto.NetworkConsumption
import com.ecloud.apps.watermeterreader.core.network.dto.NetworkProject

/**
 * Interface representing network calls to the eBT Backend
 */
interface WmrNetworkDataSource {

    suspend fun getProjects(): List<NetworkProject>

    suspend fun getConsumptions(
        projectCode: String
    ): List<NetworkConsumption>

    suspend fun login(userid: String, password: String): LoginDto

    suspend fun checkProjects(): Boolean

    suspend fun getBranches(): List<NetworkBranch>
}
