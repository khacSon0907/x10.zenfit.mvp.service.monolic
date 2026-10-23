Business Rules — ZenFit
1. Purpose
   This document defines the business rules and operational constraints of ZenFit. Developers and AI coding agents MUST follow these rules when implementing, modifying, or reviewing business logic.

If a rule is unclear:
- Inspect the existing implementation and related tests.
- Do not invent business requirements.
- Keep unresolved decisions in Pending Business Decisions.
- Request clarification before implementing consequential behavior based on an assumption.
- Update this document when an approved business rule changes.
  Technical architecture and layer responsibilities are defined in docs/system-design-architecture.md.
2. Business Model
   ZenFit is a subscription-based management platform for personal trainers (PTs), PT teams, and training-center management.

The platform supports:
- Internal user registration and authentication.
- PT team management and task assignment.
- Member profile and training management.
- Scheduling, booking, and training-session check-in.
- Workout and nutrition plans.
- Training progress and weekly goal tracking.
- Contract/form preparation and document export.
- Individual PT subscription, trial, usage, and platform administration.
  2.1. Internal-use model
- Only authorized internal users can log in to ZenFit.
- Members are people managed by PTs; they are not ZenFit system accounts.
- Creating a member profile MUST NOT automatically create a login account.
- Members MUST NOT receive system access unless a separately approved requirement introduces it.
- Member information is managed through authorized internal users.
  2.2. Subscription ownership
- Each PT subscribes to and pays for their own ZenFit subscription.
- A PT's subscription and entitlements MUST be associated with that PT's account.
- Team membership or a manager's role MUST NOT automatically transfer subscription ownership to the team or training center.
- Subscription checks MUST identify the PT account whose access or usage is being evaluated.
- The exact behavior when a PT belongs to a team but has an expired or restricted subscription remains subject to the pending decisions in Section 18.
3. Roles and Permissions
   ZenFit currently defines four business roles.
   3.1. ADMIN — Platform administrator
   Responsibilities:
- Manage PT accounts across the platform.
- Configure trial duration and trial usage limits.
- Manage subscription plans and subscription configurations, according to implemented product requirements.
- Monitor trial usage, paid subscriptions, account status, and platform usage.
- Monitor operational errors and investigate suspicious activity or attempts to bypass subscription restrictions.
- Restrict or suspend accounts only according to approved platform policies.
  ADMIN is a platform-level role, not a normal PT team-management role.
  3.2. HEAD_OF_TRAINING — Owner / senior training manager
  Responsibilities:
- Manage Team Managers within the authorized organizational scope.
- Monitor teams and their PTs.
- Review operational reports within the permitted scope.
- Monitor task completion and training operations.
  This role manages training operations, not the ZenFit platform itself.
  3.3. TEAM_MANAGER — PT team manager
  Responsibilities:
- Manage PTs within the assigned team.
- Assign tasks and work responsibilities.
- Track task progress and completion.
- Monitor PT workload and operational performance.
- View team-level reports within the permitted scope.
  A TEAM_MANAGER MUST NOT automatically receive platform-wide administrative permissions.
  3.4. PT — Personal Trainer
  Responsibilities:
- Create and manage profiles for assigned members.
- Record member goals and relevant training information.
- Manage personal work schedules and appointments.
- Check in members for training sessions.
- Track sessions and remaining package sessions where supported.
- Create workout and nutrition plans.
- Record training progress and weekly goals.
- Prepare contracts and other operational forms.
- Export authorized data to Excel and document files.
  3.5. Authorization rules
