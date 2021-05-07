package com.example.model

import com.example.model.enumerations.ProjectTags
import kotlinx.serialization.Serializable

@Serializable
data class Project(
    val id: Int,
    val name: String,
    val description: String,
    val replyIdList: List<Int>,
    val githubProjectLink: String,
    val tags: List<ProjectTags>,
    val ownerId: Int
)
