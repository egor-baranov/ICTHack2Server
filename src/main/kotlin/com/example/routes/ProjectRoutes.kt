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

                val vacancyMap = mutableMapOf<String, Int>()
                (parameters["vacancy"] ?: "").split(",").forEach {
                    vacancyMap[it.split(":")[0]] = it.split(":")[1].toInt()
                }

                val project = projectController.addProject(
                    name = parameters["name"] ?: "Nope",
                    description = parameters["description"] ?: "Nope",
                    githubProjectLink = parameters["githubProjectLink"] ?: "Nope",
                    tags = (parameters["tags"] ?: "").split(",").map { ProjectTags.valueOf(it) },
                    ownerId = (parameters["ownerId"] ?: "0").toInt(),
                    vacancyMap = vacancyMap
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

            get("/search") {
                val parameters = call.parameters
                if (parameters["tags"].isNullOrBlank()) {
                    call.respond(projectController.search(parameters["name"] ?: "Nope", setOf()))
                } else {
                    call.respond(projectController.search(parameters["name"] ?: "Nope",
                        parameters["tags"]!!.split(",").map { ProjectTags.valueOf(it) }.toSet()))
                }
            }

            get("/getByUserId"){
                val parameters = call.parameters
                call.respond(projectController.getByUserId(parameters["id"]!!.toInt()))
            }
        }
    }
}