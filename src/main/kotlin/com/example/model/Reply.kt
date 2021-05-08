package com.example.model

import com.example.model.enumerations.ReplyStatus
import kotlinx.serialization.Serializable

@Serializable
data class Reply(
    val id: Int,
    val text: String,
    val projectId: Int,
    val authorId: Int,
    val vacancy: String,
    val status: ReplyStatus
)
