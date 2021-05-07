package com.example.routes

import com.example.controller.replyList
import com.example.model.Reply
import io.ktor.application.*
import io.ktor.response.*
import io.ktor.routing.*

fun Application.configureReply() {
    routing {
        route("/reply") {
            post("/add") {
                val parameters = call.parameters

                val reply = Reply(
                    id = replyList.lastIndex + 2,
                    text = parameters["text"] ?: "Nope",
                    projectId = (parameters["projectId"] ?: "0").toInt(),
                    authorId = (parameters["githubProjectLink"] ?: "0").toInt(),
                )
                replyList.add(reply)

                call.respond(reply)
            }


            get("/list") {
                call.respond(replyList)
            }

            get("/getById") {
                val parameters = call.parameters
                call.respond(replyList.filter { it.id == parameters["id"]!!.toInt() }[0])
            }
        }
    }
}