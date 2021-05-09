# Server-side for ICTHack #2

## What is it?

Server-side kotlin application for ICTHack.

## What has already been implemented

Interfaces to work with project, users, replies from users to project and notifications.

## What is going to be implemented soon

Improvements of notifications. 

# Project architecture
```
├── Application.kt
├── controller
│   ├── NotificationController.kt
│   ├── ProjectController.kt
│   ├── ReplyController.kt
│   └── UserController.kt
├── dao
│   ├── NotificationTable.kt
│   ├── ProjectTable.kt
│   ├── ProjectTagsTable.kt
│   ├── RatingTable.kt
│   ├── ReplyTable.kt
│   ├── UsersToProjectsTable.kt
│   ├── UserTable.kt
│   └── VacancyTable.kt
├── model
│   ├── enumerations
│   │   ├── NotificationType.kt
│   │   ├── ProjectTags.kt
│   │   ├── ReplyStatus.kt
│   │   └── UserSpecialization.kt
│   ├── Notification.kt
│   ├── Project.kt
│   ├── Reply.kt
│   └── User.kt
├── plugins
│   ├── Routing.kt
│   ├── Security.kt
│   └── Serialization.kt
└── routes
    ├── NotificationRoutes.kt
    ├── ProjectRoutes.kt
    ├── ReplyRoutes.kt
    └── UserRoutes.kt
```
# List of possible requests (server API description)

## App requests

### Users

#### login

Request that is used to login user that is already been registered in system using phone or email as login and password.

Parameter | Description
----------|-------
id        | ISU id.
password  | Password of logged-in user.

#### register

Registration of new user with selected parameters.

Parameter          | Description
-------------------|-------
id                 | ISU id.
firstName          | First name of new user.
lastName           | First name of new user.
password           | Password of new user.  
specialization     | Specialization of new user.
profileDescription | Bio and other info about new user.
githubProfileLink  | Link to github of new user.
tgLink             | Link to telegram of new user.

#### list

Returns all the data about all users:
[
// list of registered users data
]
This request does not require any parameters.

#### Get by id

Returns all the data about user by id parameter.

### Projects

#### Add

Parameter          | Description
-------------------|-------
name               | Name of new project.
description        | Description of new project.
githubProjectLink  | Link to the new project.
ownerId            | Id of owner of new project.
vacancy            | List of vacancy of new project.
tags               | List of tags of new project.

#### List

Returns all the data about all Projects:
[
// list of Projects data
]

This request does not require any parameters.

#### Get by id

Returns all the data about project by id parameter.

#### Get By UId Copy

Returns all the data about projects by id parameter.

[
// list of Projects data
]

#### Search

Parameter          | Description
-------------------|-------
name               | Name to search.
tags               | List of tags in project.

### Reply

#### Add

Parameter          | Description
-------------------|-------
text               | User text in reply.
projectId          | Id of project of new reply.
authorId           | Author id of the new reply.
vacancy            | List of vacancy of new reply.

#### List

Returns all the data about all replies.

[
// list of Projects data
]

#### Get By Id

Returns all the data about replies by id parameter.

[
// list of Projects data
]

#### Get By UserId

Returns all the data about projects by id parameter.

[
// list of Projects data
]

#### Get By ProjectId

Returns all the data about projects by id parameter.

[
// list of Projects data
]

#### Accept

Accept reply by id.

#### Deny

Deny reply by id.

### Notifications Get By UserID

Return list on notifications by UserId.