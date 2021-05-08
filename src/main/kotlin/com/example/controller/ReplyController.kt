package com.example.controller

import com.example.dao.ReplyTable
import com.example.model.Reply
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction

class ReplyController {
    fun addReply(
        text: String,
        authorId: Int,
        projectId: Int,
    ): Reply {
        return transaction {
            val replyObj = ReplyTable.insert {
                it[ReplyTable.text] = text
                it[ReplyTable.authorId] = authorId
                it[ReplyTable.projectId] = projectId
            }

            Reply(
                replyObj[ReplyTable.id],
                replyObj[ReplyTable.text],
                replyObj[ReplyTable.authorId],
                replyObj[ReplyTable.projectId],
            )
        }
    }

    fun list(): List<Reply> {
        return transaction {
            ReplyTable.selectAll().map { replyObj ->
                Reply(
                    replyObj[ReplyTable.id],
                    replyObj[ReplyTable.text],
                    replyObj[ReplyTable.authorId],
                    replyObj[ReplyTable.projectId],
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
            )
        }
    }

    fun getListByUserId(id: Int): List<Reply> {
        return transaction {
            ReplyTable.select { ReplyTable.authorId eq id }.map { replyObj ->
                Reply(
                    replyObj[ReplyTable.id],
                    replyObj[ReplyTable.text],
                    replyObj[ReplyTable.authorId],
                    replyObj[ReplyTable.projectId],
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
                    replyObj[ReplyTable.authorId],
                    replyObj[ReplyTable.projectId],
                )
            }
        }
    }
}