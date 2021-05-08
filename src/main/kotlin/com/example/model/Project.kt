package com.example.model

import com.example.model.enumerations.ProjectTags
import kotlinx.serialization.Serializable

@Serializable
data class Project(
    val id: Int,
    val name: String,
    val description: String,
    val replyIdList: MutableList<Int>,
    val githubProjectLink: String,
    val tags: MutableList<ProjectTags>,
    val ownerId: Int,
    val vacancy: MutableMap<String, Int>,
    val freeVacancy: MutableMap<String, Int>,
)
