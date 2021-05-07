package com.example.routes

import com.example.controller.projectList
import com.example.model.Project
import io.ktor.application.*
import io.ktor.response.*
import io.ktor.routing.*

fun Application.configureProject() {
    routing {
        route("/projects") {
            post("/add") {
                val parameters = call.parameters

                val project = Project(id = projectList.lastIndex + 2,
                    name = parameters["name"] ?: "Nope",
                    description = parameters["description"] ?: "Nope",
                    githubProjectLink = parameters["githubProjectLink"] ?: "Nope",
                    ownerId = 0,
                    tags = listOf(),
                    replyIdList = listOf()
                )
                projectList.add(project)

                call.respond(project)
            }


            get("/list") {
                call.respond(projectList)
            }

            get("/getById") {
                val parameters = call.parameters
                call.respond(projectList.filter { it.id == parameters["id"]!!.toInt() }[0])
            }
        }
    }
}