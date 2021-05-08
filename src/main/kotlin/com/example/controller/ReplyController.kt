package com.example.controller

import com.example.dao.ReplyTable
import com.example.dao.UsersToProjectsTable
import com.example.model.Reply
import com.example.model.enumerations.ReplyStatus
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.update

class ReplyController {
    fun addReply(
        text: String,
        authorId: Int,
        projectId: Int,
        vacancy: String
    ): Reply {
        return transaction {
            val replyObj = ReplyTable.insert {
                it[ReplyTable.text] = text
                it[ReplyTable.authorId] = authorId
                it[ReplyTable.projectId] = projectId
                it[ReplyTable.vacancy] = vacancy
                it[ReplyTable.status] = ReplyStatus.WAIT.toString()
            }

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

    fun deny(id: Int) {
        transaction {
            val replyObj = ReplyTable.select { ReplyTable.id eq id }.toList()[0]
            replyObj[ReplyTable.status] = ReplyStatus.DENIED.toString()
        }
    }

    fun accept(id: Int) {
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
        }
    }
}