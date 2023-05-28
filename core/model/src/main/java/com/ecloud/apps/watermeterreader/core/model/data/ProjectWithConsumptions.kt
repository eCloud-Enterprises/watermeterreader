package com.ecloud.apps.watermeterreader.core.model.data

data class ProjectWithConsumptions(
    val code: String,
    val name: String,
    val consumptions: List<Consumption>
)
