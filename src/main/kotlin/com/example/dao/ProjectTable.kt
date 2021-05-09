package com.example.dao

import org.jetbrains.exposed.sql.Table

object ProjectTable : Table() {
    val id = integer("id").autoIncrement()
    val name = text("name")
    val description = text("description")
    val githubProjectLink = text("githubProjectLink")
    val ownerId = integer("ownerId") references UserTable.id

    override val primaryKey = PrimaryKey(id, name = "PK_Project_ID")
}