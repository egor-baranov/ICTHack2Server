package com.example.routes

import com.example.controller.userList
import com.example.model.User
import com.example.model.enumerations.UserSpecialization
import io.ktor.serialization.*
import io.ktor.features.*
import io.ktor.application.*
import io.ktor.response.*
import io.ktor.routing.*
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

fun Application.configureUser() {
    routing {
        route("/users") {
            post("/register") {
                val parameters = call.parameters

                val user = User(id = (parameters["userName"] ?: "0").toInt(),
                    userName = parameters["userName"] ?: "Nope",
                    password = parameters["password"] ?: "Nope",
                    rating = 5.toFloat(),
                    specialization = UserSpecialization.IOS_DEVELOPER,
                    firstName = parameters["firstName"] ?: "Nope",
                    lastName = parameters["lastName"] ?: "Nope",
                    profileDescription = parameters["profileDescription"] ?: "Nope",
                    githubProfileLink = parameters["githubProfileLink"] ?: "Nope",
                    projectIdList = listOf(),
                    replyIdList = listOf(),
                    tgId = parameters["firstName"] ?: ""
                )
                userList.add(user)

                call.respond(user)
            }

            post("/login"){

            }


            get("/list") {
                call.respond(userList)
            }

            get("/getById") {
                val parameters = call.parameters
                call.respond(userList.filter { it.id == parameters["id"]!!.toInt() }[0])
            }
        }
    }
}