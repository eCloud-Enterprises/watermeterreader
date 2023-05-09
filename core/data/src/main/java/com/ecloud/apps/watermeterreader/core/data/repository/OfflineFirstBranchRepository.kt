package com.ecloud.apps.watermeterreader.core.data.repository

import com.ecloud.apps.watermeterreader.core.data.Synchronizer
import com.ecloud.apps.watermeterreader.core.data.models.asEntity
import com.ecloud.apps.watermeterreader.core.database.dao.BranchDao
import com.ecloud.apps.watermeterreader.core.database.model.BranchEntity
import com.ecloud.apps.watermeterreader.core.database.model.asExternalModel
import com.ecloud.apps.watermeterreader.core.model.data.Branch
import com.ecloud.apps.watermeterreader.core.network.WmrNetworkDataSource
import com.ecloud.apps.watermeterreader.core.network.dto.NetworkBranch
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class OfflineFirstBranchRepository @Inject constructor(
    private val branchDao: BranchDao,
    private val network: WmrNetworkDataSource
) : BranchRepository {
    override suspend fun syncWith(synchronizer: Synchronizer): Boolean {
        fetchBranches()
        return true
    }

    override fun getBranchesStream(): Flow<List<Branch>> =
        branchDao.getBranchListStream().map { it.map(BranchEntity::asExternalModel) }

    override suspend fun fetchBranches() {
        val response = network.getBranches()

        branchDao.upsertBranches(response.map(NetworkBranch::asEntity))
    }

    override suspend fun deleteAll() = branchDao.deleteAll()
}
