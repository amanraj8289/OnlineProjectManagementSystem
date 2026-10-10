# Online Project Management System

A web-based project management application built using Java, Spring Boot, Thymeleaf, and MySQL. It allows users to manage projects and tasks through a simple web interface.

## Features

- User login with password verification
- Dashboard displaying the logged-in user's name and role
- Create, view, update, and delete projects
- Create, view, update, and delete tasks
- Task status management: Pending, In Progress, and Completed
- MySQL database integration
- Responsive HTML and CSS interface

## Tech Stack

- **Backend:** Java, Spring Boot, JDBC
- **Frontend:** HTML, CSS, Thymeleaf
- **Database:** MySQL
- **Build Tool:** Maven
- **IDE:** IntelliJ IDEA

## Prerequisites

- JDK 17 or compatible Java installation
- Maven
- MySQL Server
- IntelliJ IDEA or another Java IDE

## Database Setup

1. Start your MySQL server.
2. Create the database:

   ```sql
   CREATE DATABASE project_management;
   ```

3. Configure the database connection using environment variables:

    - `DB_URL`: `jdbc:mysql://localhost:3306/project_management`
    - `DB_USER`: your MySQL username
    - `DB_PASSWORD`: your MySQL password

4. Create the required `users`, `projects`, and `tasks` tables with columns matching the application DAO classes.

## Run the Application

1. Clone or download this repository.
2. Open the project in IntelliJ IDEA.
3. Reload the Maven project.
4. Configure the database environment variables.
5. Run `ProjectManagementApplication.java`.
6. Open `http://localhost:8080/login` in your browser.

## Project Structure

```text
src/
  com/codenexus/projectmanagement/
    controller/
    dao/
    model/
    service/
    util/
  main/resources/templates/
    login.html
    dashboard.html
    projects.html
    tasks.html
    task-form.html
```

## Future Improvements

- Spring Security and role-based authorization
- User registration and user management
- Task assignment to team members
- Project-task relationships
- Search, filtering, and pagination
- Automated tests and deployment

## Author

Add your name and GitHub profile here.
