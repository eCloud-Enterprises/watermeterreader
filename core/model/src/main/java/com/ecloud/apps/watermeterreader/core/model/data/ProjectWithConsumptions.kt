package com.ecloud.apps.watermeterreader.core.model.data

data class ProjectWithConsumptions(
    val project: Project,
    val waterReadingItems: List<WaterReadingItem>
)
