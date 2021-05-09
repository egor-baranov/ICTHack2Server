package com.example.controller

import com.example.dao.RatingTable
import com.example.dao.UserTable
import com.example.dao.UsersToProjectsTable
import com.example.model.User
import com.example.model.enumerations.UserSpecialization
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction

class UserController {
    private val replyController = ReplyController()

    fun userAdd(
        id: Int,
        password: String,
        firstName: String,
        lastName: String,
        profileDescription: String,
        githubProfileLink: String,
        tgId: String,
        specialization: UserSpecialization,
        rating: Float = 5.toFloat(),
    ): User {
        val userObj = transaction {
            UserTable.insert {
                it[UserTable.id] = id
                it[UserTable.password] = password
                it[UserTable.firstName] = firstName
                it[UserTable.lastName] = lastName
                it[UserTable.specialization] = specialization.toString()
                it[UserTable.profileDescription] = profileDescription
                it[UserTable.githubProfileLink] = githubProfileLink
                it[UserTable.tgLink] = tgId
            }
        }

        return User(userObj[UserTable.id],
            0.toFloat(),
            UserSpecialization.valueOf(userObj[UserTable.specialization]),
            userObj[UserTable.firstName],
            userObj[UserTable.lastName],
            userObj[UserTable.profileDescription],
            userObj[UserTable.githubProfileLink],
            mutableListOf(),
            mutableListOf(),
            userObj[UserTable.tgLink])
    }

    fun login(
        id: Int,
        password: String,
    ): User {
        return transaction {
            val userObj = UserTable.select { (UserTable.id eq id) and (UserTable.password eq password) }.toList()[0]
            var rates =
                RatingTable.select { RatingTable.userId eq userObj[UserTable.id] }.map { it[RatingTable.rate] }.toList()
            if (rates.isEmpty()) rates = listOf(0)
            User(userObj[UserTable.id],
                rates.sum().toFloat() / rates.size,
                UserSpecialization.IOS_DEVELOPER,
                userObj[UserTable.firstName],
                userObj[UserTable.lastName],
                userObj[UserTable.profileDescription],
                userObj[UserTable.githubProfileLink],
                UsersToProjectsTable.select { UsersToProjectsTable.userId eq userObj[UserTable.id] }
                    .map { it[UsersToProjectsTable.projectId] }.toMutableList(),
                replyController.getListByUserId(userObj[UserTable.id]).map { it.id }.toMutableList(),
                userObj[UserTable.tgLink])
        }
    }

    fun list(): List<User> {
        return transaction {
            UserTable.selectAll().map { userObj ->
                var rates =
                    RatingTable.select { RatingTable.userId eq userObj[UserTable.id] }.map { it[RatingTable.rate] }
                        .toList()
                if (rates.isEmpty()) rates = listOf(0)
                User(userObj[UserTable.id],
                    rates.sum().toFloat() / rates.size,
                    UserSpecialization.valueOf(userObj[UserTable.specialization]),
                    userObj[UserTable.firstName],
                    userObj[UserTable.lastName],
                    userObj[UserTable.profileDescription],
                    userObj[UserTable.githubProfileLink],
                    UsersToProjectsTable.select { UsersToProjectsTable.userId eq userObj[UserTable.id] }
                        .map { it[UsersToProjectsTable.projectId] }.toMutableList(),
                    replyController.getListByUserId(userObj[UserTable.id]).map { it.id }.toMutableList(),
                    userObj[UserTable.tgLink])
            }
        }
    }

    fun getById(
        id: Int,
    ): User {
        return transaction {
            val userObj = UserTable.select { UserTable.id eq id }.toList()[0]
            var rates =
                RatingTable.select { RatingTable.userId eq userObj[UserTable.id] }.map { it[RatingTable.rate] }.toList()
            if (rates.isEmpty()) rates = listOf(0)
            User(userObj[UserTable.id],
                rates.sum().toFloat() / rates.size,
                UserSpecialization.valueOf(userObj[UserTable.specialization]),
                userObj[UserTable.firstName],
                userObj[UserTable.lastName],
                userObj[UserTable.profileDescription],
                userObj[UserTable.githubProfileLink],
                UsersToProjectsTable.select { UsersToProjectsTable.userId eq userObj[UserTable.id] }
                    .map { it[UsersToProjectsTable.projectId] }.toMutableList(),
                replyController.getListByUserId(userObj[UserTable.id]).map { it.id }.toMutableList(),
                userObj[UserTable.tgLink])
        }
    }

    fun rateUser(id: Int, rate: Int): User {
        return transaction {
            RatingTable.insert {
                it[RatingTable.userId] = id
                it[RatingTable.rate] = rate
            }
            getById(id)
        }
    }
}