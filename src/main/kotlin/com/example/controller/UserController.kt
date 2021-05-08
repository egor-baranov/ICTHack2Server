package com.example.controller

import com.example.dao.UserTable
import com.example.model.User
import com.example.model.enumerations.UserSpecialization
import org.jetbrains.exposed.sql.insert
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
            userObj[UserTable.password],
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