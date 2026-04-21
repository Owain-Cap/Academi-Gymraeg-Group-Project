# Academi-Gymraeg-Group-Project

## Overview
Academi Gymraeg is a Spring Boot web application designed to support Welsh language learning.  
The system implements role-based access control and allows users to interact with tests and learning content based on their permissions.


## Tech Stack

- Java 17+
- Spring Boot
- Spring Security
- Spring Data JPA 
- Maven
- (Whichever database we used)
- Eclipse IDE



## Architecture

The project follows a standard layered Spring Boot structure:

config/        -> Security configuration (Spring Security)  
models/        -> JPA entities (e.g. User)  
repository/    -> Data access layer (JpaRepository interfaces)  
service/       -> Business logic (UserService, authentication)  
controller/    -> Web layer (HTTP endpoints)  



## Security Implementation

### Authentication
- Implemented via UserDetailsService (DatabaseUserDetailsService)
- Users are loaded from the database using UserRepository
- Passwords are stored using BCrypt hashing

### Authorization (Role-Based)

Roles are dynamically assigned based on flags in the User entity:

- ROLE_STUDENT (default for all users)
- ROLE_INSTRUCTOR (if isInstructor == YES)
- ROLE_SYSTEM_ADMIN (if isAdmin == YES)

Roles are constructed in:
service/DatabaseUserDetailsService.java



## Security Configuration

Defined in:
config/SecurityConfig.java

### Route Protection

/css/**, /js/** → Public  
/test/** → STUDENT, INSTRUCTOR, ADMIN  
/take-test/** → STUDENT, INSTRUCTOR, ADMIN  
/noun/edit/** → INSTRUCTOR, ADMIN  
/test/create/** → INSTRUCTOR, ADMIN  
/admin/** → SYSTEM_ADMIN only  
/** → Authenticated users only  

Spring automatically prefixes roles with ROLE_  
Example: hasRole("SYSTEM_ADMIN") → ROLE_SYSTEM_ADMIN  



## User Model

Defined in:
models/User.java

Role control is handled via:

private IsInstructor isInstructor;  
private IsAdmin isAdmin;  



## User Persistence

Repository:
repository/UserRepository.java  

Key method:
Optional<User> findByUsername(String username);  



## User Service

Defined in:
service/UserService.java  

Responsibilities:
- Create/update users  
- Ensure passwords are BCrypt-encoded before saving  
- Ensure role flags are not null  



## Running the Application (Eclipse)

1. Import Project  
- Open Eclipse  
- File → Import  
- Select "Existing Maven Project"  
- Choose the project root  

2. Build Project  
- Right-click project → Maven → Update Project  

3. Run Application  
- Locate the main class annotated with @SpringBootApplication  
- Right-click → Run As → Spring Boot App 

4. Access Application  
http://localhost:8080  



## Database

//Leaving blank for now



## Creating Users (Important)
//leaving blank for now




## Known Constraints

- Role system is flag-based (no separate role table)  
- All users receive STUDENT role by default  
- CSRF is disabled (development only)  



## Development Notes

- Ensure controller routes match SecurityConfig  
- Do not store plain-text passwords  
- Do not enable in-memory users (overrides database authentication)  
- Method-level security enabled via @EnableMethodSecurity  

