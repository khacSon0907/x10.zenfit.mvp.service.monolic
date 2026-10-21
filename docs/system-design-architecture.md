# ZenFit — System Design & Architecture

> **Purpose:** This document defines the architectural direction, boundaries, dependency rules, and implementation conventions for the ZenFit backend. It is intended for developers and AI coding agents working on the project.
>
> **Architecture style:** Clean Onion Architecture, implemented as a modular monolith.
>
> **Important:** This document describes architectural rules and intended boundaries. It does not prove that every listed package, component, or feature already exists. Before changing code, inspect the current source tree and `pom.xml`, then follow the actual implementation where it is consistent with these rules. Do not perform a broad migration solely to make the code match this document.

---

## 1. System Overview

### 1.1 Product context

ZenFit is a web-based operations and management system for personal trainers (PTs) and their internal team. Its purpose is to help the team manage clients and training operations from one system.

The system's business areas include:

- User authentication, authorization, and internal account management.
- Client profiles and client lifecycle management.
- PT schedules and calendar management.
- Training-session check-in and remaining-session tracking.
- Training progress, workout programs, and nutrition plans.
- Package renewal and operational reminders.
- Revenue and related management reports, according to implemented requirements.

**Access boundary:** Clients are managed as records by internal users. A client is not a system user and must not be given access to the internal application unless an explicit product decision changes this requirement.

### 1.2 Architecture decisions

| Concern | Decision |
|---|---|
| Application style | Modular monolith |
| Architectural pattern | Clean Onion Architecture |
| Language | Java 21, subject to the version configured in the project |
| Framework | Spring Boot, subject to the version configured in `pom.xml` |
| Build | Maven |
| Primary persistence | MongoDB, following the current project configuration |
| Temporary/cache storage | Redis where already configured and appropriate |
| Deployment topology | One deployable backend unless explicitly approved otherwise |

Do not introduce microservices, another database, a message broker, a new framework, or a new deployment unit without explicit approval and a documented reason.

### 1.3 Architectural goals

1. Keep business rules independent of Spring MVC, MongoDB, Redis, and external providers.
2. Make dependencies point inward toward business policy.
3. Keep each feature cohesive and avoid unnecessary cross-feature coupling.
4. Preserve current API contracts and persisted-data compatibility unless a change is approved.
5. Make important business behavior testable without starting the entire application.
6. Prefer the simplest design that satisfies current requirements; do not add abstractions without a concrete need.

---

## 2. Clean Onion Architecture

### 2.1 Conceptual layers

The architecture is described using four logical layers. The names are conceptual and do not require a large package rename if the existing code uses different names.

```text
┌──────────────────────────────────────────────────────┐
│ Presentation / Delivery                              │
│ REST controllers, request/response DTOs, HTTP mapping │
├──────────────────────────────────────────────────────┤
│ Infrastructure / Adapters                            │
│ MongoDB documents, persistence adapters, Redis,      │
│ email, payment, file storage, WebSocket integrations  │
├──────────────────────────────────────────────────────┤
│ Application                                          │
│ Use cases, application services, input/output ports, │
│ transaction and workflow coordination                │
├──────────────────────────────────────────────────────┤
│ Domain                                               │
│ Entities, value objects, domain rules, domain errors │
└──────────────────────────────────────────────────────┘

Dependency direction: outer layers → inner layers.
Domain must not depend on Application, Infrastructure, or Presentation.
Application may depend on Domain, but not on concrete outer-layer adapters.
```

This diagram shows logical responsibilities, not a requirement that every class must have a separate interface or package.

### 2.2 Domain layer

**Responsibility:** Express business concepts, invariants, and rules without depending on web or persistence technologies.

May contain:

- Domain entities and value objects.
- Domain enums where they represent business concepts.
- Domain-level validation and business invariants.
- Domain exceptions or error types when they represent business failures.
- Domain services only when a rule genuinely spans multiple domain objects and does not naturally belong to one entity.

Must not contain:

- Spring MVC annotations or HTTP request/response objects.
- MongoDB persistence annotations such as `@Document` or database-specific repository code when following the separated-persistence model.
- Redis, SMTP, payment-provider, or cloud-storage clients.
- Framework-specific configuration.

Rules:

- Keep business rules close to the domain concept they protect.
- Do not use a domain entity as an HTTP request or response DTO.
- Do not create duplicate models for the same concept without a clear responsibility and mapping strategy.
- If the current implementation intentionally combines a domain model and a persistence document, do not split it automatically. Propose the migration and its benefits/costs first.

