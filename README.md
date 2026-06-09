# CopsBoot

CopsBoot is a Kotlin Spring Boot REST API backend for a fictional police system. The project is built as a learning implementation of a backend API using Spring Boot, Spring Data JPA, Spring Security, validation, testing, and database persistence.

The system is designed to support police officers in their daily work by providing backend services for user management, authentication, authorization, and crime reporting.

## Project Overview

CopsBoot represents the backend side of a fictional mobile application used by police officers. Officers can use the system to report crimes, manage data, and interact with the server through REST API endpoints.

This repository follows the ideas from *Practical Guide to Building an API Back End with Spring Boot*, but the implementation is written in Kotlin instead of Java.

## Main Goals

* Build a clean REST API backend using Spring Boot
* Practice Kotlin with Spring Boot
* Implement user management and roles
* Use Spring Data JPA for database persistence
* Use Spring Security for authentication and authorization
* Write unit and integration tests
* Learn how to structure a real backend project

## Tech Stack

* Kotlin
* Spring Boot
* Spring Web MVC
* Spring Data JPA
* Spring Security
* Jakarta Validation
* H2 Database
* JUnit 5
* AssertJ
* Gradle Kotlin DSL

## Project Structure

```text
src
├── main
│   ├── kotlin
│   │   └── com.example.copsboot
│   │       ├── CopsbootApplication.kt
│   │       └── user
│   │           ├── User.kt
│   │           ├── UserRepository.kt
│   │           └── UserRole.kt
│   └── resources
│       └── application.properties
└── test
    └── kotlin
        └── com.example.copsboot
            ├── CopsbootApplicationTests.kt
            └── user
                └── UserRepositoryTest.kt
```

## Current Features

* User entity
* User roles
* User repository using Spring Data JPA
* Repository testing with `@DataJpaTest`
* In-memory H2 database for testing

## User Roles

The system currently supports three user roles:

| Role      | Description                                       |
| --------- | ------------------------------------------------- |
| `OFFICER` | A police officer who works in the field           |
| `CAPTAIN` | A supervisor or leader of officers                |
| `ADMIN`   | An administrative user with broader system access |

## Running the Project

To run the application:

```bash
./gradlew bootRun
```

On Windows:

```bash
gradlew.bat bootRun
```

The application will start on the default Spring Boot port:

```text
http://localhost:8080
```

## Running Tests

To run all tests:

```bash
./gradlew test
```

On Windows:

```bash
gradlew.bat test
```

## Database

The project currently uses H2 as an in-memory database for development and testing. This makes it easy to run tests without installing an external database.

## Notes

This project is still under development. More features will be added gradually, including REST controllers, security configuration, authentication, validation, and crime report management.

## Purpose

The main purpose of this repository is educational. It is used to practice building a maintainable backend API with Kotlin and Spring Boot while following a real project structure.
