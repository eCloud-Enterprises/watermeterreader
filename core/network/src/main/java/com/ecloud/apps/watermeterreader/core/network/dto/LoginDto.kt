package com.ecloud.apps.watermeterreader.core.network.dto

import kotlinx.serialization.Serializable

@Serializable
data class LoginDto(
    val branchcode: String? = null,
    val branchname: String? = null,
    val companycode: String? = null,
    val companyname: String? = null,
    val corid: String? = null,
    val groupid: String? = null,
    val returnstatus: String,
    val roleid: String? = null,
    val suser: String? = null,
    val userid: String? = null,
    val username: String? = null
)
