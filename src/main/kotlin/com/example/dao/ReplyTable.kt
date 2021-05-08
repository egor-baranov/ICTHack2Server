package com.example.dao

import org.jetbrains.exposed.sql.*

object ReplyTable : Table() {
    val id = integer("id").autoIncrement()
    val text = text("text")
    val authorId = integer("authorId") references UserTable.id
    val projectId = integer("projectId") references ProjectTable.id

    override val primaryKey = PrimaryKey(id, name = "PK_Reply_ID")
}