# Security-
Spring Security Project
Overview
This project demonstrates Spring Boot Security with:

User registration and login
Role-based access control
Account lock after multiple failed attempts
Audit logging for login events


Features

Register new users with roles (USER, ADMIN)
Login with username and password
Role-based access for /api/admin/** and /api/user/**
Audit logs for login success, failure, and account lock
Password encryption using BCrypt


Database Schema
Tables
<img width="1920" height="1080" alt="Screenshot (50)" src="https://github.com/user-attachments/assets/2875a9a4-6145-41ff-a2dc-8caabacaaaf3" />


users

id (BIGINT, Primary Key)
username (VARCHAR)
password (VARCHAR)
failed_attempt (INT)
account_locked (BOOLEAN)



roles

id (BIGINT, Primary Key)
name (VARCHAR) → e.g., ROLE_USER, ROLE_ADMIN

<img width="1920" height="1080" alt="Screenshot (51)" src="https://github.com/user-attachments/assets/7ca21a03-df4f-4526-8f31-d6a793a80ef0" />


user_roles (Join Table)

user_id (BIGINT, Foreign Key)
role_id (BIGINT, Foreign Key)



audit_log

id (BIGINT, Primary Key)
username (VARCHAR)
action (VARCHAR) → e.g., LOGIN_SUCCESS, LOGIN_FAILED, ACCOUNT_LOCKED
timestamp (DATETIME)




🖼 Screenshot of Database Table
(Insert your screenshot here)
Example:
| id | username | password (hashed) | failed_attempt | account_locked |
|----|----------|--------------------|----------------|----------------|
| 1  | admin    | $2a$10$...        | 0              | false          |
| 2  | user1    | $2a$10$...        | 2              | false          |

<img width="1920" height="1080" alt="Screenshot (53)" src="https://github.com/user-attachments/assets/65fc05e0-76ad-4b00-a6cf-c50717564ee3" />

▶ How It Works

Register → /api/register
Login → /api/login-success
Logout → /api/logout-success
Dashboard → /api/dashboard
Role-based endpoints:

/api/admin/** → ADMIN only
/api/user/** → USER or ADMIN




🔐 Security Configuration

Public Endpoints: /api/register, /api/login-success, /api/logout-success, /api/dashboard
Role Restrictions:

/api/admin/** → ADMIN
/api/user/** → USER or ADMIN


Password Encoding: BCryptPasswordEncoder
Authentication: Custom UserDetailsService


🛠 Setup Instructions

Clone the repository:
ShellShow more lines

Configure application.properties:
Plain Textproperties isn’t fully supported. Syntax highlighting is based on Plain Text.spring.datasource.url=jdbc:mysql://localhost:3306/security_dbspring.datasource.username=rootspring.datasource.password=yourpasswordspring.jpa.hibernate.ddl-auto=updateShow more lines

Run the application:
Shellmvn spring-boot:runShow more lines

Access APIs via Postman or browser.








































MethodEndpointDescriptionPOST/api/registerRegister a new userPOST/api/login-successLogin userPOST/api/logout-successLogout userGET/api/dashboardDashboard messageGET/api/admin/**Admin-only endpointsGET/api/user/**User/Admin endpoints
