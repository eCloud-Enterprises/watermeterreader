package com.ecloud.apps.watermeterreader.core.data.repository

import com.ecloud.apps.watermeterreader.core.model.data.UserData
import kotlinx.coroutines.flow.Flow

interface UserDataRepository {

    /**
     * Stream of [UserData]
     */
    val userDataStream: Flow<UserData>

    /**
     * Sets the user's  information after authentication
     */
    suspend fun setUserData(
        userId: String,
        branchCode: String,
        branchName: String,
        companyCode: String
    )

    /**
     * Sets the base url for the application
     */
    suspend fun setUrl(url: String)

    /**
     * Adds a url to be used for network request
     */
    suspend fun addUrl(name: String, url: String)

    /**
     * Updates the url by name
     */
    suspend fun updateUrlByName(name: String, url: String)

    /**
     * Updates the warehouse code
     */
    suspend fun setProjectCode(warehouseCode: String)

    /**
     * Updates the branch code and branch name
     */
    suspend fun setBranch(branchCode: String, branchName: String)

    /**
     * Updates the should update audit list after onboard
     */
    suspend fun toggleShouldUpdateAuditList(value: Boolean)

    /**
     * Clears the custom network url set
     */
    suspend fun clearUrl()

    /**
     * Toggle offline mode
     */
    suspend fun toggleMode(value: Boolean)

    /**
     * Deletes URL by Key
     */
    suspend fun deleteUrlByKey(key: String)
}
