package com.example.model

import com.example.model.enumerations.UserSpecialization

data class User(
    val id: Int,
    val password: String,
    val rating: Float,
    val specialization: UserSpecialization,
    val firstName: String,
    val lastName: String,
    val profileDescription: String,
    val githubProfileLink: String,
    val projectIdList: List<Int>,
    val replyIdList: List<Int>
)