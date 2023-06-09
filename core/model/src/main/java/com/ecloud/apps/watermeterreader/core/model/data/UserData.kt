package com.ecloud.apps.watermeterreader.core.model.data

data class UserData(
    val id: String,
    val branchName: String,
    val branchCode: String,
    val companyCode: String,
    val projectCode: String,
    val selectedUrl: String,
    val customUrl: List<NetworkUrl>,
    val shouldUpdateAuditList: Boolean,
    val onlineMode: Boolean,
)
