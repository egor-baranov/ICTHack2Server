package com.example.dao

import org.jetbrains.exposed.sql.Table

object NotificationTable : Table() {
    val id = integer("id").autoIncrement()
    val toUserId = integer("toUserId") references UserTable.id
    val title = text("title")
    val text = text("text")
    val type = text("type")
    val projectId = integer("projectId") references ProjectTable.id
    val replyId = integer("replyId") references ReplyTable.id

    override val primaryKey = PrimaryKey(ProjectTable.id, name = "PK_Notification_ID")
}