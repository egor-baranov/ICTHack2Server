package com.example.routes

import com.example.controller.ReplyController
import io.ktor.application.*
import io.ktor.response.*
import io.ktor.routing.*

fun Application.configureReply() {
    routing {
        val replyController = ReplyController()

        route("/reply") {
            post("/add") {
                val parameters = call.parameters

                val reply = replyController.addReply(
                    text = parameters["text"] ?: "Nope",
                    projectId = (parameters["projectId"] ?: "0").toInt(),
                    authorId = (parameters["authorId"] ?: "0").toInt(),
                )

                call.respond(reply)
            }


            get("/list") {
                call.respond(replyController.list())
            }

            get("/getById") {
                val parameters = call.parameters
                call.respond(replyController.getById(parameters["id"]!!.toInt()))
            }

            get("/getByUserId"){
                val parameters = call.parameters
                call.respond(replyController.getListByUserId(parameters["id"]!!.toInt()))
            }

            get("/getByProjectId"){
                val parameters = call.parameters
                call.respond(replyController.getListByProjectId(parameters["id"]!!.toInt()))
            }

            post("/deny"){
                val parameters = call.parameters
                val id = parameters["id"]!!.toInt()
                replyController.deny(id)
                call.respond(replyController.getById(id))
            }

            post("/accept"){
                val parameters = call.parameters
                val id = parameters["id"]!!.toInt()
                replyController.accept(id)
                call.respond(replyController.getById(id))
            }
        }
    }
}