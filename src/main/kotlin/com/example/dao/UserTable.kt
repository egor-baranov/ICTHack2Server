package com.example.dao

import org.jetbrains.exposed.sql.Table

object UserTable : Table() {
    val id = integer("id")
    val password = text("password")
    val rating = float("rating")
    val firstName = text("firstName")
    val specialization = text("specialization")
    val lastName = text("lastName")
    val profileDescription = text("profileDescription")
    val githubProfileLink = text("githubProfileLink")
    val tgLink = text("tgLink")

    override val primaryKey = PrimaryKey(id, name = "PK_User_ID")
}