### 2.3 Application layer

**Responsibility:** Orchestrate use cases and coordinate domain objects through abstractions.

May contain:

- Application services and use cases.
- Command/query input models when useful.
- Input ports and output ports where they clarify boundaries.
- Repository or gateway interfaces owned by the application/domain boundary.
- Mapping and orchestration that are part of a use case.
- Authorization checks that depend on the current actor and use-case policy, while technical authentication remains in the security adapter.

Rules:

- Each use case should have a clear business purpose and a focused responsibility.
- Application services coordinate; they should not become giant classes containing every feature's logic.
- Depend on interfaces for persistence and external capabilities rather than constructing concrete adapters.
- Do not return MongoDB documents or persistence-specific types from application use cases.
- Do not put HTTP status codes or `ResponseEntity` in the application layer.
- Do not access a repository implementation directly from a controller.
- Avoid creating an interface for every class by habit. Create ports where they protect an architectural boundary, enable meaningful testing, or support a real alternative implementation.

### 2.4 Infrastructure / adapter layer

**Responsibility:** Implement technical details and connect the application to external systems.

May contain:

- MongoDB documents and Spring Data MongoDB repositories.
- Implementations of repository/output ports.
- Mappers between domain objects and persistence documents.
- Redis adapters and cache implementations.
- Email/OTP delivery adapters.
- Payment, file-storage, location, or other external-service adapters where they are actually used.
- WebSocket/STOMP integration details.
- Framework-specific configuration required by these adapters.

Rules:

- Keep database annotations and persistence mapping here when using the separated-persistence model.
- Convert persistence models to/from domain models at the adapter boundary.
- Do not put business decisions in a MongoDB repository implementation or external-service client.
- Treat external responses as untrusted input; validate and normalize them before use.
- Handle timeouts, failures, retries, and idempotency according to the operation's risk. Do not add blind retries to non-idempotent operations.
- Keep provider-specific request/response types from leaking into domain models.

### 2.5 Presentation / delivery layer

**Responsibility:** Translate transport-level input into application calls and translate results into API responses.

May contain:

- REST controllers and endpoint definitions.
- HTTP request/response DTOs.
- Request validation annotations.
- Authentication/authorization integration at the HTTP boundary.
- Mapping application results and errors to the established API response format.

Rules:

- Controllers should be thin: validate transport input, call a use case, and return the mapped result.
- Do not put business workflows in controllers.
- Do not call MongoDB or Redis directly from controllers.
- Do not expose domain or persistence objects directly when dedicated response DTOs are appropriate.
- Keep HTTP status codes and protocol-specific behavior at the presentation boundary.
- Follow the existing global exception handler and response envelope rather than introducing a competing mechanism.

---

## 3. Dependency Rules

### 3.1 Allowed dependency direction

```text
Presentation ───────► Application ───────► Domain
Infrastructure ────► Application ports
Infrastructure ─────────────────────────► Domain (only when needed)
```

The exact arrow between Infrastructure and Application depends on the port design: the application owns the abstraction; infrastructure implements it. Application code must not import a concrete MongoDB adapter merely to call it.

### 3.2 Forbidden dependencies

- Domain → Spring Boot, Spring MVC, MongoDB, Redis, provider SDKs, or controllers.
- Application → concrete persistence implementations, `ResponseEntity`, servlet request/response objects, or provider SDK types.
- Presentation → MongoDB document classes or Spring Data repositories.
- Domain/Application → `util` classes that hide business workflows or introduce infrastructure dependencies.
- Any layer → circular package dependency.

### 3.3 Dependency injection

- Use constructor injection; follow the project's established Lombok convention where applicable.
- Let Spring configuration wire concrete implementations to their ports.
- Do not instantiate Spring-managed services manually with `new` inside business code.
- Avoid service-locator patterns and static global access to application components.
- Keep configuration classes focused on wiring and integration setup, not business workflows.

### 3.4 Feature boundaries

Organize code by feature where the current project structure supports it (for example, user, role, client, schedule, training, nutrition, session tracking, and revenue).

- A feature should own its use cases and business rules.
- Reuse another feature through a clear application-level contract rather than reaching into its internal implementation.
- Avoid a generic `CommonService` that accumulates unrelated business behavior.
- Shared code belongs in a common package only when it is genuinely shared and stable.
- Do not create cross-feature dependencies merely to reuse a small helper; weigh duplication against coupling.

---

## 4. Mapping to the Current ZenFit Codebase

