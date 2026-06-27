# CopsBoot

CopsBoot is a completed full-stack learning project for a fictional police system.

The project includes:

* A Kotlin Spring Boot REST API backend
* OAuth2 password-login authentication
* Role-based user support
* Crime report creation
* Database persistence with Spring Data JPA
* Validation with Jakarta Validation
* A native Android frontend built with Kotlin and Jetpack Compose

The Android app allows an officer to log in, view the authenticated user profile, create crime reports, select an image, upload the report to the backend, and view the created report result.

## Project Overview

CopsBoot represents a fictional mobile system used by police officers. Officers can authenticate through the Android app and create crime reports that are submitted to the Spring Boot backend.

The backend exposes secured REST endpoints, handles authentication, validates report data, persists users and reports, and returns structured DTO responses to the frontend.

This project was built for educational purposes to practice building a maintainable backend API with Kotlin and Spring Boot, then connecting it to a real Android frontend.

## Features

### Backend Features

* Kotlin Spring Boot backend
* REST API architecture
* Spring Security configuration
* OAuth2 Authorization Server setup
* Password grant login flow for the mobile app
* Bearer token authentication
* Authenticated current-user endpoint
* User entity and strongly typed user IDs
* User roles such as officer, captain, and admin
* Spring Data JPA repositories
* Crime report entity
* Crime report creation endpoint
* Multipart crime report upload with image input
* Jakarta Validation for request validation
* Custom validation for report descriptions
* H2 database support for development and testing
* Unit and integration testing support
* Gradle Kotlin DSL build setup

### Android Features

* Native Android app inside the same repository
* Kotlin and Jetpack Compose UI
* Login screen
* Home screen
* Logout support
* Auth session storage in memory
* Retrofit and OkHttp networking
* OAuth2 token request
* Authenticated `/api/users/me` request
* Create crime report screen
* Image picker for report image upload
* Multipart form-data report submission
* Report validation before sending
* Created report details displayed after submission
* Scrollable report form UI
* Real-device backend testing using ADB reverse

## Tech Stack

### Backend

* Kotlin
* Spring Boot
* Spring Web MVC
* Spring Security
* Spring Authorization Server
* Spring Data JPA
* Jakarta Validation
* H2 Database
* JUnit 5
* AssertJ
* Gradle Kotlin DSL

### Android

* Kotlin
* Jetpack Compose
* Android ViewModel
* Kotlin Coroutines
* Retrofit
* OkHttp
* Gson
* Coil Compose
* Material 3

## Project Structure

```text
CopsBoot
├── android-app
│   └── app
│       └── src
│           └── main
│               └── java
│                   └── com.example.copsboot.android
│                       ├── api
│                       ├── data
│                       │   ├── auth
│                       │   └── report
│                       ├── model
│                       ├── navigation
│                       └── ui
│                           ├── home
│                           ├── login
│                           └── report
├── src
│   ├── main
│   │   ├── kotlin
│   │   │   └── com.example.copsboot
│   │   │       ├── infrastructure
│   │   │       ├── report
│   │   │       └── user
│   │   └── resources
│   └── test
├── build.gradle.kts
├── settings.gradle.kts
├── gradlew
├── gradlew.bat
└── README.md
```

## Main Backend Endpoints

### Authentication

```http
POST /oauth/token
```

Used by the Android app to log in and receive an access token.

### Current User

```http
GET /api/users/me
```

Returns the authenticated user.

### Crime Reports

```http
POST /api/reports
```

Creates a crime report for the authenticated officer.

The request is sent as `multipart/form-data` and includes:

```text
dateTime
description
trafficIncident
numberOfInvolvedCars
image
```

The backend validates the report before saving it.

## Crime Report Rules

A crime report requires:

* A date and time
* A description
* An image
* An authenticated user

Additional validation rules:

* The description must contain the word `suspect`
* If `trafficIncident` is true, `numberOfInvolvedCars` must be greater than `0`

The uploaded image is accepted by the backend request, but it is not persisted in the database in the current implementation.

## Running the Backend

From the repository root:

```bash
./gradlew bootRun --args="--spring.profiles.active=dev,local"
```

On Windows PowerShell:

```powershell
.\gradlew bootRun --args="--spring.profiles.active=dev,local"
```

The backend runs on:

```text
http://localhost:8080
```

## Running the Android App

Open this folder in Android Studio:

```text
android-app
```

Run the app on a real Android device or emulator.

For a real Android phone connected by USB, run this before testing backend calls:

```powershell
& "C:\Users\HP\AppData\Local\Android\Sdk\platform-tools\adb.exe" reverse tcp:8080 tcp:8080
```

This allows the Android phone to reach the local backend through:

```text
http://127.0.0.1:8080
```

## Android App Flow

```text
Login screen
↓
OAuth2 token request
↓
Fetch current user
↓
Home screen
↓
Create crime report
↓
Select image
↓
Submit multipart report
↓
Display created report details
```

## Running Tests

To run backend tests:

```bash
./gradlew test
```

On Windows:

```powershell
.\gradlew test
```

## Database

The project uses H2 for local development and testing.

The backend persists users and reports using Spring Data JPA.

## User Roles

The system supports these roles:

| Role      | Description                                       |
| --------- | ------------------------------------------------- |
| `OFFICER` | A police officer who works in the field           |
| `CAPTAIN` | A supervisor or leader of officers                |
| `ADMIN`   | An administrative user with broader system access |

## Status

This project is completed for its planned learning scope.

Completed parts include:

* Backend API
* Security configuration
* Authentication
* User management
* Crime report creation
* Validation
* Database persistence
* Android frontend
* Login flow
* Authenticated API calls
* Multipart crime report upload

## Purpose

The purpose of this repository is educational. It demonstrates how to build a Kotlin Spring Boot backend and connect it to a native Android frontend using real authentication, REST APIs, database persistence, validation, and multipart upload.
