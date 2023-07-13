package com.ecloud.apps.watermeterreader.core.data.repository

import com.ecloud.apps.watermeterreader.core.data.Syncable
import com.ecloud.apps.watermeterreader.core.model.data.Project
import com.ecloud.apps.watermeterreader.core.model.data.ProjectWithConsumptions
import kotlinx.coroutines.flow.Flow

interface ProjectRepository : Syncable {

    fun getProjectsStream(): Flow<List<Project>>

    fun getProjectsWithConsumptions(): Flow<List<ProjectWithConsumptions>>

    suspend fun fetchProjects()

    suspend fun deleteAll()
}
