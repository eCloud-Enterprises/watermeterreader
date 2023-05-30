package com.ecloud.apps.watermeterreader.core.database.model

import androidx.room.Embedded
import androidx.room.Relation
import com.ecloud.apps.watermeterreader.core.model.data.ProjectWithConsumptions

data class ProjectWithConsumptionsEntity(
    @Embedded
    val project: ProjectEntity,
    @Relation(
        parentColumn = "code",
        entityColumn = "projectCode"
    )
    val consumptions: List<ConsumptionEntity>
)

fun ProjectWithConsumptionsEntity.asExternalModel() = ProjectWithConsumptions(
    project = project.asExternalModel(), consumptions = consumptions.map { it.asExternalModel() }
)

fun List<ProjectWithConsumptionsEntity>.asExternalModels() =
    map(ProjectWithConsumptionsEntity::asExternalModel)