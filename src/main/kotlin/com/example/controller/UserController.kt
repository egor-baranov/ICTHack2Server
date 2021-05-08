package com.example.controller

import com.example.dao.UserTable
import com.example.model.User
import com.example.model.enumerations.UserSpecialization
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction

class UserController {
    fun userAdd(
        id: Int,
        password: String,
        firstName: String,
        lastName: String,
        profileDescription: String,
        githubProfileLink: String,
        tgId: String,
        rating: Float = 5.toFloat(),
    ): User {
        val userObj = transaction {
            UserTable.insert {
                it[UserTable.id] = id
                it[UserTable.password] = password
                it[UserTable.firstName] = firstName
                it[UserTable.lastName] = lastName
                it[UserTable.rating] = rating
                it[UserTable.profileDescription] = profileDescription
                it[UserTable.githubProfileLink] = githubProfileLink
                it[UserTable.tgId] = tgId
            }
        }

        return User(userObj[UserTable.id],
            userObj[UserTable.rating],
            UserSpecialization.IOS_DEVELOPER,
            userObj[UserTable.firstName],
            userObj[UserTable.lastName],
            userObj[UserTable.profileDescription],
            userObj[UserTable.githubProfileLink],
            mutableListOf(),
            mutableListOf(),
            userObj[UserTable.tgId])
    }

    fun login(
        id: Int,
        password: String,
    ): User {
        return transaction {
            val userObj = UserTable.select { UserTable.id eq id; UserTable.password eq password }.toList()[0]
            User(userObj[UserTable.id],
                userObj[UserTable.rating],
                UserSpecialization.IOS_DEVELOPER,
                userObj[UserTable.firstName],
                userObj[UserTable.lastName],
                userObj[UserTable.profileDescription],
                userObj[UserTable.githubProfileLink],
                mutableListOf(),
                mutableListOf(),
                userObj[UserTable.tgId])
        }
    }

    fun list(): List<User> {
        return transaction {
            UserTable.selectAll().map { userObj ->
                User(userObj[UserTable.id],
                    userObj[UserTable.rating],
                    UserSpecialization.IOS_DEVELOPER,
                    userObj[UserTable.firstName],
                    userObj[UserTable.lastName],
                    userObj[UserTable.profileDescription],
                    userObj[UserTable.githubProfileLink],
                    mutableListOf(),
                    mutableListOf(),
                    userObj[UserTable.tgId])
            }
        }
    }

    fun getById(
        id: Int,
    ): User {
        return transaction {
            val userObj = UserTable.select { UserTable.id eq id}.toList()[0]
            User(userObj[UserTable.id],
                userObj[UserTable.rating],
                UserSpecialization.IOS_DEVELOPER,
                userObj[UserTable.firstName],
                userObj[UserTable.lastName],
                userObj[UserTable.profileDescription],
                userObj[UserTable.githubProfileLink],
                mutableListOf(),
                mutableListOf(),
                userObj[UserTable.tgId])
        }
    }
}