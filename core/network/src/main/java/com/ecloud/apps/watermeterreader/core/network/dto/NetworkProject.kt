package com.ecloud.apps.watermeterreader.core.network.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class NetworkProject(
    @SerialName("docno")
    val projectCode: String,
    @SerialName("project")
    val projectName: String
)