- Every protected operation MUST validate authentication and authorization on the backend.
- Frontend role checks MUST NOT be the sole security mechanism.
- Users MUST NOT gain privileges by changing request parameters or frontend state.
- Resource access MUST validate the user's role and permitted organizational/data scope.
- Role changes and privileged operations MUST be restricted to authorized users.
- The backend MUST NOT trust a role, user ID, team ID, or subscription status supplied by an untrusted client without validation.
4. Authentication and Accounts
- Registration and login are for internal ZenFit users.
- Registration MUST validate required fields and reject invalid input.
- Duplicate account identifiers MUST be handled according to the existing account-uniqueness policy.
- Passwords MUST never be stored as plaintext.
- Protected operations MUST reject unauthenticated requests.
- Disabled or suspended accounts MUST NOT continue using restricted features.
- Account status and subscription status are separate concepts and MUST NOT be treated as interchangeable.
- Authentication and account-security errors MUST NOT expose secrets or sensitive implementation details.
  Supported login methods, account verification requirements, and account recovery must follow the approved authentication specification.
5. Organizational and Team Management
- A PT belongs to the organizational scope defined by the system's team model.
- A TEAM_MANAGER can manage only PTs and tasks within the permitted team scope.
- A HEAD_OF_TRAINING can manage Team Managers within the authorized training-center scope.
- ADMIN has platform-level administration permissions.
- Team membership and role assignments MUST be validated on the backend.
- Moving a PT between teams MUST NOT unintentionally expose unrelated team data.
- Deactivating a user MUST NOT silently delete historical tasks, appointments, or training records.
- Organizational changes MUST preserve the integrity of existing records.
  The exact relationship between training centers, teams, and individual PT accounts must follow the approved domain model.
6. Task and Work Assignment
- Authorized managers can create and assign tasks to eligible PTs.
- A task MUST contain the required information defined by the task model.
- Task assignment MUST validate that the assignee belongs to the appropriate organizational scope.
- Only authorized users can update task details, assignment, or status.
- Task status transitions MUST follow the defined workflow.
- Task completion MUST be validated by the backend.
- The system SHOULD record relevant assignment and status-change history.
- Repeated requests MUST NOT create duplicate tasks unintentionally.
  Task statuses, deadlines, priorities, reassignment rules, and escalation workflows remain pending unless already defined in the implementation or approved requirements.
7. Member Profile Management
   A member is a person receiving PT services, not a ZenFit system login account.
- Authorized PTs can create and manage member profiles assigned to them.
- A PT MUST only access member profiles assigned to that PT.
- A PT MUST NOT read, update, export, or otherwise access a member assigned exclusively to another PT unless an explicitly approved permission grants that access.
- Member access restrictions MUST be enforced by the backend for every relevant endpoint, not only by filtering the frontend list.
- Creating a member profile MUST NOT automatically create a member login account.
- Member information MUST be validated before persistence.
- Member goals, training information, and progress records MUST be associated with the correct member.
- Updating a profile MUST NOT overwrite unrelated information unintentionally.
- Historical records needed for operational traceability MUST NOT be silently deleted.
  Member reassignment, duplicate-member detection, archival, and access after reassignment require explicit rules before implementation.
8. Scheduling and Booking
- PTs can manage their own work schedules within their permissions.
- Appointment creation MUST validate the member, PT, date, time, and required appointment details.
- A PT can create or manage appointments only for members assigned to that PT, unless an approved permission explicitly allows otherwise.
- The backend MUST validate appointment availability before saving a booking.
- The system SHOULD prevent conflicting appointments when overlapping sessions are not allowed.
- Appointment updates and cancellations MUST follow permitted status transitions.
- The system MUST use a consistent timezone policy for scheduling and time comparisons.
- Creating or rescheduling an appointment MUST NOT automatically deduct a training session unless an explicitly approved rule requires it.
- A booking MUST reference the correct member and PT.
  The following require confirmation:
- Whether all overlapping appointments are forbidden.
- Permitted cancellation and rescheduling windows.
- Appointment statuses and rules for trial sessions.
- Whether no-shows or late cancellations affect session balances.
9. Training Sessions and Check-in
- Check-in MUST be performed by an authorized PT or another explicitly authorized internal user.
- A PT can check in only a member assigned to that PT, unless an explicitly approved permission allows otherwise.
- A check-in MUST reference the appropriate member and training session or appointment.
- Creating a booking alone MUST NOT deduct a training session.
- Session deduction MUST follow the approved attendance and package rules.
- The remaining session balance MUST be validated before deduction.
- Repeated check-in requests MUST NOT cause multiple deductions for the same session.
- Check-in records and session-balance changes MUST remain consistent.
- Corrections to a completed check-in MUST be authorized and traceable.
- Session balances MUST NOT be changed through an unrestricted client-side request.
  Rules for trial sessions, no-shows, late cancellations, package expiration, refunds, and session restoration remain pending.
