package com.ecloud.apps.watermeterreader.core.model.data

data class Project(
    val code: String,
    val name: String,
)

fun Project.getDisplayName(): String = "$code ${if (name.isNotEmpty()) "-" else ""} $name"
