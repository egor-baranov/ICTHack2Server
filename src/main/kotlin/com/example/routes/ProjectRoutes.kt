package com.example.routes

import com.example.controller.projectList
import com.example.controller.userList
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
                    ownerId = (parameters["ownerId"] ?: "0").toInt(),
                    tags = mutableListOf(),
                    replyIdList = mutableListOf()
                )


                val owner = userList.filter { it.id == project.ownerId }[0]
                owner.projectIdList.add(project.id)
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