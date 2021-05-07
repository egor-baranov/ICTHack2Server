package com.example.model

data class Reply(
    val id: Int,
    val text: String,
    val postId: Int,
    val authorId: Int
)
