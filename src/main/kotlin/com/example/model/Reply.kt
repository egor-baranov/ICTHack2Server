package com.example.model

import kotlinx.serialization.Serializable

@Serializable
data class Reply(
    val id: Int,
    val text: String,
    val projectId: Int,
    val authorId: Int,
)
