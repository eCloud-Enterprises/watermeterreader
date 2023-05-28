package com.ecloud.apps.watermeterreader.core.data.models

import com.ecloud.apps.watermeterreader.core.database.model.ProjectEntity
import com.ecloud.apps.watermeterreader.core.network.dto.NetworkProject

fun NetworkProject.asEntity() = ProjectEntity(projectcode, projectname)
