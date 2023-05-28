package com.ecloud.apps.watermeterreader.core.network.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class NetworkProject(
    @SerialName("projectcode")
    val projectcode: String,
    @SerialName("projectname")
    val projectname: String
)
