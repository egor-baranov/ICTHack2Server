package com.example.dao

import org.jetbrains.exposed.sql.Table

object VacancyTable : Table() {
    val projectId = integer("projectId") references ProjectTable.id
    val type = text("vacancy type")
    val count = integer("count of necessary people")
}