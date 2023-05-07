package com.ecloud.apps.watermeterreader.core.network.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GetBranchesResponse(
    @SerialName("branches")
    val branches: List<NetworkBranch>
)
