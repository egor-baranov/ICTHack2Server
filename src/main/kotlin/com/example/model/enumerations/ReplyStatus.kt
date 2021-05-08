package com.example.model.enumerations

import kotlinx.serialization.Serializable

@Serializable
enum class ReplyStatus {
    ACCEPTED, DENIED, WAIT
}