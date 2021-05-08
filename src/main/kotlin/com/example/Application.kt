package com.example

import com.example.dao.*
import com.example.plugins.configureRouting
import com.example.plugins.configureSecurity
import com.example.plugins.configureSerialization
import com.example.routes.configureNotifications
import com.example.routes.configureProject
import com.example.routes.configureReply
import com.example.routes.configureUser
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction

fun initDataBase() {
    val config = HikariConfig("/hikari.properties")
    val ds = HikariDataSource(config)
    Database.connect(ds)
}

fun createTables() {
    transaction {
        SchemaUtils.create(UserTable)
        SchemaUtils.create(ProjectTable)
        SchemaUtils.create(ReplyTable)
        SchemaUtils.create(UsersToProjectsTable)
        SchemaUtils.create(ProjectTagsTable)
        SchemaUtils.create(VacancyTable)
        SchemaUtils.create(RatingTable)
        SchemaUtils.create(NotificationTable)
    }
}

fun main() {
    initDataBase()
    createTables()
    embeddedServer(Netty, port = 8080, host = "0.0.0.0") {
        configureSerialization()
        configureUser()
        configureProject()
        configureReply()
        configureNotifications()
    }.start(wait = true)
}
