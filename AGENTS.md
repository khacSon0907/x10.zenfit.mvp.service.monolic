**# AGENTS.md — ZenFit Development Guidelines
Environment Variables and Secrets
- Never read, print, dump, or expose the contents of .env files or other files containing secrets unless explicitly authorized and strictly necessary.
- Never include secret values in source code, logs, terminal output, API responses, documentation, or generated reports.
- Treat database credentials, MongoDB connection strings, Redis passwords, JWT secrets, API keys, OAuth client secrets, SMTP credentials, and payment gateway secrets as sensitive.
- You may inspect .env.example, configuration templates, and configuration code that use environment variables.
- When a task requires environment variable names, inspect the application configuration and .env.example instead of reading the actual .env file.
- Never modify, delete, overwrite, or rotate secrets in .env without explicit approval.
- Do not copy secret values into other files or send them to external services.
- If a secret is accidentally exposed, do not repeat it; notify the user and recommend rotating it when appropriate.

## 1. Technical Overview

### 1.1. Project Description

ZenFit is a web-based management system for personal trainers (PTs) and their internal team.

The system supports client management, PT scheduling, training progress tracking, workout programs, nutrition plans, PT session tracking, and revenue management.

### 1.2. Technology Stack

- **Programming Language:** Java 21
- **Backend Framework:** Spring Boot 3.5.0
- **Build Tool:** Maven
- **Architecture:** Monolithic application
- **Primary Database:** MongoDB
- **In-memory Data Store:** Redis

### 1.3. Architecture

ZenFit follows a monolithic architecture.

- Keep the application within the existing Spring Boot project.
- Organize code into clear modules and layers based on the existing project structure.
- Separate HTTP handling, business logic, data access, and domain models according to the established conventions.
- Reuse existing services and components whenever appropriate.
- Do not introduce microservices or split the application into independently deployed services without explicit approval.

### 1.4. Database and Data Storage

- Use MongoDB as the primary database.
- Use Redis for caching or other temporary data storage when appropriate to the existing implementation.
- Follow existing document structures, repository conventions, and data access patterns.
- Avoid introducing another database without explicit approval.
- Consider data consistency, concurrent updates, and duplicate requests when implementing business operations.

### 1.5. Dependency and Configuration Management

- Use Maven to manage project dependencies.
- Follow the versions defined in `pom.xml`.
- Do not upgrade Java, Spring Boot, or major dependencies without explicit approval.
- Store environment-specific configuration in the existing configuration files and environment variables as appropriate.
- Never hardcode credentials, database passwords, API keys, or other secrets in source code.

### 1.6. General Development Principles

- Inspect the existing codebase before implementing a feature.
- Follow the current project architecture, naming conventions, and coding style.
- Prefer simple, maintainable solutions over unnecessary abstractions.
- Avoid unrelated refactoring when implementing a feature.
- Verify changes with relevant tests and Maven build commands when possible.
- Never claim that tests or builds passed unless they were actually executed.

### 2. Project Structure and Architecture

### 2.1. Architectural Style

- ZenFit follows a monolithic architecture using Java 21 and Spring Boot.
- The application is organized into logical packages with separate responsibilities.
- Maintain clear boundaries between domain models, business logic, HTTP handling, and infrastructure.
- Do not introduce microservices or unrelated architectural patterns without explicit approval.
- Follow the existing package structure rather than creating a new architecture for each feature.

### 2.2. Package Responsibilities

The root package is:

`x10.zenfit.mvp.service`

#### `common.domain`

Contains shared domain models and data transfer objects.

- `dto/`: Shared DTOs used for data exchange where appropriate.
- `entities/`: Domain entities representing business concepts and business state.
- `enums/`: Shared domain enumerations.

Rules:
- Domain entities represent business concepts, not MongoDB documents.
- Do not add MongoDB-specific annotations or persistence concerns to domain entities unless explicitly required by an established design decision.
- Do not use domain entities directly as HTTP request or response models when dedicated DTOs are appropriate.
- Keep business rules in the appropriate domain or service layer according to the existing implementation.

