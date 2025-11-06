# genai-dll-poc

A small proof-of-concept project for Delhaize.

This repository uses Spring Boot with Liquibase for database migrations and Twilio for communication services.

## Checklist (what you'll do to run the project locally)

- Create a `.env` file from the provided example and fill in credentials.
- Create a MSsql database named `dllpoc` (see Database setup below).
- Set the active Spring profile (e.g. `local`, `dev` or `prod`).
- Run the application using the included Maven wrapper or from your IDE.

## Quick start
### 1) Create a `.env` file

If your repository contains a `.env.example`, make a copy manually or run:

```shell
copy .env.example .env
```

If `.env.example` is not present, create a file named `.env` in the project root and add the variables listed below.

#### Required environment variables

  - `DATASOURCE_URL` - JDBC URL.
  - `DATASOURCE_USERNAME` - DB username
  - `DATASOURCE_PASSWORD` - DB password
  - `TWILIO_ACCOUNT_SID`
  - `TWILIO_API_KEY`
  - `TWILIO_API_SECRET`
  - `TWILIO_APP_SID`

#### Sample `.env` / `.env.example`

```dotenv
DATASOURCE_URL=jdbc:sqlserver://localhost:1433;databaseName=dll-callcenter;encrypt=true;trustServerCertificate=true
DATASOURCE_USERNAME=dllpoc_user
DATASOURCE_PASSWORD=change_me

TWILIO_ACCOUNT_SID=ACxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
TWILIO_API_KEY=SKxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
TWILIO_API_SECRET=your_twilio_api_secret
TWILIO_APP_SID=APxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
```

---
### 2) Database setup (Microsoft SQL Server)

We keep detailed database setup instructions on [Confluence](https://e-3d-dc1.capgemini.com/confluence/display/DC0514/03+-+Local+development+Databases). Follow the internal guide for setting up Microsoft SQL Server and users.
You could also start a container with docker or podman by running:

```shell
docker compose -f compose.local.yml up -d
```

---
> **⚠️ IMPORTANT:** Don't forget this step

Create an MSSQL database named `dll-callcenter`. The project uses Liquibase to apply migrations automatically (on application startup).

---
**Connection string example:**
```
jdbc:sqlserver://localhost:1433;databaseName=dll-callcenter;encrypt=true;trustServerCertificate=true
```

---

### 3) Set the active Spring profile

The app provides `application-local.yml` and `application-dev.yml`. Choose one of: `local` or `dev`.

You can set the active profile in several ways:
- Via VM options in your IDE run configuration:
```
-Dspring.profiles.active=local
```
- Via active profile in your IDE run configuration (Only for IntelliJ Ultimate IDEA):
```
local
```
- Via JVM system property when running from the command line:

```shell
# using the Maven wrapper
mvnw.cmd spring-boot:run -Dspring-boot.run.profiles=local

# OR
mvnw.cmd spring-boot:run -Dspring.profiles.active=local
```

---
### 4) Run the application

Run `com.capgemini.dllpoc.DllpocApplication` from your IDE with the `local` profile active.

Or

From the project root run the following commands:

```shell
.\mvnw.cmd clean package
.\mvnw.cmd spring-boot:run -Dspring.profiles.active=local
```