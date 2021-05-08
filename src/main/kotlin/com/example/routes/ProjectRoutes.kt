package com.example.routes

import com.example.controller.ProjectController
import com.example.model.enumerations.ProjectTags
import io.ktor.application.*
import io.ktor.response.*
import io.ktor.routing.*

fun Application.configureProject() {
    routing {
        val projectController = ProjectController()

        route("/projects") {
            post("/add") {
                val parameters = call.parameters

                val project = projectController.addProject(
                    name = parameters["name"] ?: "Nope",
                    description = parameters["description"] ?: "Nope",
                    githubProjectLink = parameters["githubProjectLink"] ?: "Nope",
                    tags = (parameters["tags"] ?: "").split(",").map { ProjectTags.valueOf(it) },
                    ownerId = (parameters["ownerId"] ?: "0").toInt()
                )

                call.respond(project)
            }


            get("/list") {
                call.respond(projectController.list())
            }

            get("/getById") {
                val parameters = call.parameters
                call.respond(projectController.getById(parameters["id"]!!.toInt()))
            }
        }
    }
}