10. Workout Plans and Nutrition Plans
- PTs can create and manage workout and nutrition plans only for members assigned to them.
- A plan MUST be associated with the correct member.
- Plan data MUST be validated before persistence.
- A PT MUST NOT unintentionally modify another member's plan.
- Updating a plan MUST preserve the integrity of unrelated plan data.
- Previous plans SHOULD remain accessible when required for progress comparison or historical review.
- Exporting a plan MUST NOT modify the original data.
- Exported files MUST contain only data the requesting user is authorized to access.
  Plan templates, version history, approval, and plan-sharing rules remain pending unless specified by approved requirements.
11. Training Progress and Weekly Goals
- PTs can record and review progress only for members assigned to them.
- Progress records MUST belong to the correct member.
- The system MUST distinguish historical progress from the latest recorded progress.
- Weekly goals MUST be associated with the correct member and applicable time period.
- Goal completion MUST be evaluated using defined criteria rather than arbitrary assumptions.
- Missing progress data MUST NOT automatically be interpreted as failure.
- Updating a goal MUST NOT silently erase historical results.
- Progress reports MUST use consistent calculations across the application.
  The definition of a training week, supported progress metrics, goal-completion criteria, and reminder thresholds remain pending.
12. Contracts, Forms, and Document Export
- PTs can prepare operational forms for members assigned to them.
- Forms MUST use approved templates and validated input data.
- Required contract fields MUST be validated before document generation.
- Generated documents MUST contain data belonging to the correct member and authorized organization.
- Export operations MUST enforce the same authorization policy as normal data retrieval.
- Excel exports MUST use the approved columns and data types.
- Document exports MUST preserve the required template structure and formatting.
- Missing required information MUST be reported clearly rather than silently replaced with fabricated data.
- Exporting a document MUST NOT modify the original member, contract, or plan records.
- Generated files MUST NOT be publicly accessible by default.
- Temporary generated files SHOULD be cleaned up according to the application's file-handling policy.
  Official contract templates, signature requirements, document formats, and retention rules remain pending.
13. Individual PT Subscription and Trial Management
    13.1. Subscription ownership
- Each PT subscribes to and pays for their own subscription.
- Subscription status, billing state, trial state, usage, and entitlements MUST be associated with the individual PT account.
- A team manager or training-center owner MUST NOT automatically own or pay for a PT's subscription.
- The system MUST validate the relevant PT's subscription before allowing subscription-restricted operations.
- Subscription ownership MUST NOT be inferred only from team membership or role.
  13.2. Trial configuration
- ADMIN configures the trial duration and trial usage limits.
- Trial duration and limits MUST be stored in approved configuration or persisted data; they MUST NOT be arbitrary hard-coded values scattered through business logic.
- Trial eligibility and expiration MUST be evaluated by the backend.
- A PT MUST NOT extend trial access by modifying client-side dates or request parameters.
- Repeated registration or account creation MUST be handled according to an approved trial-abuse policy.
- Changes to trial configuration SHOULD be traceable.
  13.3. Paid plans and expiration
- Each subscription plan MUST use approved pricing, entitlements, and usage limits.
- The backend MUST validate subscription eligibility before allowing restricted operations.
- Plan changes, renewals, cancellations, payment failures, and refunds MUST follow approved billing rules.
- Expired or suspended subscriptions MUST be handled according to the configured access policy.
- Existing data MUST NOT be silently deleted merely because a subscription expires.
  Pricing, plan catalog, grace periods, payment integration, feature restrictions, and expired-subscription behavior remain pending unless already approved elsewhere.
