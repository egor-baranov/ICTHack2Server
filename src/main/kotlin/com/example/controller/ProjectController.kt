package com.example.controller

import com.example.dao.ProjectTable
import com.example.dao.ProjectTagsTable
import com.example.dao.UsersToProjectsTable
import com.example.model.Project
import com.example.model.enumerations.ProjectTags
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
        tags: List<ProjectTags>,
        ownerId: Int,
    ): Project {
        return transaction {
            val projectObj = ProjectTable.insert {
                it[ProjectTable.name] = name
                it[ProjectTable.description] = description
                it[ProjectTable.githubProjectLink] = githubProjectLink
                it[ProjectTable.ownerId] = ownerId
            }

            tags.forEach { tag ->
                ProjectTagsTable.insert {
                    it[ProjectTagsTable.projectId] = projectObj[ProjectTable.id]
                    it[ProjectTagsTable.tag] = tag.toString()
                }
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
                ProjectTagsTable.select { ProjectTagsTable.projectId eq projectObj[ProjectTable.id] }
                    .map { ProjectTags.valueOf(it[ProjectTagsTable.tag]) }.toMutableList(),
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
                    ProjectTagsTable.select { ProjectTagsTable.projectId eq projectObj[ProjectTable.id] }
                        .map { ProjectTags.valueOf(it[ProjectTagsTable.tag]) }.toMutableList(),
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
                ProjectTagsTable.select { ProjectTagsTable.projectId eq projectObj[ProjectTable.id] }
                    .map { ProjectTags.valueOf(it[ProjectTagsTable.tag]) }.toMutableList(),
                projectObj[ProjectTable.ownerId]
            )
        }
    }
}