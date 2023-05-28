package com.ecloud.apps.watermeterreader.core.network.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GetProjectsResponse(
    @SerialName("projects")
    val warehouses: List<NetworkProject>
)
