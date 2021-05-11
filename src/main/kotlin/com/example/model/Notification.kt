package com.example.model

import com.example.model.enumerations.NotificationType
import kotlinx.serialization.Serializable

@Serializable
data class Notification(
    val id: Int,
    val toUserId: Int,
    val title: String,
    val text: String,
    val type: NotificationType,
    val projectId: Int,
    val replyId: Int,
)