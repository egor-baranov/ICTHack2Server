package com.example.controller

import com.example.dao.ProjectTable
import com.example.dao.ProjectTagsTable
import com.example.dao.UsersToProjectsTable
import com.example.dao.VacancyTable
import com.example.model.Project
import com.example.model.enumerations.ProjectTags
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction

class ProjectController {
    private val replyController = ReplyController()

    private fun getVacancyMap(id: Int): MutableMap<String, Int> {
        val vacancyMap = mutableMapOf<String, Int>()
        VacancyTable.select { VacancyTable.projectId eq id }.forEach { row ->
            vacancyMap[row[VacancyTable.type]] = row[VacancyTable.count]
        }
        return vacancyMap
    }

    private fun getFreeVacancy(id: Int): MutableMap<String, Int> {
        val vacancyMap = getVacancyMap(id)
        for (resultRow in UsersToProjectsTable.select { UsersToProjectsTable.projectId eq id }) {
            if (resultRow[UsersToProjectsTable.vacancy] in vacancyMap) {
                vacancyMap[resultRow[UsersToProjectsTable.vacancy]] =
                    vacancyMap[resultRow[UsersToProjectsTable.vacancy]]!! - 1
            }
        }
        return vacancyMap
    }

    fun addProject(
        name: String,
        description: String,
        githubProjectLink: String,
        tags: List<ProjectTags>,
        ownerId: Int,
        vacancyMap: MutableMap<String, Int>,
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

            for (vacancy in vacancyMap) {
                VacancyTable.insert {
                    it[VacancyTable.projectId] = projectObj[ProjectTable.id]
                    it[VacancyTable.type] = vacancy.key
                    it[VacancyTable.count] = vacancy.value
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
                projectObj[ProjectTable.ownerId],
                listOf(),
                vacancyMap,
                vacancyMap
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
                    projectObj[ProjectTable.ownerId],
                    UsersToProjectsTable.select {
                        (UsersToProjectsTable.projectId eq projectObj[ProjectTable.id]) and
                                (UsersToProjectsTable.userId neq projectObj[ProjectTable.ownerId])
                    }.map { it[UsersToProjectsTable.userId] },
                    getVacancyMap(projectObj[ProjectTable.id]),
                    getFreeVacancy(projectObj[ProjectTable.id])
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
                projectObj[ProjectTable.ownerId],
                UsersToProjectsTable.select {
                    (UsersToProjectsTable.projectId eq projectObj[ProjectTable.id]) and
                            (UsersToProjectsTable.userId neq projectObj[ProjectTable.ownerId])
                }.map { it[UsersToProjectsTable.userId] },
                getVacancyMap(projectObj[ProjectTable.id]),
                getFreeVacancy(projectObj[ProjectTable.id])
            )
        }
    }

    fun search(value: String, tags: Set<ProjectTags>): List<Project> {
        return transaction {
            value.split(" ")
            ProjectTable.select { ProjectTable.name like "%$value%" }.toList().sortedWith { a, b ->
                when {
                    getFreeVacancy(a[ProjectTable.id]).values.sum() == getFreeVacancy(b[ProjectTable.id]).values.sum() -> {
                        val aTags = ProjectTagsTable.select { ProjectTagsTable.projectId eq a[ProjectTable.id] }
                            .map { ProjectTags.valueOf(it[ProjectTagsTable.tag]) }.toSet().intersect(tags).size
                        val bTags = ProjectTagsTable.select { ProjectTagsTable.projectId eq b[ProjectTable.id] }
                            .map { ProjectTags.valueOf(it[ProjectTagsTable.tag]) }.toSet().intersect(tags).size
                        when {
                            aTags == bTags -> 0
                            aTags > bTags -> -1
                            else -> 1
                        }
                    }
                    getFreeVacancy(a[ProjectTable.id]).values.sum() > getFreeVacancy(b[ProjectTable.id]).values.sum() -> -1
                    else -> 1
                }
            }.map { projectObj ->
                Project(
                    projectObj[ProjectTable.id],
                    projectObj[ProjectTable.name],
                    projectObj[ProjectTable.description],
                    replyController.getListByProjectId(projectObj[ProjectTable.id]).map { it.id }.toMutableList(),
                    projectObj[ProjectTable.githubProjectLink],
                    ProjectTagsTable.select { ProjectTagsTable.projectId eq projectObj[ProjectTable.id] }
                        .map { ProjectTags.valueOf(it[ProjectTagsTable.tag]) }.toMutableList(),
                    projectObj[ProjectTable.ownerId],
                    UsersToProjectsTable.select {
                        (UsersToProjectsTable.projectId eq projectObj[ProjectTable.id]) and
                                (UsersToProjectsTable.userId neq projectObj[ProjectTable.ownerId])
                    }.map { it[UsersToProjectsTable.userId] },
                    getVacancyMap(projectObj[ProjectTable.id]),
                    getFreeVacancy(projectObj[ProjectTable.id])
                )
            }
        }
    }

    fun getByUserId(id: Int): List<Project> {
        return transaction {
            ProjectTable.select { ProjectTable.ownerId eq id }.map { projectObj ->
                Project(
                    projectObj[ProjectTable.id],
                    projectObj[ProjectTable.name],
                    projectObj[ProjectTable.description],
                    replyController.getListByProjectId(projectObj[ProjectTable.id]).map { it.id }.toMutableList(),
                    projectObj[ProjectTable.githubProjectLink],
                    ProjectTagsTable.select { ProjectTagsTable.projectId eq projectObj[ProjectTable.id] }
                        .map { ProjectTags.valueOf(it[ProjectTagsTable.tag]) }.toMutableList(),
                    projectObj[ProjectTable.ownerId],
                    UsersToProjectsTable.select {
                        (UsersToProjectsTable.projectId eq projectObj[ProjectTable.id]) and
                                (UsersToProjectsTable.userId neq projectObj[ProjectTable.ownerId])
                    }.map { it[UsersToProjectsTable.userId] },
                    getVacancyMap(projectObj[ProjectTable.id]),
                    getFreeVacancy(projectObj[ProjectTable.id])
                )
            }
        }
    }
}