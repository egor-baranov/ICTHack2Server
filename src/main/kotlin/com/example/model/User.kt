package com.example.model

import com.example.model.enumerations.UserSpecialization
import kotlinx.serialization.Serializable

@Serializable
data class User(
    val id: Int,
    val userName: String,
    val password: String,
    val rating: Float,
    val specialization: UserSpecialization,
    val firstName: String,
    val lastName: String,
    val profileDescription: String,
    val githubProfileLink: String,
    val projectIdList: List<Int>,
    val replyIdList: List<Int>,
    val tgId: String
)