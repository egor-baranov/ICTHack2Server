package com.example

import com.example.plugins.configureRouting
import com.example.plugins.configureSecurity
import com.example.plugins.configureSerialization
import com.example.routes.configureProject
import com.example.routes.configureReply
import com.example.routes.configureUser
import io.ktor.server.engine.*
import io.ktor.server.netty.*

fun main() {
    embeddedServer(Netty, port = 8080, host = "0.0.0.0") {
        configureRouting()
        configureSecurity()
        configureSerialization()
        configureUser()
        configureProject()
        configureReply()
    }.start(wait = true)
}
