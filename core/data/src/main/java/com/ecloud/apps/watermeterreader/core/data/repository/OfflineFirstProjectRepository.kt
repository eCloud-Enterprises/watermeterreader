package com.ecloud.apps.watermeterreader.core.data.repository

import com.ecloud.apps.watermeterreader.core.data.Synchronizer
import com.ecloud.apps.watermeterreader.core.data.models.asEntity
import com.ecloud.apps.watermeterreader.core.database.dao.ProjectDao
import com.ecloud.apps.watermeterreader.core.database.model.ProjectEntity
import com.ecloud.apps.watermeterreader.core.database.model.ProjectWithConsumptionsEntity
import com.ecloud.apps.watermeterreader.core.database.model.asExternalModel
import com.ecloud.apps.watermeterreader.core.database.model.asExternalModels
import com.ecloud.apps.watermeterreader.core.model.data.Project
import com.ecloud.apps.watermeterreader.core.model.data.ProjectWithConsumptions
import com.ecloud.apps.watermeterreader.core.network.WmrNetworkDataSource
import com.ecloud.apps.watermeterreader.core.network.dto.NetworkProject
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
        fetchProjects()
        return true
    }

    override suspend fun deleteAll() = projectDao.deleteAllProjects()

    override suspend fun fetchProjects() {
        val response = network.getProjects()

        projectDao.upsertProjects(response.map(NetworkProject::asEntity))
    }

    override fun getProjectsWithConsumptions(): Flow<List<ProjectWithConsumptions>> =
        projectDao.getProjectsWithConsumptionsStream()
            .map { it.asExternalModels() }
}
