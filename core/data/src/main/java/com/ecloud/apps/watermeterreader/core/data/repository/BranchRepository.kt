package com.ecloud.apps.watermeterreader.core.data.repository

import com.ecloud.apps.watermeterreader.core.data.Syncable
import com.ecloud.apps.watermeterreader.core.model.data.Branch
import kotlinx.coroutines.flow.Flow

interface BranchRepository : Syncable {

    fun getBranchesStream(): Flow<List<Branch>>

    suspend fun fetchBranches()

    suspend fun deleteAll()
}
