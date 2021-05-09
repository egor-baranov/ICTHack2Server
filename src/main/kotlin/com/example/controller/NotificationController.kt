package com.example.controller

import com.example.dao.NotificationTable
import com.example.model.Notification
import com.example.model.enumerations.NotificationType
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction

class NotificationController {
    fun addNotification(
        toUserId: Int,
        title: String,
        text: String,
        type: NotificationType,
        replyId: Int,
    ): Notification {
        return transaction {
            val notificationObj = NotificationTable.insert {
                it[NotificationTable.toUserId] = toUserId
                it[NotificationTable.title] = title
                it[NotificationTable.type] = type.toString()
                it[NotificationTable.text] = text
                it[NotificationTable.replyId] = replyId
            }

            Notification(
                notificationObj[NotificationTable.id],
                notificationObj[NotificationTable.toUserId],
                notificationObj[NotificationTable.title],
                notificationObj[NotificationTable.text],
                NotificationType.valueOf(notificationObj[NotificationTable.type]),
                notificationObj[NotificationTable.replyId],
            )
        }
    }

    fun getUsersNotification(id: Int): List<Notification> {
        return transaction {
            NotificationTable.select { NotificationTable.toUserId eq id }.map { notificationObj ->
                Notification(
                    notificationObj[NotificationTable.id],
                    notificationObj[NotificationTable.toUserId],
                    notificationObj[NotificationTable.title],
                    notificationObj[NotificationTable.text],
                    NotificationType.valueOf(notificationObj[NotificationTable.type]),
                    notificationObj[NotificationTable.replyId],
                )
            }
        }
    }

    fun getByReplyId(id: Int): List<Notification> {
        return transaction {
            NotificationTable.select { NotificationTable.replyId eq id }.map { notificationObj ->
                Notification(
                    notificationObj[NotificationTable.id],
                    notificationObj[NotificationTable.toUserId],
                    notificationObj[NotificationTable.title],
                    notificationObj[NotificationTable.text],
                    NotificationType.valueOf(notificationObj[NotificationTable.type]),
                    notificationObj[NotificationTable.replyId],
                )
            }
        }
    }

    fun deleteById(id: Int) {
        transaction {
            NotificationTable.deleteWhere { NotificationTable.id eq id }
        }
    }

    fun deleteByReplyId(id: Int){
        transaction {
            NotificationTable.deleteWhere { NotificationTable.replyId eq id }
        }
    }
}