package com.example.controller

import com.example.dao.UserTable
import com.example.model.Project
import com.example.model.Reply
import com.example.model.User
import com.example.model.enumerations.ProjectTags
import com.example.model.enumerations.UserSpecialization
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.transactions.transaction

val userList = mutableListOf<User>()
val projectList = mutableListOf<Project>()
val replyList = mutableListOf<Reply>()

