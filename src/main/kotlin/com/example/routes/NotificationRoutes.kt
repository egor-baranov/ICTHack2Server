package com.example.routes

import com.example.controller.NotificationController
import io.ktor.application.*
import io.ktor.response.*
import io.ktor.routing.*

fun Application.configureNotifications() {
    routing {
        val notificationController = NotificationController()

        get("/notification/getUsersNotification") {
            val parameters = call.parameters
            call.respond(notificationController.getUsersNotification(parameters["id"]!!.toInt()))
        }
    }
}