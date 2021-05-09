package com.example.routes

import com.example.controller.UserController
import com.example.model.enumerations.UserSpecialization
import io.ktor.application.*
import io.ktor.response.*
import io.ktor.routing.*

fun Application.configureUser() {
    routing {
        val userController = UserController()

        route("/users") {
            post("/register") {
                val parameters = call.parameters

                val user = userController.userAdd(id = (parameters["id"] ?: "0").toInt(),
                    password = parameters["password"] ?: "Nope",
                    rating = 5.toFloat(),
                    firstName = parameters["firstName"] ?: "Nope",
                    lastName = parameters["lastName"] ?: "Nope",
                    profileDescription = parameters["profileDescription"] ?: "Nope",
                    githubProfileLink = parameters["githubProfileLink"] ?: "Nope",
                    specialization = if (parameters["specialization"].isNullOrEmpty()) UserSpecialization.WEB_BACKEND_DEVELOPER else UserSpecialization.valueOf(
                        parameters["specialization"]
                            ?: "WEB_BACKEND_DEVELOPER"),
                    tgId = parameters["tgLink"] ?: ""
                )

                call.respond(user)
            }

            post("/login") {
                val parameters = call.parameters
                call.respond(userController.login(parameters["id"]!!.toInt(), parameters["password"]!!))
            }


            get("/list") {
                call.respond(userController.list())
            }

            get("/getById") {
                val parameters = call.parameters
                call.respond(userController.getById(parameters["id"]!!.toInt()))
            }

            post("/rate") {
                val parameters = call.parameters
                call.respond(userController.rateUser(parameters["id"]!!.toInt(), parameters["rate"]!!.toInt()))
            }
        }
    }
}