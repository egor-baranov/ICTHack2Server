package com.example.dao

import org.jetbrains.exposed.sql.Table

object RatingTable : Table() {
    val userId = integer("user id") references UserTable.id
    val rate = integer("rate")
}