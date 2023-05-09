package com.ecloud.apps.watermeterreader.core.data.repository

import com.ecloud.apps.watermeterreader.core.data.Synchronizer
import com.ecloud.apps.watermeterreader.core.data.models.asEntity
import com.ecloud.apps.watermeterreader.core.database.dao.ProjectDao
import com.ecloud.apps.watermeterreader.core.database.model.ProjectEntity
import com.ecloud.apps.watermeterreader.core.database.model.asExternalModel
import com.ecloud.apps.watermeterreader.core.model.data.Project
import com.ecloud.apps.watermeterreader.core.network.WmrNetworkDataSource
import com.ecloud.apps.watermeterreader.core.network.dto.WarehouseDto
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class OfflineFirstProjectRepository @Inject constructor(
    private val projectDao: ProjectDao,
    private val network: WmrNetworkDataSource
) :
    ProjectRepository {
    override fun getProjectsStream(): Flow<List<Project>> =
        projectDao.getProjectsStream().map { it.map(ProjectEntity::asExternalModel) }

    override suspend fun syncWith(synchronizer: Synchronizer): Boolean {
        fetchWarehouses()
        return true
    }

    override suspend fun deleteAll() = projectDao.deleteAll()

    override suspend fun fetchWarehouses() {
        val response = network.getProjects()

        projectDao.upsertProjects(response.map(WarehouseDto::asEntity))
    }
}
