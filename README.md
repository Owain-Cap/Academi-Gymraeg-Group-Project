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
- MySQL
- Eclipse IDE


## Architecture

The project follows a standard layered Spring Boot structure:

config/        -> Security configuration (Spring Security)
models/        -> JPA entities (e.g. User)
repository/    -> Data access layer (JpaRepository interfaces)
service/       -> Business logic (UserService, authentication)
controller/    -> Web layer (HTTP endpoints)


## File Structure
<img width="1163" height="1353" alt="image" src="https://github.com/user-attachments/assets/515fedef-f0ad-41e5-8b6b-13adcd540d99" />


## class diagram overview
<img width="1278" height="1464" alt="image" src="https://github.com/user-attachments/assets/f119e5e1-c8fc-4be0-9078-1beb92e0fb9c" />


## sequence diagram
<img width="1536" height="1024" alt="image" src="https://github.com/user-attachments/assets/984f2055-181b-4eff-93a7-a8bde49a863e" />


## screen wireframes
<img width="2600" height="1142" alt="image" src="https://github.com/user-attachments/assets/f81b900f-fe6b-4be4-ad39-e2fe159810f9" />


## User Model
<img width="1483" height="1313" alt="image" src="https://github.com/user-attachments/assets/65f2365e-a7f2-4d5b-af83-74b5a77fab26" />


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


## Nouns
<img width="1136" height="868" alt="image" src="https://github.com/user-attachments/assets/7532658f-30bd-416f-b0ee-a45445c9eae2" />

Model:
models/Noun.java

Fields:
- nounId
- english
- welsh
- gender (MASCULINE or FEMININE)
- createdByUsername
- createdAt

Repository:
repository/NounRepository.java

Service:
service/NounService.java

Responsibilities:
- Create new nouns
- Edit existing nouns
- Delete nouns

Access:
- INSTRUCTOR and SYSTEM_ADMIN only (/nouns/**)

Initial data:
config/NounInitialiser.java
- Seeds 33 Welsh/English nouns on first run if the noun table is empty


## Test Logic
<img width="1887" height="1416" alt="image" src="https://github.com/user-attachments/assets/2535d124-f547-492a-8c0c-ff32c5d0ddeb" />


Entities:
- models/Test.java (a single test taken by a user)
- models/TestQuestions.java (individual questions belonging to a test)
- models/Question.java (question type enum: WELSH, ENGLISH, GENDER)

Service:
service/TestGeneratorService.java

Question types:
- WELSH → English to Welsh translation
- ENGLISH → Welsh to English translation
- GENDER → Gender of a Welsh noun (multiple choice)

Test composition:
- 20 questions per test
- 7 English to Welsh, 7 Welsh to English, 6 Gender
- Noun selection and question order are shuffled

Timer:
- 30 minute time limit per test
- On expiry any unanswered questions are marked SKIPPED and the test is auto-submitted

Answer checking:
- Exact match first
- Then a diacritic-tolerant check (e.g. "ty" will match "tŷ") with a reminder flag
- Gender questions require an exact MASCULINE or FEMININE answer

Anti-cheating:
- A user can only have one IN_PROGRESS test at a time
- Starting a test while one is in progress resumes the existing test
- Test history and review pages are blocked while a test is active (test-locked page)
- Browser caching of the test page is disabled to force a refresh on load

Persistence:
- Question text and correct answer are stored on TestQuestions
- Test results remain intact even if nouns or users are later deleted


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

/css/**, /images/**, /js/** → Public
/tests/**, /my-tests/** → STUDENT, INSTRUCTOR, SYSTEM_ADMIN
/nouns/** → INSTRUCTOR, SYSTEM_ADMIN
/admin/**, /register/** → SYSTEM_ADMIN only
/** → Authenticated users only

Spring automatically prefixes roles with ROLE_
Example: hasRole("SYSTEM_ADMIN") → ROLE_SYSTEM_ADMIN


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

Configured in:
src/main/resources/application.properties

Engine:
MySQL (via mysql-connector-j)

Schema management:
spring.jpa.hibernate.ddl-auto=update

Key tables:
- user (authentication details and role flags)
- noun (Welsh and English words with gender)
- test (test records per user)
- test_questions (questions belonging to a test)

<img width="1485" height="1080" alt="image" src="https://github.com/user-attachments/assets/dd569c79-aa4a-4063-9827-27caf1bb08af" />


## Creating Users (Important)

Users are created in two ways:

Auto-created on startup:
config/FirstUserAutoConfigurer.java
- Creates a default admin if one does not already exist
- Username: admin
- Password: password

Created by a System Administrator:
- Navigate to /register (SYSTEM_ADMIN only)
- Fill in the form to create a new user
- Edit or delete existing users from /admin


## Known Constraints

- Role system is flag-based (no separate role table)
- All users receive STUDENT role by default
- CSRF is disabled (development only)



## Development Notes

- Ensure controller routes match SecurityConfig
- Do not store plain-text passwords
- Do not enable in-memory users (overrides database authentication)
- Method-level security enabled via @EnableMethodSecurity





