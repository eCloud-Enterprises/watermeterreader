package com.ecloud.apps.watermeterreader.core.network.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class NetworkBranch(
    @SerialName("branchcode")
    val branchcode: String,
    @SerialName("branchname")
    val branchname: String
)