The current codebase has used package names such as `x10.zenfit.mvp.service`, `common.domain`, `controller`, `service`, `Repository`, `config`, `share`, and `util`. Treat these as existing conventions to inspect, not as proof that every class is already placed correctly.

| Existing package / concept | Intended responsibility | Guidance |
|---|---|---|
| `common.domain.entities` | Domain concepts, if genuinely persistence-independent | Keep free of transport and persistence concerns when feasible. |
| `common.domain.dto` | Shared DTOs only when they are truly shared | Prefer feature-specific request/response DTOs for API contracts. |
| `common.domain.enums` | Shared domain enums | Keep business meaning clear; avoid using one enum for unrelated concepts. |
| `controller` | HTTP endpoints and transport mapping | Call application use cases; no direct repository access. |
| `service` / service implementations | Application use cases, based on the existing convention | Keep orchestration focused and avoid database-specific logic. |
| `Repository` | Inspect each type before classifying it | Interfaces may be ports; Spring Data repositories and implementations are adapters. Do not assume the folder name alone defines the layer. |
| `infrastructure` | Persistence documents, adapter implementations, mappers, external integrations | Follow the actual source tree; do not create duplicate persistence paths. |
| `config` | Security, framework wiring, integration configuration | No business workflows or hardcoded secrets. |
| `share` | Shared exception handling and API response support | Preserve the existing error contract and global handler. |
| `util` | Small stateless technical helpers | Do not place business services or workflows here. |

### 4.1 Gradual adoption rule

If the current source tree does not perfectly match this model:

1. Inspect the feature being changed and a comparable existing feature.
2. Follow the established convention unless it violates a critical dependency or security rule.
3. For a new feature, use the cleanest structure that fits the current architecture.
4. For a significant migration, propose a staged plan listing affected files, risks, and tests before changing code.
5. Do not rename packages or move large groups of classes as an incidental part of an unrelated feature.

---

## 5. Persistence and Data Access

### 5.1 MongoDB

- MongoDB is the primary persistence technology according to the current project context; verify the actual configuration before making changes.
- Keep MongoDB-specific queries, annotations, indexes, and document mapping in the persistence adapter layer when the domain/persistence split is used.
- Reuse current identifier types, collection names, field names, and mapping conventions unless a migration is explicitly planned.
- Consider indexes for fields used frequently in lookups, uniqueness checks, sorting, and filtering. Verify query patterns before adding indexes.
- Do not assume MongoDB multi-document operations are transactional in the current deployment. Verify transaction support and deployment configuration before relying on transactions.
- Validate inputs before persistence and handle missing records, duplicate keys, and optimistic/concurrent update risks explicitly.
- Do not silently change a persisted field's type or meaning. Data migrations must be planned and documented.

### 5.2 Redis

- Use Redis only for an identified purpose, such as caching or temporary, expiring data already supported by the application.
- Define TTL and invalidation behavior for cached data.
- Do not treat cached data as the source of truth for critical business state unless the design explicitly requires it.
- Handle cache misses and Redis outages according to the operation's requirements; do not make every operation depend on cache availability unnecessarily.
- Never log Redis passwords or full connection strings.

### 5.3 Repository abstractions

- A repository port expresses the persistence capability required by a use case; a Spring Data repository is a technical implementation.
- Keep query methods aligned with use-case needs. Avoid leaking `Pageable`, MongoDB-specific query objects, or persistence documents into the domain layer.
- Do not add speculative generic repository abstractions that obscure the real operations.
- Ensure uniqueness and concurrency constraints are enforced at the persistence boundary where needed; a prior application-level existence check alone is not sufficient for race-sensitive uniqueness.

---

## 6. API, Validation, and Error Handling

- Preserve the existing API paths, response shape, and error format unless a change is approved.
- Use request DTOs for external input and validate them at the presentation boundary.
- Enforce business invariants again in the application/domain layer; transport validation alone is not enough.
- Use the existing shared exception types and global exception handling in `share` or its current equivalent.
- Map internal exceptions to stable, safe API errors. Do not return stack traces, database details, credentials, or provider secrets to clients.
- Use appropriate HTTP status codes consistent with current project conventions.
- Keep API response DTOs explicit; do not serialize internal entities or persistence documents by default.
- Add pagination, filtering, and sorting in a consistent manner for list endpoints when required.
- Avoid breaking changes to request fields, response fields, status codes, and authorization behavior without approval.

---

## 7. Security Architecture

### 7.1 Authentication and authorization

