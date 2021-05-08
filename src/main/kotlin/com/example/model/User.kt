package com.example.model

import com.example.model.enumerations.UserSpecialization
import kotlinx.serialization.Serializable

@Serializable
data class User(
    var id: Int,
    var rating: Float,
    var specialization: UserSpecialization,
    var firstName: String,
    var lastName: String,
    var profileDescription: String,
    var githubProfileLink: String,
    var projectIdList: MutableList<Int>,
    var replyIdList: MutableList<Int>,
    var tgId: String,
)