package com.example.dao

import org.jetbrains.exposed.sql.Table

object NotificationTable: Table() {
    val id = integer("id").autoIncrement()
    val toUserId = integer("toUserId") references UserTable.id
    val title = text("title")
    val text = text("text")
    val type = text("type")
    val replyId = integer("replyId")

    override val primaryKey = PrimaryKey(ProjectTable.id, name = "PK_Notification_ID")
}