- Follow the current authentication mechanism and security configuration; inspect the implementation before modifying it.
- Authentication determines who the actor is; authorization determines whether that actor may perform the requested operation.
- Enforce authorization on the server, not only in the frontend.
- Keep role and permission checks consistent across controllers, use cases, and security configuration.
- The current product boundary is for internal team users and PTs; client records are not client login accounts by default.
- Do not grant access to an endpoint merely because a frontend route is hidden.
- Do not weaken or bypass existing security filters to make a test or integration work.

### 7.2 Secrets and configuration

- Never read, print, dump, commit, or expose `.env` secrets unless the user explicitly authorizes it and access is strictly necessary.
- Use environment variable names and `.env.example` to understand configuration without reading actual secret values.
- Never hardcode JWT secrets, passwords, database credentials, SMTP credentials, API keys, OAuth secrets, or payment-provider secrets.
- Do not include secrets in logs, exception messages, API responses, test fixtures, documentation, or generated reports.
- Do not modify, delete, rotate, or overwrite secrets without explicit approval.
- If a secret is exposed, do not repeat it; notify the user and recommend rotation when appropriate.

### 7.3 Cookies, tokens, and sensitive data

- Follow the existing cookie and token design; verify environment-specific settings before changing `Secure`, `HttpOnly`, `SameSite`, domain, or path attributes.
- Do not place credentials or sensitive personal data in URLs.
- Avoid logging access tokens, refresh tokens, OTPs, passwords, or full authentication headers.
- Use short-lived verification codes and enforce expiry, attempt limits, and one-time-use behavior where applicable to the implemented flow.
- Avoid leaking whether an account exists through authentication and recovery responses where the current security policy requires generic messages.

### 7.4 Webhooks and external integrations

- Verify signatures or authenticity using the provider's documented mechanism when supported.
- Validate payloads and treat external input as untrusted.
- Design payment- or state-changing callbacks for duplicate delivery and idempotency.
- Do not trust a frontend redirect as proof that a payment or external action succeeded.
- Never invent provider API behavior; inspect current integration code and official documentation when implementation details are uncertain.

---

## 8. Business Integrity and Concurrency

ZenFit manages schedules, client records, training sessions, remaining sessions, and potentially financial data. These operations can affect real business state and must be handled carefully.

- Validate that the actor is allowed to operate on the target client, schedule, session, or record.
- Validate state transitions before persisting them.
- Prevent duplicate session check-ins or duplicate deductions for the same business event.
- Make retryable operations idempotent where possible.
- Consider concurrent schedule changes, simultaneous check-ins, and duplicate requests.
- Do not calculate or mutate financial values using floating-point types. Follow the project's existing monetary representation; if not defined, propose a consistent decimal/currency approach before implementation.
- Define timezone handling consistently for schedules and reminders. Do not mix local times and UTC implicitly.
- Do not infer a business rule (for example, cancellation windows, renewal policy, or deduction timing) if it has not been specified. Ask for clarification when it changes business behavior.
- Use transactions only where the database and deployment configuration support the required semantics. Otherwise, document the consistency strategy and failure recovery.

---

## 9. Testing Strategy

Test behavior at the layer where it can be verified most clearly.

### 9.1 Domain tests

- Test business invariants, state transitions, and calculations without starting Spring when practical.
- Cover valid and invalid cases, including boundary values.

### 9.2 Application/use-case tests

- Test orchestration, authorization decisions owned by the use case, error paths, and interactions with mocked ports where appropriate.
- Verify that failure from a dependency does not result in an invalid state transition.

### 9.3 Adapter/integration tests

- Test MongoDB mapping, repository queries, unique constraints, and integration behavior where relevant.
- Use test configuration or controlled test containers when available; never point tests at production data.
- Do not call real payment, email, or cloud services from ordinary automated tests unless the test is explicitly designed and safely configured for that purpose.

### 9.4 API/security tests

- Test request validation, response mapping, authentication, authorization, and error responses.
- Include tests for unauthorized and forbidden access where applicable.

### 9.5 Verification honesty

- Run the narrowest relevant tests first, then broader tests/builds when feasible.
- Report the exact commands run and whether they passed, failed, or were not run.
- Never claim tests or Maven builds passed unless they were actually executed.
- If verification is blocked by missing environment variables, unavailable services, or tooling, report the limitation without exposing secrets.

---

## 10. Observability and Operational Reliability

