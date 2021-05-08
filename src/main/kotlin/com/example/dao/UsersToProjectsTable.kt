package com.example.dao

import org.jetbrains.exposed.sql.*

object UsersToProjectsTable : Table() {
    val projectId = integer("projectId") references ProjectTable.id
    val userId = integer("userId") references UserTable.id
}