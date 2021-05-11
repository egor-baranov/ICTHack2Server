package com.example.controller

import com.example.dao.ProjectTable
import com.example.dao.ReplyTable
import com.example.dao.UsersToProjectsTable
import com.example.model.Reply
import com.example.model.enumerations.NotificationType
import com.example.model.enumerations.ReplyStatus
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.update

class ReplyController {
    private val notificationController = NotificationController()

    fun addReply(
        text: String,
        authorId: Int,
        projectId: Int,
        vacancy: String,
    ): Reply {
        return transaction {
            val replyObj = ReplyTable.insert {
                it[ReplyTable.text] = text
                it[ReplyTable.authorId] = authorId
                it[ReplyTable.projectId] = projectId
                it[ReplyTable.vacancy] = vacancy
                it[ReplyTable.status] = ReplyStatus.WAIT.toString()
            }

            notificationController.addNotification(replyObj[ReplyTable.authorId],
                "WAIT",
                "Ожидайте.",
                NotificationType.WAIT_REPLY,
                projectId,
                replyObj[ReplyTable.id]
            )

            notificationController.addNotification(ProjectTable.select { ProjectTable.id eq replyObj[ReplyTable.projectId] }
                .toList()[0][ProjectTable.ownerId],
                "WAIT",
                "Новый запрос от $authorId на ${
                    ProjectTable.select { ProjectTable.id eq projectId }.toList()[0][ProjectTable.name]
                }.",
                NotificationType.NEW_REPLY,
                replyObj[ReplyTable.projectId],
                replyObj[ReplyTable.id]
            )

            Reply(
                replyObj[ReplyTable.id],
                replyObj[ReplyTable.text],
                replyObj[ReplyTable.projectId],
                replyObj[ReplyTable.authorId],
                replyObj[ReplyTable.vacancy],
                ReplyStatus.valueOf(replyObj[ReplyTable.status])
            )
        }
    }

    fun list(): List<Reply> {
        return transaction {
            ReplyTable.selectAll().map { replyObj ->
                Reply(
                    replyObj[ReplyTable.id],
                    replyObj[ReplyTable.text],
                    replyObj[ReplyTable.projectId],
                    replyObj[ReplyTable.authorId],
                    replyObj[ReplyTable.vacancy],
                    ReplyStatus.valueOf(replyObj[ReplyTable.status]),
                )
            }
        }
    }

    fun getById(id: Int): Reply {
        return transaction {
            val replyObj = ReplyTable.select { ReplyTable.id eq id }.toList()[0]

            Reply(
                replyObj[ReplyTable.id],
                replyObj[ReplyTable.text],
                replyObj[ReplyTable.authorId],
                replyObj[ReplyTable.projectId],
                replyObj[ReplyTable.vacancy],
                ReplyStatus.valueOf(replyObj[ReplyTable.status]),
            )
        }
    }

    fun getListByUserId(id: Int): List<Reply> {
        return transaction {
            ReplyTable.select { ReplyTable.authorId eq id }.map { replyObj ->
                Reply(
                    replyObj[ReplyTable.id],
                    replyObj[ReplyTable.text],
                    replyObj[ReplyTable.projectId],
                    replyObj[ReplyTable.authorId],
                    replyObj[ReplyTable.vacancy],
                    ReplyStatus.valueOf(replyObj[ReplyTable.status]),
                )
            }
        }
    }

    fun getListByProjectId(id: Int): List<Reply> {
        return transaction {
            ReplyTable.select { ReplyTable.projectId eq id }.map { replyObj ->
                Reply(
                    replyObj[ReplyTable.id],
                    replyObj[ReplyTable.text],
                    replyObj[ReplyTable.projectId],
                    replyObj[ReplyTable.authorId],
                    replyObj[ReplyTable.vacancy],
                    ReplyStatus.valueOf(replyObj[ReplyTable.status]),
                )
            }
        }
    }

    fun deny(id: Int, text: String = "Вы не приняты.") {
        transaction {
            ReplyTable.update({ ReplyTable.id eq id }) {
                it[ReplyTable.status] = ReplyStatus.DENIED.toString()
            }

            val replyObj = ReplyTable.select { ReplyTable.id eq id }.toList()[0]

            notificationController.deleteByReplyId(id)
            notificationController.addNotification(
                replyObj[ReplyTable.authorId],
                "DENIED",
                text,
                NotificationType.DENIED_REPLY,
                replyObj[ReplyTable.projectId],
                replyObj[ReplyTable.id]
            )
        }
    }

    fun accept(id: Int, text: String = "Вы приняты.") {
        transaction {
            ReplyTable.update({ ReplyTable.id eq id }) {
                it[ReplyTable.status] = ReplyStatus.ACCEPTED.toString()
            }

            val replyObj = ReplyTable.select { ReplyTable.id eq id }.toList()[0]

            UsersToProjectsTable.insert {
                it[UsersToProjectsTable.userId] = replyObj[ReplyTable.authorId]
                it[UsersToProjectsTable.projectId] = replyObj[ReplyTable.projectId]
                it[UsersToProjectsTable.vacancy] = replyObj[ReplyTable.vacancy]
            }

            notificationController.deleteByReplyId(id)
            notificationController.addNotification(
                replyObj[ReplyTable.authorId],
                "ACCEPTED",
                text,
                NotificationType.ACCEPTED_REPLY,
                replyObj[ReplyTable.projectId],
                replyObj[ReplyTable.id]
            )
        }
    }
}