14. Platform Monitoring and Abuse Prevention
    ADMIN can monitor platform health, PT account usage, subscription status, operational errors, and potential attempts to bypass subscription or security rules.

The system SHOULD support:
- Account and subscription usage monitoring.
- Tracking relevant quota and entitlement violations.
- Detection of repeated failed or suspicious operations.
- Audit records for privileged changes.
- Error monitoring for operational troubleshooting.
- Rate limiting for sensitive or abuse-prone endpoints.
- Alerts for unusual activity based on approved detection criteria.
  Security requirements:
- Subscription entitlements MUST be enforced by the backend.
- Usage counters MUST NOT rely solely on client-submitted values.
- Repeated requests MUST NOT bypass quotas or create duplicate business effects.
- Sensitive administrative actions MUST be authorized and auditable.
- Suspicious activity MUST be evaluated using reliable evidence and approved policies.
- Logs MUST NOT contain passwords, access tokens, or other secrets.
- Do not automatically ban accounts solely because of an unverified anomaly.
  Abuse-detection thresholds, account restrictions, review procedures, and retention periods remain pending.
15. Data Integrity and Auditability
- Business validation MUST be enforced on the backend.
- Related data updates MUST follow an appropriate consistency strategy.
- Operations that require atomicity MUST use an appropriate transaction or consistency mechanism supported by the persistence layer.
- Repeated requests MUST NOT cause duplicate business effects where idempotency is required.
- Invalid business-state transitions MUST be rejected.
- Important changes to roles, subscriptions, check-ins, and session balances SHOULD be traceable.
- Historical operational records MUST NOT be silently altered to hide prior actions.
- Deletion and archival MUST follow the approved retention policy.
16. Error Handling
- Business-rule violations MUST return consistent error responses.
- Validation failures MUST be distinguishable from unexpected system errors.
- Errors MUST NOT expose secrets, credentials, or internal implementation details.
- Business exceptions MUST follow the project's established exception-handling conventions.
- Failed operations MUST NOT be reported as successful.
- Partial updates MUST be handled according to the operation's consistency requirements.
17. AI Coding Rules
    AI coding agents MUST:
1. Read this document and docs/system-design-architecture.md before implementing a business feature.
2. Inspect existing entities, DTOs, services, repositories, controllers, security configuration, and tests.
3. Identify the applicable role, data scope, subscription entitlement, and business-state rules.
4. Enforce business rules on the backend.
5. Keep business logic out of controllers when the established architecture places it in the application or domain layer.
6. Add tests for valid operations, invalid operations, authorization boundaries, edge cases, and duplicate requests where applicable.
7. Avoid changing unrelated business behavior.
8. Never invent pricing, quotas, trial duration, or other unapproved business policies.
9. Report unresolved business decisions before implementing consequential assumptions.
10. Update this document when an approved rule changes.
18. Pending Business Decisions
    The following decisions remain unconfirmed and MUST NOT be inferred by AI coding agents:
- Exact permission matrix for ADMIN, HEAD_OF_TRAINING, TEAM_MANAGER, and PT.
- Relationship between training centers, teams, Team Managers, and individual PT accounts.
- What happens to team-managed work when a PT's individual subscription expires or is restricted.
- Subscription plan catalog, pricing, quotas, billing, renewal, cancellation, grace period, and payment integration.
- Trial eligibility, repeat-trial prevention, and consequences of exceeding trial limits.
- Appointment statuses, overlap, cancellation, rescheduling, and no-show rules.
- Session deduction, package expiration, refunds, and session restoration.
- Member reassignment, duplicate detection, and archival.
- Workout/nutrition plan versioning and approval.
- Weekly goal calculation, progress metrics, and reminder thresholds.
- Official contract templates, signatures, and document retention.
- Abuse-detection thresholds, account restrictions, review procedures, and audit-log retention.
  Until a decision is approved, AI coding agents MUST NOT treat an assumption as an established business rule.