#### `controller`

Handles incoming HTTP requests and outgoing HTTP responses.

- Validate request inputs using the project's established validation conventions.
- Delegate business operations to the service layer.
- Do not implement core business logic in controllers.
- Do not access MongoDB documents or repositories directly from controllers.
- Follow existing API response and error-handling conventions.

#### `service`

Contains application use cases and business operations, organized by feature.

Examples of existing feature areas include user, role, product, order, payment, revenue, notification, and other service packages.

- Keep business logic in the appropriate service implementation or use case.
- Follow existing interface and implementation conventions, such as `ICoreUserService` and `CoreUserServiceImpl`.
- Keep each use case focused on a clear business responsibility.
- Reuse existing business services instead of duplicating logic.
- Do not place database-specific mapping or infrastructure implementation details in business use cases.
- Avoid creating unnecessary interfaces or layers when the existing project conventions do not require them.

#### `infrastructure`

Contains technical implementations required by the application to interact with external systems and persistence mechanisms.

- Place database-specific persistence implementations and MongoDB document models here, following the existing structure.
- Place mappers that translate between domain entities and persistence documents here when consistent with the current implementation.
- Keep MongoDB-specific annotations and persistence concerns out of domain entities.
- Keep infrastructure details separate from core business rules.
- Follow existing repository interfaces and implementations rather than creating duplicate data access mechanisms.

#### `config`

Contains application and integration configuration.

Existing configuration areas include JWT, security, Redis, WebSocket, location, cloud services, PayOS, and role initialization.

- Keep configuration classes focused on configuring application components and integrations.
- Place third-party integration configuration and webhook-related configuration in the appropriate existing package.
- Keep business workflows out of configuration classes.
- Never hardcode credentials, secrets, or private keys.
- Inspect existing configuration before introducing a new bean or integration.

#### `share`

Contains shared error-handling and response-related components.

Existing components include business exceptions, error descriptors, common errors, and global exception handling.

- Reuse existing exception and error-response mechanisms.
- Keep feature-specific error definitions organized according to the existing package structure.
- Do not introduce a competing global exception-handling mechanism.
- Maintain consistent HTTP status codes and API error formats.

#### `util`

Contains small, reusable technical utilities, such as `CookieUtil`.

- Utilities should provide focused reusable operations.
- Do not use utility classes as a substitute for business services.
- Avoid adding stateful business workflows to utility classes.
- Reuse existing utilities where appropriate.

### 2.3. Domain Entities and Persistence Documents

ZenFit distinguishes business domain entities from database persistence documents.

- A domain entity represents a business concept and should not depend on MongoDB-specific persistence annotations.
- A persistence document represents the structure stored in MongoDB and may use Spring Data MongoDB annotations.
- Use explicit mapping between domain entities and persistence documents when required by the existing architecture.
- Place persistence documents and their mappers in the appropriate infrastructure packages.
- Do not pass persistence documents directly into business use cases or expose them through controllers.
- Do not create a second model representing the same concept without a clear responsibility and mapping strategy.
- Follow existing identifier types, field names, and mapping conventions.

Example conceptual flow:

`Controller -> Application Service / Use Case -> Repository Abstraction -> Infrastructure Implementation -> MongoDB`

The response follows the reverse direction through the appropriate mapping and DTO conversion.

### 2.4. Dependency and Layering Rules

- Controllers depend on application services, not persistence implementations.
- Business use cases should depend on abstractions for infrastructure operations where the existing architecture supports this pattern.
- Infrastructure implementations handle database and external-system details.
- Domain models should not depend on controllers, HTTP request objects, or MongoDB documents.
- Avoid circular dependencies between packages.
- Do not bypass established layers for convenience.
- Before creating or moving a class, inspect similar existing features and follow their conventions.

### 2.5. Change Management

Before implementing a feature:

1. Identify the relevant existing package and similar implementations.
2. Determine which layers need to change.
3. Preserve compatibility with existing APIs and stored documents.
4. Avoid unrelated refactoring.
5. Add or update tests where appropriate.
6. Report the files changed and verification performed.**