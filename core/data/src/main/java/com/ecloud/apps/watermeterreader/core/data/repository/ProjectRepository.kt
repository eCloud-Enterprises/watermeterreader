package com.ecloud.apps.watermeterreader.core.data.repository

import com.ecloud.apps.watermeterreader.core.data.Syncable
import com.ecloud.apps.watermeterreader.core.model.data.Project
import kotlinx.coroutines.flow.Flow

interface ProjectRepository : Syncable {

    fun getProjectsStream(): Flow<List<Project>>

    suspend fun fetchWarehouses()

    suspend fun deleteAll()
}
