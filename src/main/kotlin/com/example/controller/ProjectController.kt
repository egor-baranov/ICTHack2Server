package com.example.controller

import com.example.dao.ProjectTable
import com.example.dao.UsersToProjectsTable
import com.example.model.Project
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction

class ProjectController {
    private val replyController = ReplyController()


    fun addProject(
        name: String,
        description: String,
        githubProjectLink: String,
        ownerId: Int,
    ): Project {
        return transaction {
            val projectObj = ProjectTable.insert {
                it[ProjectTable.name] = name
                it[ProjectTable.description] = description
                it[ProjectTable.githubProjectLink] = githubProjectLink
                it[ProjectTable.ownerId] = ownerId
            }

            UsersToProjectsTable.insert {
                it[UsersToProjectsTable.userId] = ownerId
                it[UsersToProjectsTable.projectId] = projectObj[ProjectTable.id]
            }

            Project(
                projectObj[ProjectTable.id],
                projectObj[ProjectTable.name],
                projectObj[ProjectTable.description],
                mutableListOf(),
                projectObj[ProjectTable.githubProjectLink],
                mutableListOf(),
                projectObj[ProjectTable.ownerId]
            )
        }
    }

    fun list(): List<Project> {
        return transaction {
            ProjectTable.selectAll().map { projectObj ->
                Project(
                    projectObj[ProjectTable.id],
                    projectObj[ProjectTable.name],
                    projectObj[ProjectTable.description],
                    replyController.getListByProjectId(projectObj[ProjectTable.id]).map { it.id }.toMutableList(),
                    projectObj[ProjectTable.githubProjectLink],
                    mutableListOf(),
                    projectObj[ProjectTable.ownerId]
                )
            }
        }
    }

    fun getById(id: Int): Project {
        return transaction {
            val projectObj = ProjectTable.select { ProjectTable.id eq id }.toList()[0]

            Project(
                projectObj[ProjectTable.id],
                projectObj[ProjectTable.name],
                projectObj[ProjectTable.description],
                replyController.getListByProjectId(projectObj[ProjectTable.id]).map { it.id }.toMutableList(),
                projectObj[ProjectTable.githubProjectLink],
                mutableListOf(),
                projectObj[ProjectTable.ownerId]
            )
        }
    }
}