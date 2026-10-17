# Gymify Development Plan


## 1. Technology stack decision

My recommendation is Spring Boot + React Native with Expo + PostgreSQL. Use the same mobile application for customers and trainers, with role-specific screens. Build a separate web dashboard for gym owners and platform administrators.

React Native's official guidance recommends using a framework such as Expo for new applications. Expo also supports building Android and iOS app binaries through EAS Build. This lets you share most mobile application code across both platforms.&#x20;

[image](https://www.google.com/s2/favicons?domain=https://reactnative.dev\&sz=32)

React Native


Mobile application

### React Native + Expo + TypeScript

One customer and trainer app for Android and iOS, with role-based navigation.

Web dashboard

### React + TypeScript

Gym owners manage gyms, memberships, customers, trainers, and reports. Platform admins can use a separate role-protected area.

Backend

### Java 21 + Spring Boot + Spring Security

REST APIs, JWT authentication, business logic, access control, payment verification, and scheduled tasks.

Database and storage

### PostgreSQL + PostGIS + object storage

Relational data, nearby gym searches, and exercise images/videos.

### Supporting technologies

| Area                  | Recommended technology                                             |
| --------------------- | ------------------------------------------------------------------ |
| Mobile navigation     | Expo Router                                                        |
| API communication     | REST + JSON                                                        |
| API state and caching | TanStack Query                                                     |
| Forms and validation  | React Hook Form + Zod                                              |
| Mobile token storage  | Expo SecureStore                                                   |
| Database migrations   | Flyway                                                             |
| API documentation     | OpenAPI / Swagger UI                                               |
| Payment gateway       | Razorpay, if using online payments in India                        |
| Push notifications    | Expo Notifications with the appropriate platform delivery services |
| Backend testing       | JUnit 5, Mockito, Testcontainers                                   |
| Mobile testing        | Jest and React Native Testing Library                              |
| CI/CD                 | GitHub Actions                                                     |
| Initial deployment    | Dockerized backend, managed PostgreSQL, and object storage         |

Expo SecureStore provides encrypted local key-value storage on Android and iOS; use it for mobile authentication credentials rather than ordinary unencrypted application storage.&#x20;

[image](https://www.google.com/s2/favicons?domain=https://docs.expo.dev\&sz=32)

Expo documentation



Why not Flutter? Flutter is also a strong Android/iOS option. React Native is my first choice for this project because TypeScript and React can be shared across the mobile app and owner dashboard, reducing the number of technologies you need to maintain.

## 2. High-level architecture

Start with a modular monolith, rather than microservices. It will be easier to develop, test, deploy, and debug while you are building the MVP.

Gymify mobile app

Customers + Trainers

HTTPS REST API

Gymify web portal

Gym Owners + Administrators

Spring Security + authorization

Spring Boot application

Identity & profiles

Gym discovery

Membership & payments

Exercise & workouts

Fitness tracking

Notifications & admin

PostgreSQL / PostGIS

Image & video storage

Push services

The database is the source of truth for users, memberships, assigned plans, and workout logs. Images and videos should live in object storage, with only their metadata and URLs stored in PostgreSQL.

## 3. Development roadmap

Planning estimate: approximately 14–16 weeks for a focused MVP with one developer working substantially full-time, including time to learn mobile development. A part-time solo effort will likely take longer.

### Phase 1 — Requirements and UI design

Week 1

Tasks:

- Finalize the customer, trainer, owner, and admin workflows.
- Design screens for login, gym discovery, membership, exercise library, workouts, and dashboards.
- Define REST API contracts and the database schema.
- Establish the MVP feature backlog and acceptance criteria.

Deliverables: wireframes, database ERD, API specification, and a prioritized GitHub issue board.

### Phase 2 — Project foundation

Week 2

Tasks:

- Create the Spring Boot project with Java 21, Spring Security, validation, Flyway, and PostgreSQL.
- Create the Expo + TypeScript mobile project.
- Create the React + TypeScript owner/admin web dashboard.
- Configure Docker for local dependencies and GitHub Actions for builds and tests.
- Add development, test, and production configuration profiles.

Deliverables: all projects start locally, database migrations run, health checks work, and CI executes basic tests.

### Phase 3 — Authentication and profiles

Week 3

Tasks:

- Implement registration, login, logout, token refresh, and password reset.
- Implement the roles `CUSTOMER`, `TRAINER`, `GYM_OWNER`, and `ADMIN`.
- Implement customer and trainer profile management.
- Protect APIs using Spring Security and role-based authorization.
- Build login, registration, profile, and role-specific navigation screens.

Deliverables: each user can authenticate and access only permitted screens and APIs.

### Phase 4 — Gym management and discovery

Weeks 4–5

Tasks:

- Implement gym onboarding, profiles, facilities, photos, address, and operating hours.
- Allow owners to create membership plans.
- Implement gym approval in the admin portal.
- Store gym coordinates and implement nearby search using PostGIS.
- Build gym search, filters, map/list view, and gym details screens.

Deliverables: owners can publish approved gym listings, and customers can discover nearby gyms. Allow manual city or area search when location permission is denied.

### Phase 5 — Memberships and payments

Weeks 6–7

Tasks:

- Implement membership plan listing and purchase.
- Create membership records with start date, end date, and status.
- Integrate the payment gateway if online payment is in MVP scope.
- Verify gateway webhooks on the backend and make payment handling idempotent.
- Build membership history, renewal, and expiry views for customers and owners.

Deliverables: a verified payment creates a valid membership; failed or pending payments do not grant paid access.

Keep gym memberships separate from a possible future subscription that gym owners pay to Gymify.

### Phase 6 — Exercise catalogue and workout plans

Weeks 8–9

Tasks:

- Implement exercise categories, muscle groups, equipment, difficulty, instructions, and media references.
- Build the exercise search and filtering screens.
- Implement the workout plan builder for trainers.
- Configure sets, repetitions, duration, rest, ordering, and scheduled days.
- Allow trainers to publish, update, and assign plans to customers.

Deliverables: a trainer can build and assign a complete weekly plan from the shared exercise library.

### Phase 7 — Customer workout and fitness tracking

Weeks 10–11

Tasks:

- Build the customer's daily workout screen.
- Display exercise instructions, images/videos, and workout parameters.
- Save workout and exercise completion logs.
- Implement weight history and basic progress charts.
- Allow trainers to view assigned customers' workout completion and progress.

Deliverables: customer workout activity is persisted, and authorized trainers can review that activity.

### Phase 8 — Reminders and owner operations

Weeks 12–13

Tasks:

- Add local reminders for customer workout schedules.
- Add push notifications for new or changed workout assignments and membership expiry.
- Build owner views for customers, trainers, membership status, payment records, and basic summaries.
- Build the admin screens for exercise management, gym approval, and account management.
- Add empty states, loading states, error handling, and notification preferences.

Deliverables: the principal customer, trainer, and owner workflows are connected end-to-end.

### Phase 9 — QA, security, and beta release

Weeks 14–16

Tasks:

- Test mobile screens on Android and iOS.
- Add integration tests for membership payments, trainer assignments, and workout tracking.
- Verify object-level permissions and prevent users from accessing other gyms' or customers' records.
- Test location denial, network failures, duplicate webhooks, token expiry, and notification permissions.
- Deploy the backend and database to a test environment.
- Generate Android and iOS beta builds, collect feedback, fix issues, and prepare store submissions.

Deliverables: a stable beta release with deployment instructions, test results, and a known-issues list.

## 4. Database design

Design the schema around the main business entities before starting feature development.

| Entity / table                 | Main responsibility                                                     |
| ------------------------------ | ----------------------------------------------------------------------- |
| `users`                        | Login identity, email/mobile, password hash, account status             |
| `user_roles`                   | Customer, trainer, owner, and admin roles                               |
| `customer_profiles`            | Age, height, weight, goals, and workout preferences                     |
| `trainer_profiles`             | Specializations, experience, and profile information                    |
| `gyms`                         | Name, owner, address, coordinates, operating hours, verification status |
| `gym_media`                    | Gym photos and media references                                         |
| `membership_plans`             | Gym-specific pricing, duration, and benefits                            |
| `memberships`                  | Customer's membership, validity, and status                             |
| `payment_transactions`         | Gateway reference, amount, status, and idempotency key                  |
| `customer_trainer_assignments` | Mapping between customers, trainers, and gyms                           |
| `exercises`                    | Exercise details, equipment, difficulty, and instructions               |
| `workout_plans`                | Reusable workout plan definitions                                       |
| `workout_plan_days`            | Days or sessions within each plan                                       |
| `workout_plan_exercises`       | Exercise, sets, reps, rest, duration, and ordering                      |
| `workout_assignments`          | Plan assigned to a particular customer and validity period              |
| `workout_logs`                 | Workout session completion and timestamps                               |
| `exercise_logs`                | Per-exercise completion and actual performance                          |
| `weight_logs`                  | Customer weight history                                                 |
| `notification_preferences`     | Reminder settings and preferences                                       |

Important design decisions:

- Support multiple roles per user where appropriate. Do not assume every person will always have exactly one role.
- Scope each gym owner's access to gyms they manage.
- Let trainers access only customers assigned to them and only the data needed for coaching.
- Keep reusable workout plans separate from customer assignments and workout completion logs.
- Use a geospatial index for nearby gym searches.
- Use explicit membership and payment statuses; do not infer successful payment from a mobile app screen.

## 5. REST API implementation plan

Use a versioned API prefix, such as `/api/v1`, and document endpoints in OpenAPI.

| API group         | Example endpoints                                                     |
| ----------------- | --------------------------------------------------------------------- |
| Authentication    | `POST /auth/register`, `/auth/login`, `/auth/refresh`, `/auth/logout` |
| Profile           | `GET /me`, `PUT /me/profile`                                          |
| Gym discovery     | `GET /gyms?latitude=...&longitude=...`, `GET /gyms/{gymId}`           |
| Gym management    | `POST /owner/gyms`, `PUT /owner/gyms/{gymId}`                         |
| Membership plans  | `GET /gyms/{gymId}/membership-plans`                                  |
| Memberships       | `POST /memberships`, `GET /me/memberships`                            |
| Payments          | `POST /payments/orders`, `POST /payments/webhook`                     |
| Exercise library  | `GET /exercises`, `GET /exercises/{exerciseId}`                       |
| Workout plans     | `POST /trainer/workout-plans`, `PUT /trainer/workout-plans/{planId}`  |
| Assignments       | `POST /trainer/customers/{customerId}/assignments`                    |
| Customer workouts | `GET /me/workouts`, `POST /me/workout-logs`                           |
| Progress          | `POST /me/weight-logs`, `GET /me/progress`                            |
| Owner operations  | `/owner/customers`, `/owner/trainers`, `/owner/memberships`           |
| Administration    | `/admin/gyms`, `/admin/exercises`, `/admin/users`                     |

These are proposed route examples; finalize request/response DTOs and validation rules in the API specification before implementation.

## 6. GitHub repository structure

Since your repository is already named `Gymify`, I recommend keeping the initial implementation in one monorepo.

```
Gymify/
├── backend/
│   ├── src/main/java/com/gymify/
│   │   ├── auth/
│   │   ├── user/
│   │   ├── gym/
│   │   ├── discovery/
│   │   ├── membership/
│   │   ├── payment/
│   │   ├── exercise/
│   │   ├── workout/
│   │   ├── progress/
│   │   ├── notification/
│   │   └── admin/
│   ├── src/main/resources/
│   │   ├── application.yml
│   │   └── db/migration/
│   └── pom.xml
├── mobile/
│   ├── app/
│   ├── components/
│   ├── features/
│   ├── services/
│   ├── hooks/
│   └── types/
├── owner-web/
│   ├── src/
│   │   ├── features/
│   │   ├── components/
│   │   └── services/
│   └── package.json
├── docs/
│   ├── requirements.md
│   ├── architecture.md
│   ├── database-erd.md
│   └── api-specification.md
├── infra/
├── docker-compose.yml
└── README.md
```

Keep each Spring Boot feature organized around its controller, service, repository, DTOs, entity, and tests as appropriate. Avoid putting all business logic in a shared service package.

## 7. Start implementing the projects

### Create the mobile app

Install Node.js and then run:

```
npx create-expo-app@latest mobile
cd mobile
npx expo start
```

Begin with navigation, login screens, customer/trainer role selection, and API connectivity.

Expo's official guide provides the steps for setting up development and production builds.&#x20;

[image](https://www.google.com/s2/favicons?domain=https://docs.expo.dev\&sz=32)

Expo documentation

+1



### Create the backend

Use Spring Initializr with Java 21 and a compatible, currently supported Spring Boot release. The current Spring Boot requirements document specifies Java 17 as the minimum, so Java 21 is a suitable baseline; verify compatibility for the exact dependencies you select.&#x20;

[image](https://www.google.com/s2/favicons?domain=https://docs.spring.io\&sz=32)

Home



Select Spring Web, Spring Security, Spring Data JPA, Validation, PostgreSQL Driver, and Flyway support. Add testing and OpenAPI dependencies as part of the foundation.

### Configure the local development environment

Use Docker Compose for PostgreSQL and any optional local infrastructure. Keep credentials in environment variables or a local ignored configuration file, never in Git.

After the mobile and backend foundations work, create the owner web dashboard and connect it to the same REST API.

## 8. Testing and security strategy

Before releasing the MVP, include these checks as part of development rather than leaving all testing until the end.

Backend: Unit tests for services, integration tests for repositories and API endpoints, migration tests, authorization tests, and payment webhook tests.

Mobile: Test authentication, permission denial, API loading and error states, workout completion, offline/network interruptions, and notification permission handling. React Native's testing guidance describes automated testing approaches ranging from static analysis to end-to-end checks.&#x20;

[image](https://www.google.com/s2/favicons?domain=https://reactnative.dev\&sz=32)

React Native



Security: Use short-lived JWT access tokens and a properly managed refresh flow. Store mobile credentials using secure storage. For the browser portal, use an appropriate secure session or HTTP-only cookie approach for refresh credentials. Enforce permissions on every backend operation, validate payment webhooks, rate-limit authentication endpoints, and protect sensitive fitness-profile data.

Notifications: Use scheduled local notifications for workout reminders and server-triggered push notifications for assignments and expiry alerts. Actual notification delivery depends on user permission and platform behavior; don't treat a mobile reminder as a guaranteed alarm.

## 9. Definition of done for the MVP

Gymify is ready for a first beta when:

- A customer can register, discover nearby gyms, view plans, and purchase a membership through the selected payment flow.
- A gym owner can manage the gym profile, membership plans, customers, and trainer assignments.
- A trainer can create a workout plan from the exercise library and assign it to a customer.
- A customer can complete the workout, record progress, and receive permitted reminders.
- Administrators can approve gyms and manage the exercise library.
- The Android and iOS apps pass core device testing, the owner portal works in browsers, and backend permissions and payment handling have been tested.


Build in this order: backend foundation → customer/trainer mobile app → owner web portal → memberships and payments → workouts and tracking → release hardening. Implement gym management early enough for owners to populate real gym listings before you build the discovery experience.

