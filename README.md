# Java Authentication Microservice

![Tech Stack](https://img.shields.io/badge/Backend-Spring%20Boot%203-green?style=flat-square&logo=springboot)
![Language](https://img.shields.io/badge/Language-Java%2021-orange?style=flat-square&logo=openjdk)
![Auth](https://img.shields.io/badge/Auth-JWT-purple?style=flat-square&logo=jsonwebtokens)
![Database](https://img.shields.io/badge/Database-PostgreSQL-blue?style=flat-square&logo=postgresql)

A stateless authentication microservice built with Spring Boot. Handles user registration, email verification, sign-in, password reset, and password changes — all secured with JWT.

---

## Features

- **Sign Up** — Register a new user account (password hashed with BCrypt)
- **Email Verification** — Confirm email via a tokenised link before gaining access
- **Sign In** — Authenticate and receive a signed JWT
- **Password Reset** — Request a reset link sent to the registered email
- **Change Password** — Submit a new password using a valid reset token

---

## Endpoints

| Method | Path | Description |
|--------|------|-------------|
| `POST` | `/api/auth/signup` | Register a new user |
| `GET` | `/api/auth/verify?token=` | Verify email address |
| `POST` | `/api/auth/signin` | Sign in and receive a JWT |
| `POST` | `/api/auth/reset` | Request a password reset email |
| `POST` | `/api/auth/changepassword?token=` | Change password using reset token |

---

## Tech Stack

| Layer | Technology |
|-------|-----------|
| Framework | Spring Boot 3.5 |
| Language | Java 21 |
| Authentication | JWT (JJWT 0.11.5) |
| Database | PostgreSQL |
| Password Hashing | BCrypt (Spring Security) |
| Email | Spring Mail (SMTP) |
| Validation | Jakarta Bean Validation |

---

## Getting Started

### Prerequisites

- Java 21+
- PostgreSQL database
- An SMTP account (e.g. Gmail App Password)

### Configuration

Copy the following into `src/main/resources/application.properties` and fill in your values:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/postgres
spring.datasource.username=<db-username>
spring.datasource.password=<db-password>

spring.mail.host=smtp.gmail.com
spring.mail.port=587
spring.mail.username=<your-email>
spring.mail.password=<your-app-password>

jwt.secret=<at-least-32-character-secret-key>
```

### Database

Create the users table before running:

```sql
CREATE TABLE authservice_users (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    email VARCHAR(255) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    status VARCHAR(50) NOT NULL DEFAULT 'PENDING_VERIFICATION'
);
```

### Run

```bash
./mvnw spring-boot:run
```

### Test

```bash
./mvnw test
```
