# Server-side for ICTHack #2

## What is it?

## What has already been implemented

## What is going to be implemented soon

# Project architecture
│   main.py
│   README.md
│   requirements.txt
│
├───components
│   │   config.py
│   │   core.py
│   │
│   ├───database
│   │   │   DatabaseWorker.py
│   │   │
│   │   └───data
│   │           blocked_users.json
│   │           comments.json
│   │           users.json
│   │           videos.json
│   │    
│   └───managers
│           CommentManager.py
│           UserManager.py
│           VideoManager.py
│
├───dto
│       Comment.py
│       User.py
│       Video.py
│    
├───lib
│       smsc_api.py
│
├───routes
│   ├───admin_panel
│   │       admin_requests.py
│   │       user_editing.py
│   │
│   └───app_requests
│           data_list.py
│           regular_requests.py
│       
└───tests
test_dbworker.py

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

Returns all the data about users by id parameter.

### Projects

#### Add

Parameter          | Description
-------------------|-------
name               | Name of new project.
description        | Description of new project.
githubProjectLink  | Link to the new project.
ownerId            | Id of owner of new project.
tags               | List of tags of new user.

Returns all the data about all users:
[
// list of registered users data
]

This request does not require any parameters.