- Use structured, meaningful logs with enough context to diagnose failures without exposing sensitive information.
- Prefer stable identifiers or correlation IDs over logging full request bodies.
- Do not log passwords, tokens, OTPs, payment secrets, or unnecessary personal data.
- Distinguish expected business errors from unexpected technical failures.
- Handle external-service timeouts and failures explicitly.
- Add retries only when safe; use bounded retries and avoid retry storms.
- Keep health checks and operational configuration aligned with the actual deployment setup.
- Do not add a monitoring stack or new infrastructure without a concrete need and approval.

---

## 11. Performance and Scalability

- Optimize based on actual query patterns, measurements, and known bottlenecks rather than assumptions.
- Avoid N+1-style repeated database access and unnecessary full-collection scans.
- Use pagination for potentially large result sets.
- Select only the fields needed for a query when supported and beneficial.
- Use Redis only when its cache semantics and invalidation are clear.
- Avoid premature asynchronous processing. When async work is introduced, define failure handling, retry behavior, and observability.
- Keep the modular monolith as the default. Consider splitting a module into a separate service only when there is a demonstrated scaling, isolation, ownership, or deployment need and the operational cost is justified.

---

## 12. AI Coding Workflow

AI coding agents must follow this workflow for changes to the ZenFit codebase.

### Before implementation

1. Read this document and the relevant sections of `AGENTS.md`.
2. Inspect the actual source tree, relevant classes, `pom.xml`, and configuration templates. Do not read actual secret files.
3. Find a similar existing feature and follow its conventions where appropriate.
4. Identify the business requirement, affected layer(s), API impact, data impact, security concerns, and test needs.
5. For multi-file, cross-layer, or architecture-affecting work, present a concise implementation plan before coding.
6. Ask for clarification when missing requirements could materially change business behavior or data integrity.

### During implementation

7. Make the smallest complete change that satisfies the requirement.
8. Keep dependencies pointing inward and preserve feature boundaries.
9. Reuse existing components and error/response conventions.
10. Do not change unrelated files, perform broad refactors, add dependencies, or change architecture without a clear need and approval.
11. Do not invent package names, existing classes, API contracts, or configuration values. Inspect first.
12. Do not modify real secrets or production data.

### After implementation

13. Review the diff for correctness, security, compatibility, and unintended changes.
14. Run relevant tests and Maven verification where possible.
15. Update tests and documentation when behavior or contracts change.
16. Report changed files, key decisions, commands run, verification results, and known limitations.

---

## 13. Definition of Done

A change is complete only when the applicable items below have been addressed:

- [ ] The requested behavior is implemented and matches the stated requirement.
- [ ] The code follows the current Clean Onion boundaries and feature conventions.
- [ ] Request validation and business invariants are enforced at the appropriate layers.
- [ ] Authentication and authorization behavior is preserved and tested where relevant.
- [ ] Persistence and API compatibility are considered; schema/data migrations are documented if needed.
- [ ] Relevant automated tests are added or updated.
- [ ] Relevant tests/build commands are run, or the reason they could not be run is reported.
- [ ] No secrets, credentials, or unnecessary sensitive data are exposed.
- [ ] No unrelated refactoring or unexplained dependency changes are included.
- [ ] Documentation is updated when architecture, API behavior, configuration, or business behavior changes.
- [ ] The final report accurately lists changed files, verification results, and remaining risks.

Not every checkbox applies to every change; explain any important exception rather than claiming completion without verification.

---

## 14. Architecture Change Policy

Changes to any of the following require explicit discussion before implementation:

- Splitting the modular monolith into microservices or changing deployment topology.
- Replacing MongoDB/Redis or adding another datastore or infrastructure service.
- Changing authentication, authorization, token, or cookie behavior.
- Changing public API contracts or role/access boundaries.
- Changing persisted field types, collection structure, identifier types, or migration strategy.
- Introducing a new framework, major dependency, or cross-cutting architectural pattern.
- Moving many classes across layers or packages.

For a proposed architecture change, document:

1. Problem being solved.
2. Proposed decision and alternatives considered.
3. Impact on code, data, APIs, security, deployment, and maintenance.
4. Migration and rollback approach where relevant.
5. Tests and verification required.

---

## 15. Source of Truth and Maintenance

When sources disagree, use this priority:

1. Explicit, current product requirements approved by the project owner.
2. Actual source code, tests, and configuration for what is currently implemented.
3. This architecture document for intended architectural direction.
4. Older plans, comments, or assumptions.

If actual implementation conflicts with this document, do not silently make a broad correction. Identify the conflict, explain the impact, and propose a focused change. Update this document when an architectural decision is approved.

**Last reviewed:** 2026-10-21
