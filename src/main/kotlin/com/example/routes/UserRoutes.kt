package com.example.routes

import io.ktor.routing.*
import io.ktor.http.*
import io.ktor.application.*
import io.ktor.response.*
import io.ktor.request.*

fun Application.configureRouting() {

    install(Routing) {
        route("/user") {
            get("/") {

                call.respondText("Hello World!")
            }
        }
    }
}