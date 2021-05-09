package com.example.dao

import org.jetbrains.exposed.sql.Table

object ProjectTagsTable : Table() {
    val projectId = integer("id") references ProjectTable.id
    val tag = text("tag")
}