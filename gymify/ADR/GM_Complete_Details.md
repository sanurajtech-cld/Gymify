# Gymify — MVP Feature List

Gymify can be a fitness platform connecting customers, gym owners, and gym trainers in one application ecosystem. Customers discover gyms, purchase subscriptions, and follow personalized workout plans. Gym owners manage their gyms and memberships, while trainers manage assigned customers and their exercise routines.

For the MVP, I recommend focusing on the complete core journey: Discover a gym → Subscribe → Connect with a trainer → Follow workouts → Track progress.

## 1. User roles and their core responsibilities

[Comparatif Salles de Sport 2026 : Prix, Avis et Verdict | Fitidia](https://images.openai.com/static-rsc-4/OKozhp0d6aQsrzLydNDi8v_MbnQ5hp5QVAuP0TT6bRRtxgTu0h9LMaIxvqT-I0KlLhlXhlgodV8Ca5jOzHZ16iwRbsfHr8jB6QIKYaqj-EsRwbSHqaVGIBibqkU_AGHRCYdoM-YVz8b3rJrgemGPp3BDxml7l8xDBrWzBadzm2k?purpose=inline)

## 1. Customer

Fitness app user

- Register and manage fitness profile.
- Find nearby gyms and view gym details.
- Purchase and manage gym subscriptions.
- View assigned workouts and exercise instructions.
- Set workout reminders and track progress.

[How to Simplify Member Management and Scheduling—A Complete Guide for Gym Owners | Peavox](https://images.openai.com/static-rsc-4/qcr4on8rgflhwgAd8irY5uv1Pte29DBM1109q-PKkT5HqJUsCiBu4BdGHPpdty2sZKtP3d5_epg2bthMqqhx6NnHXmEDZRKQ2Um9E3K081ITIf8N_2hlFWxaUcILo257feOASs1lnbDXAkbB439vN8VbXzdvcG6wUdXGKv5TKpM?purpose=inline)

## 2. Gym Owner

Gym business management

- Register and manage gym details.
- Publish gym location, facilities, pricing, and plans.
- Manage subscriptions and customer memberships.
- Add trainers and assign customers to trainers.
- View membership and revenue summaries.

[Why most people reach their fitness goals with a personal trainer](https://images.openai.com/static-rsc-4/YQg3NwGcgfkbnJUCM5DytZm6IqjQyrhlCnwT36zds7At5lcQ62GLRarv5TBl6dGmdkQ9HrYdMhSxfhBi1Z6PSFmdZJf6sbW-sZ3n8fsoQfK2c5wDOMwLCsCI7OgDfOq2qpJ9ktG3U3JfJ6NK5OlO9wbh2NxINynySm-VnhG-zgI?purpose=inline)

## 3. Gym Trainer

Customer coaching and workout management

- View assigned customers and their profiles.
- Create workout plans using the exercise library.
- Assign exercises, sets, reps, and rest periods.
- Track workout completion and customer progress.
- Update plans based on customer performance.

## 2. Customer app — MVP features

| Feature name                 | Description                                                      | Priority  |
| ---------------------------- | ---------------------------------------------------------------- | --------- |
| User Registration & Login    | Sign up, log in, log out, and reset password                     | Must-have |
| JWT Authentication           | Secure API access using access tokens and authorization          | Must-have |
| Fitness Profile              | Age, weight, height, fitness goal, and experience level          | Must-have |
| Location Permission          | Use current location to discover nearby gyms                     | Must-have |
| Nearby Gym Discovery         | List gyms by distance from the customer                          | Must-have |
| Gym Search & Filters         | Search by name, distance, price, and facilities                  | Must-have |
| Gym Details                  | View address, photos, facilities, timings, trainers, and pricing | Must-have |
| Gym Subscription Plans       | View available membership plans and validity                     | Must-have |
| Membership Purchase          | Purchase a selected gym membership                               | Must-have |
| My Memberships               | View active, expired, and upcoming memberships                   | Must-have |
| Exercise Library             | Browse exercises by muscle group, equipment, and difficulty      | Must-have |
| My Workout Plan              | View trainer-assigned exercises and daily workout routines       | Must-have |
| Workout Instructions         | View exercise images/videos, sets, reps, and rest time           | Must-have |
| Workout Completion           | Mark exercises or workouts as completed                          | Must-have |
| Workout Reminders            | Schedule local notifications for planned workouts                | Must-have |
| Progress Tracking            | Record weight, workout completion, and progress history          | Must-have |
| Customer-Trainer Association | View the trainer assigned to the customer                        | Must-have |

## 3. Gym owner dashboard — MVP features

| Feature name                  | Description                                                   | Priority    |
| ----------------------------- | ------------------------------------------------------------- | ----------- |
| Gym Owner Registration        | Create an owner account                                       | Must-have   |
| Gym Profile Management        | Manage gym name, description, address, photos, and facilities | Must-have   |
| Gym Location Management       | Save map coordinates and address                              | Must-have   |
| Gym Operating Hours           | Configure opening and closing hours                           | Must-have   |
| Membership Plan Management    | Create monthly, quarterly, half-yearly, and annual plans      | Must-have   |
| Membership Pricing            | Set price, duration, and plan benefits                        | Must-have   |
| Customer Management           | View members, membership status, and expiry date              | Must-have   |
| Trainer Management            | Add trainers and manage their profiles                        | Must-have   |
| Customer-Trainer Assignment   | Assign a trainer to a gym member                              | Must-have   |
| Membership Renewal Management | View upcoming expiries and renewals                           | Must-have   |
| Payment Records               | View successful, failed, and pending payments                 | Must-have   |
| Gym Dashboard                 | Summary of active members, new memberships, and revenue       | Should-have |
| Offers & Discounts            | Configure promotional membership offers                       | Later       |

## 4. Gym trainer dashboard — MVP features

| Feature name                | Description                                                 | Priority    |
| --------------------------- | ----------------------------------------------------------- | ----------- |
| Trainer Profile             | Name, specialization, experience, and profile photo         | Must-have   |
| Assigned Customer List      | View customers assigned by the gym owner                    | Must-have   |
| Customer Fitness Profile    | View relevant goals, age, height, weight, and experience    | Must-have   |
| Exercise Library            | Search and select exercises from the shared catalogue       | Must-have   |
| Workout Plan Builder        | Create a plan using selected exercises                      | Must-have   |
| Exercise Assignment         | Assign exercises to individual customers                    | Must-have   |
| Sets, Reps & Rest           | Configure sets, repetitions, duration, and rest intervals   | Must-have   |
| Workout Scheduling          | Assign workouts to specific days of the week                | Must-have   |
| Workout Plan Updates        | Modify an existing plan and its exercises                   | Must-have   |
| Workout Completion Tracking | See which assigned workouts customers complete              | Must-have   |
| Progress Monitoring         | Review weight history and workout adherence                 | Must-have   |
| Trainer Notes               | Record coaching observations and modifications              | Should-have |
| Customer Notifications      | Notify customers when a workout plan is assigned or updated | Should-have |

## 5. Shared exercise library — core feature

The exercise library should be a centralized catalogue used by customers and trainers. Trainers should select existing exercises rather than manually creating every exercise from scratch.

[Komposiit-raskusketas Actiget® 15 kg | 31 mm | SuperKaup.EE](https://images.openai.com/static-rsc-4/0CCSrjgP4cGbmiybwpIGZxWv8PIPqQNumyNPpnYQGisqcQmr_X8oHrx_dGN7kdWzJY3oRiDgPLDCH8RQnuMz5b2gGnhkufOgrtFXvzofoS1LUeedg72uVq0l-K6WgforpiO4WtihKf7oy8n1fN-vlR_sEHy_Luv6fFi23zuSrEA?purpose=inline)

Exercise master data

Exercise name, description, target muscle, secondary muscles, equipment, difficulty, instructions, image/video, and safety tips.

[10 minutes twice a week is all you need to build strength for running, says a certified running coach | Fit&Well](https://images.openai.com/static-rsc-4/nhRC9cRSlEwunc7HNXpJ7F88SDhFZvezyb9x1XoTMyJB9_EEpIiAhf-0bU2UrkN37OOt1uS0pWpf9LlmagC_Ci0MLuZdljLgq35dpcug_rIywsF6RpopW1fD12xiMeLHX78ISIaSrz-PlcXSOXvKWr5tJdeTG8HhHlHIcwrpfZc?purpose=inline)

Exercise categories

Chest, back, shoulders, arms, legs, abs, cardio, stretching, bodyweight, dumbbell, barbell, and machine exercises.

[Personal Training Packages - Certified Trainers in Singapore](https://images.openai.com/static-rsc-4/fkKFVHa3575tOPDh37tO49c5ol3C3mbvmI2f_az-y-J3z_gX46LDZHAg-5-_WfFzfZi959tM-UkNoGlph6cON6o_Nr5dXZFktjmzWerc0jy0rofUcrIClZdbjahjuR28-kGg2o-q_KIHMi7w_2YzOn2rmOhP6Z5Z1txQFKvNYq0?purpose=inline)

Workout plan assignment

Trainers select exercises, configure sets/reps/rest, and assign the resulting workout plan to one or more scheduled days.

For the MVP, start with a curated exercise catalogue and allow an administrator to add or update exercises. You do not need AI-generated workout plans in the first version.

## 6. Admin panel — essential platform features

A basic Gymify administrator interface will help operate the platform.

| Feature name                 | Description                                               |
| ---------------------------- | --------------------------------------------------------- |
| User Management              | View and manage customer, owner, and trainer accounts     |
| Gym Verification             | Approve or reject gym registrations                       |
| Exercise Management          | Add, edit, categorize, and deactivate exercises           |
| Subscription Configuration   | Configure supported membership plans and payment settings |
| Payment Monitoring           | Review transactions and payment status                    |
| Support Management           | Handle user complaints and basic support requests         |
| Role & Permission Management | Enforce access rules for each user type                   |

## 7. Subscription and payment model

One important product decision is that Gymify can have two distinct subscription types.

MVP

Gym membership subscription

The customer pays a gym for a monthly, quarterly, or annual membership. The gym owner configures the price and duration. Gymify records the payment and membership validity.

Later

Gymify platform subscription

Gym owners pay Gymify for premium business features, such as advanced analytics, multiple branches, staff management, and automated marketing.

For the first release, implement gym membership plans and payment tracking. Integrate a payment gateway such as Razorpay if online payments are required. Handle payment verification through the backend and signed webhooks rather than trusting a success response from the mobile app.

## 8. Recommended MVP scope

To avoid making the first release too large, prioritize these end-to-end workflows.

MVP 1 — Discover & Subscribe

Customer registration, location-based gym search, gym details, membership plans, and subscription purchase.

MVP 2 — Gym Operations

Owner registration, gym profile, plan management, customer records, trainer management, and customer-trainer assignment.

MVP 3 — Workout Management

Exercise library, workout plan builder, trainer assignments, workout schedules, and completion tracking.

MVP 4 — Reminders & Progress

Workout alarms, notifications, weight history, basic progress charts, and membership expiry reminders.

### Features to postpone

- AI-generated personalized workout plans.
- Wearable and smartwatch integrations.
- Diet planning and calorie tracking.
- Live trainer video sessions.
- Social feed, challenges, and leaderboards.
- Advanced business analytics and multi-branch management.
- Automated recurring payments and complex referral programs.

## 9. Suggested technical modules

Given your Java backend background, a modular Spring Boot backend would be a practical starting point. Keep the initial implementation as a modular monolith rather than introducing multiple microservices before the product has traction.

| Module               | Main responsibilities                                 |
| -------------------- | ----------------------------------------------------- |
| Identity & Access    | JWT authentication, roles, permissions                |
| User Profile         | Customer, owner, and trainer profiles                 |
| Gym Management       | Gym details, locations, facilities, operating hours   |
| Discovery            | Nearby gyms, distance sorting, search filters         |
| Membership           | Plans, subscriptions, renewals, expiry                |
| Payments             | Payment initiation, verification, webhooks            |
| Exercise Catalogue   | Exercise master data, categories, media               |
| Workout Management   | Plans, exercises, schedules, assignments              |
| Progress & Reminders | Workout logs, weight history, notifications           |
| Administration       | Gym approval, account management, exercise management |

Use role-based authorization on every protected API. For location search, use geospatial queries; PostgreSQL with PostGIS is one suitable option. You can use a relational database for membership and workout data, and object storage for exercise images and videos.
