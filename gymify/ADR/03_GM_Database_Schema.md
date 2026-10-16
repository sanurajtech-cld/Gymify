# Gymify Database Schema

## Overview

This document describes the database schema for the Gymify application. The schema uses PostgreSQL with PostGIS extension for geospatial queries.

---

## Entity Relationship Diagram (Conceptual)

```
┌─────────────┐     ┌─────────────┐     ┌─────────────┐
│   users     │────<│ user_roles  │>────│   roles     │
└─────────────┘     └─────────────┘     └─────────────┘
       │
       ├──────────────────────────────────────────────┐
       │                    │                         │
       ▼                    ▼                         ▼
┌─────────────┐     ┌─────────────┐           ┌─────────────┐
│    gyms     │     │ memberships │           │workout_plans│
└─────────────┘     └─────────────┘           └─────────────┘
       │                    │                         │
       ▼                    ▼                         ▼
┌─────────────┐     ┌─────────────┐           ┌─────────────┐
│membership_  │     │  payments   │           │workout_days │
│   plans     │     └─────────────┘           └─────────────┘
└─────────────┘                                       │
                                                      ▼
┌─────────────┐     ┌─────────────┐           ┌─────────────┐
│ exercises   │────<│workout_     │>──────────│workout_     │
└─────────────┘     │ exercises   │           │assignments  │
                    └─────────────┘           └─────────────┘
```

---

## Tables

### 1. User Management

#### users
Primary user table for all roles (Customer, Trainer, Gym Owner, Admin).

| Column | Type | Constraints | Description |
|--------|------|-------------|-------------|
| id | UUID | PK, NOT NULL | Primary key |
| email | VARCHAR(255) | UNIQUE, NOT NULL | User email |
| phone | VARCHAR(20) | UNIQUE | Phone number |
| password_hash | VARCHAR(255) | NOT NULL | Bcrypt hashed password |
| first_name | VARCHAR(100) | NOT NULL | First name |
| last_name | VARCHAR(100) | | Last name |
| profile_image_url | VARCHAR(500) | | Profile picture URL |
| date_of_birth | DATE | | Birth date |
| gender | VARCHAR(20) | | MALE, FEMALE, OTHER |
| is_active | BOOLEAN | DEFAULT true | Account status |
| is_email_verified | BOOLEAN | DEFAULT false | Email verification |
| is_phone_verified | BOOLEAN | DEFAULT false | Phone verification |
| created_at | TIMESTAMPTZ | DEFAULT NOW() | Creation timestamp |
| updated_at | TIMESTAMPTZ | DEFAULT NOW() | Last update |

#### roles
System roles for authorization.

| Column | Type | Constraints | Description |
|--------|------|-------------|-------------|
| id | UUID | PK, NOT NULL | Primary key |
| name | VARCHAR(50) | UNIQUE, NOT NULL | CUSTOMER, TRAINER, GYM_OWNER, ADMIN |
| description | VARCHAR(255) | | Role description |

#### user_roles
Many-to-many relationship between users and roles.

| Column | Type | Constraints | Description |
|--------|------|-------------|-------------|
| user_id | UUID | PK, FK | Reference to users |
| role_id | UUID | PK, FK | Reference to roles |

#### refresh_tokens
JWT refresh tokens for session management.

| Column | Type | Constraints | Description |
|--------|------|-------------|-------------|
| id | UUID | PK, NOT NULL | Primary key |
| user_id | UUID | FK, NOT NULL | Reference to users |
| token | VARCHAR(500) | UNIQUE, NOT NULL | Refresh token |
| device_info | VARCHAR(255) | | Device identifier |
| expires_at | TIMESTAMPTZ | NOT NULL | Expiration time |
| created_at | TIMESTAMPTZ | DEFAULT NOW() | Creation timestamp |

#### password_reset_tokens
Tokens for password reset flow.

| Column | Type | Constraints | Description |
|--------|------|-------------|-------------|
| id | UUID | PK, NOT NULL | Primary key |
| user_id | UUID | FK, NOT NULL | Reference to users |
| token | VARCHAR(255) | UNIQUE, NOT NULL | Reset token |
| expires_at | TIMESTAMPTZ | NOT NULL | Expiration time |
| is_used | BOOLEAN | DEFAULT false | Token usage status |
| created_at | TIMESTAMPTZ | DEFAULT NOW() | Creation timestamp |

---

### 2. Gym Management

#### gyms
Gym profiles and information.

| Column | Type | Constraints | Description |
|--------|------|-------------|-------------|
| id | UUID | PK, NOT NULL | Primary key |
| owner_id | UUID | FK, NOT NULL | Reference to users (GYM_OWNER) |
| name | VARCHAR(200) | NOT NULL | Gym name |
| description | TEXT | | Gym description |
| logo_url | VARCHAR(500) | | Logo image URL |
| address_line1 | VARCHAR(255) | NOT NULL | Street address |
| address_line2 | VARCHAR(255) | | Additional address |
| city | VARCHAR(100) | NOT NULL | City |
| state | VARCHAR(100) | | State/Province |
| country | VARCHAR(100) | NOT NULL | Country |
| postal_code | VARCHAR(20) | | ZIP/Postal code |
| location | GEOMETRY(Point, 4326) | | PostGIS point for geo queries |
| phone | VARCHAR(20) | | Contact phone |
| email | VARCHAR(255) | | Contact email |
| website | VARCHAR(255) | | Website URL |
| status | VARCHAR(20) | DEFAULT 'PENDING' | PENDING, APPROVED, REJECTED, SUSPENDED |
| is_active | BOOLEAN | DEFAULT true | Active status |
| created_at | TIMESTAMPTZ | DEFAULT NOW() | Creation timestamp |
| updated_at | TIMESTAMPTZ | DEFAULT NOW() | Last update |

#### gym_operating_hours
Weekly operating schedule for gyms.

| Column | Type | Constraints | Description |
|--------|------|-------------|-------------|
| id | UUID | PK, NOT NULL | Primary key |
| gym_id | UUID | FK, NOT NULL | Reference to gyms |
| day_of_week | SMALLINT | NOT NULL | 0=Sunday, 6=Saturday |
| open_time | TIME | | Opening time |
| close_time | TIME | | Closing time |
| is_closed | BOOLEAN | DEFAULT false | Closed on this day |

#### facilities
Master list of gym facilities/amenities.

| Column | Type | Constraints | Description |
|--------|------|-------------|-------------|
| id | UUID | PK, NOT NULL | Primary key |
| name | VARCHAR(100) | UNIQUE, NOT NULL | Facility name |
| icon | VARCHAR(100) | | Icon identifier |
| description | VARCHAR(255) | | Facility description |

#### gym_facilities
Many-to-many: gyms and their facilities.

| Column | Type | Constraints | Description |
|--------|------|-------------|-------------|
| gym_id | UUID | PK, FK | Reference to gyms |
| facility_id | UUID | PK, FK | Reference to facilities |

#### gym_photos
Photo gallery for gyms.

| Column | Type | Constraints | Description |
|--------|------|-------------|-------------|
| id | UUID | PK, NOT NULL | Primary key |
| gym_id | UUID | FK, NOT NULL | Reference to gyms |
| image_url | VARCHAR(500) | NOT NULL | Image URL |
| caption | VARCHAR(255) | | Photo caption |
| display_order | INT | DEFAULT 0 | Sort order |
| created_at | TIMESTAMPTZ | DEFAULT NOW() | Upload timestamp |

#### gym_trainers
Trainers associated with gyms.

| Column | Type | Constraints | Description |
|--------|------|-------------|-------------|
| gym_id | UUID | PK, FK | Reference to gyms |
| trainer_id | UUID | PK, FK | Reference to users (TRAINER) |
| is_active | BOOLEAN | DEFAULT true | Active association |
| joined_at | TIMESTAMPTZ | DEFAULT NOW() | Join date |

---

### 3. Membership & Payments

#### membership_plans
Subscription plans offered by gyms.

| Column | Type | Constraints | Description |
|--------|------|-------------|-------------|
| id | UUID | PK, NOT NULL | Primary key |
| gym_id | UUID | FK, NOT NULL | Reference to gyms |
| name | VARCHAR(100) | NOT NULL | Plan name |
| description | TEXT | | Plan description |
| duration_days | INT | NOT NULL | Plan duration |
| price | DECIMAL(10,2) | NOT NULL | Plan price |
| currency | VARCHAR(3) | DEFAULT 'INR' | Currency code |
| includes_trainer | BOOLEAN | DEFAULT false | Trainer included |
| max_freeze_days | INT | DEFAULT 0 | Allowed freeze days |
| is_active | BOOLEAN | DEFAULT true | Plan availability |
| created_at | TIMESTAMPTZ | DEFAULT NOW() | Creation timestamp |
| updated_at | TIMESTAMPTZ | DEFAULT NOW() | Last update |

#### memberships
Customer gym memberships.

| Column | Type | Constraints | Description |
|--------|------|-------------|-------------|
| id | UUID | PK, NOT NULL | Primary key |
| user_id | UUID | FK, NOT NULL | Reference to users (CUSTOMER) |
| gym_id | UUID | FK, NOT NULL | Reference to gyms |
| plan_id | UUID | FK, NOT NULL | Reference to membership_plans |
| assigned_trainer_id | UUID | FK | Reference to users (TRAINER) |
| start_date | DATE | NOT NULL | Membership start |
| end_date | DATE | NOT NULL | Membership end |
| status | VARCHAR(20) | DEFAULT 'ACTIVE' | ACTIVE, EXPIRED, FROZEN, CANCELLED |
| freeze_start_date | DATE | | Freeze period start |
| freeze_end_date | DATE | | Freeze period end |
| total_freeze_days_used | INT | DEFAULT 0 | Used freeze days |
| created_at | TIMESTAMPTZ | DEFAULT NOW() | Creation timestamp |
| updated_at | TIMESTAMPTZ | DEFAULT NOW() | Last update |

#### payments
Payment transactions.

| Column | Type | Constraints | Description |
|--------|------|-------------|-------------|
| id | UUID | PK, NOT NULL | Primary key |
| membership_id | UUID | FK, NOT NULL | Reference to memberships |
| user_id | UUID | FK, NOT NULL | Reference to users |
| amount | DECIMAL(10,2) | NOT NULL | Payment amount |
| currency | VARCHAR(3) | DEFAULT 'INR' | Currency code |
| payment_method | VARCHAR(50) | | UPI, CARD, NETBANKING, CASH |
| gateway | VARCHAR(50) | | RAZORPAY, MANUAL |
| gateway_payment_id | VARCHAR(255) | | Gateway transaction ID |
| gateway_order_id | VARCHAR(255) | | Gateway order ID |
| status | VARCHAR(20) | DEFAULT 'PENDING' | PENDING, SUCCESS, FAILED, REFUNDED |
| failure_reason | VARCHAR(500) | | Failure description |
| paid_at | TIMESTAMPTZ | | Payment completion time |
| created_at | TIMESTAMPTZ | DEFAULT NOW() | Creation timestamp |
| updated_at | TIMESTAMPTZ | DEFAULT NOW() | Last update |

---

### 4. Exercise Catalog

#### exercise_categories
Exercise classification categories.

| Column | Type | Constraints | Description |
|--------|------|-------------|-------------|
| id | UUID | PK, NOT NULL | Primary key |
| name | VARCHAR(100) | UNIQUE, NOT NULL | Category name |
| description | VARCHAR(255) | | Category description |
| icon | VARCHAR(100) | | Icon identifier |

#### muscle_groups
Target muscle groups.

| Column | Type | Constraints | Description |
|--------|------|-------------|-------------|
| id | UUID | PK, NOT NULL | Primary key |
| name | VARCHAR(100) | UNIQUE, NOT NULL | Muscle group name |
| body_region | VARCHAR(50) | | UPPER, LOWER, CORE |
| image_url | VARCHAR(500) | | Anatomy image URL |

#### equipment
Gym equipment catalog.

| Column | Type | Constraints | Description |
|--------|------|-------------|-------------|
| id | UUID | PK, NOT NULL | Primary key |
| name | VARCHAR(100) | UNIQUE, NOT NULL | Equipment name |
| description | VARCHAR(255) | | Equipment description |
| image_url | VARCHAR(500) | | Equipment image URL |

#### exercises
Exercise library.

| Column | Type | Constraints | Description |
|--------|------|-------------|-------------|
| id | UUID | PK, NOT NULL | Primary key |
| name | VARCHAR(200) | NOT NULL | Exercise name |
| description | TEXT | | Exercise description |
| instructions | TEXT | | Step-by-step instructions |
| category_id | UUID | FK | Reference to exercise_categories |
| difficulty | VARCHAR(20) | | BEGINNER, INTERMEDIATE, ADVANCED |
| exercise_type | VARCHAR(30) | | STRENGTH, CARDIO, FLEXIBILITY, BALANCE |
| image_url | VARCHAR(500) | | Exercise image URL |
| video_url | VARCHAR(500) | | Demo video URL |
| thumbnail_url | VARCHAR(500) | | Video thumbnail URL |
| calories_per_minute | DECIMAL(5,2) | | Estimated calorie burn |
| is_active | BOOLEAN | DEFAULT true | Active status |
| created_by | UUID | FK | Reference to users (ADMIN) |
| created_at | TIMESTAMPTZ | DEFAULT NOW() | Creation timestamp |
| updated_at | TIMESTAMPTZ | DEFAULT NOW() | Last update |

#### exercise_muscle_groups
Many-to-many: exercises and target muscles.

| Column | Type | Constraints | Description |
|--------|------|-------------|-------------|
| exercise_id | UUID | PK, FK | Reference to exercises |
| muscle_group_id | UUID | PK, FK | Reference to muscle_groups |
| is_primary | BOOLEAN | DEFAULT true | Primary or secondary target |

#### exercise_equipment
Many-to-many: exercises and required equipment.

| Column | Type | Constraints | Description |
|--------|------|-------------|-------------|
| exercise_id | UUID | PK, FK | Reference to exercises |
| equipment_id | UUID | PK, FK | Reference to equipment |
| is_required | BOOLEAN | DEFAULT true | Required or optional |

---

### 5. Workout Plans

#### workout_plans
Trainer-created workout plans.

| Column | Type | Constraints | Description |
|--------|------|-------------|-------------|
| id | UUID | PK, NOT NULL | Primary key |
| trainer_id | UUID | FK, NOT NULL | Reference to users (TRAINER) |
| name | VARCHAR(200) | NOT NULL | Plan name |
| description | TEXT | | Plan description |
| goal | VARCHAR(100) | | WEIGHT_LOSS, MUSCLE_GAIN, ENDURANCE, GENERAL |
| difficulty | VARCHAR(20) | | BEGINNER, INTERMEDIATE, ADVANCED |
| duration_weeks | INT | | Plan duration in weeks |
| is_template | BOOLEAN | DEFAULT false | Reusable template |
| is_published | BOOLEAN | DEFAULT false | Published status |
| created_at | TIMESTAMPTZ | DEFAULT NOW() | Creation timestamp |
| updated_at | TIMESTAMPTZ | DEFAULT NOW() | Last update |

#### workout_days
Daily workout structure within a plan.

| Column | Type | Constraints | Description |
|--------|------|-------------|-------------|
| id | UUID | PK, NOT NULL | Primary key |
| plan_id | UUID | FK, NOT NULL | Reference to workout_plans |
| day_of_week | SMALLINT | NOT NULL | 0=Sunday, 6=Saturday |
| name | VARCHAR(100) | | Day name (e.g., "Chest Day") |
| is_rest_day | BOOLEAN | DEFAULT false | Rest day flag |
| notes | TEXT | | Day-specific notes |

#### workout_exercises
Exercises within a workout day.

| Column | Type | Constraints | Description |
|--------|------|-------------|-------------|
| id | UUID | PK, NOT NULL | Primary key |
| workout_day_id | UUID | FK, NOT NULL | Reference to workout_days |
| exercise_id | UUID | FK, NOT NULL | Reference to exercises |
| display_order | INT | DEFAULT 0 | Exercise order |
| sets | INT | | Number of sets |
| reps | VARCHAR(50) | | Reps (e.g., "8-12" or "AMRAP") |
| duration_seconds | INT | | Duration for timed exercises |
| rest_seconds | INT | | Rest between sets |
| weight_kg | DECIMAL(6,2) | | Suggested weight |
| notes | TEXT | | Exercise-specific notes |

#### workout_assignments
Plan assignments to customers.

| Column | Type | Constraints | Description |
|--------|------|-------------|-------------|
| id | UUID | PK, NOT NULL | Primary key |
| plan_id | UUID | FK, NOT NULL | Reference to workout_plans |
| customer_id | UUID | FK, NOT NULL | Reference to users (CUSTOMER) |
| trainer_id | UUID | FK, NOT NULL | Reference to users (TRAINER) |
| gym_id | UUID | FK | Reference to gyms |
| start_date | DATE | NOT NULL | Assignment start |
| end_date | DATE | | Assignment end |
| status | VARCHAR(20) | DEFAULT 'ACTIVE' | ACTIVE, COMPLETED, PAUSED, CANCELLED |
| notes | TEXT | | Assignment notes |
| created_at | TIMESTAMPTZ | DEFAULT NOW() | Creation timestamp |
| updated_at | TIMESTAMPTZ | DEFAULT NOW() | Last update |

---

### 6. Fitness Tracking

#### workout_logs
Customer workout session logs.

| Column | Type | Constraints | Description |
|--------|------|-------------|-------------|
| id | UUID | PK, NOT NULL | Primary key |
| user_id | UUID | FK, NOT NULL | Reference to users |
| assignment_id | UUID | FK | Reference to workout_assignments |
| workout_day_id | UUID | FK | Reference to workout_days |
| workout_date | DATE | NOT NULL | Workout date |
| started_at | TIMESTAMPTZ | | Session start time |
| completed_at | TIMESTAMPTZ | | Session end time |
| duration_minutes | INT | | Total duration |
| calories_burned | INT | | Estimated calories |
| status | VARCHAR(20) | DEFAULT 'IN_PROGRESS' | IN_PROGRESS, COMPLETED, SKIPPED |
| notes | TEXT | | Session notes |
| created_at | TIMESTAMPTZ | DEFAULT NOW() | Creation timestamp |

#### exercise_logs
Individual exercise completion within a workout.

| Column | Type | Constraints | Description |
|--------|------|-------------|-------------|
| id | UUID | PK, NOT NULL | Primary key |
| workout_log_id | UUID | FK, NOT NULL | Reference to workout_logs |
| exercise_id | UUID | FK, NOT NULL | Reference to exercises |
| workout_exercise_id | UUID | FK | Reference to workout_exercises |
| sets_completed | INT | | Actual sets done |
| reps_completed | VARCHAR(100) | | Actual reps (e.g., "12,10,8") |
| weight_used_kg | DECIMAL(6,2) | | Actual weight used |
| duration_seconds | INT | | Actual duration |
| is_completed | BOOLEAN | DEFAULT false | Completion status |
| notes | TEXT | | Exercise notes |
| created_at | TIMESTAMPTZ | DEFAULT NOW() | Creation timestamp |

#### body_measurements
Customer body measurement history.

| Column | Type | Constraints | Description |
|--------|------|-------------|-------------|
| id | UUID | PK, NOT NULL | Primary key |
| user_id | UUID | FK, NOT NULL | Reference to users |
| measured_at | DATE | NOT NULL | Measurement date |
| weight_kg | DECIMAL(5,2) | | Body weight |
| height_cm | DECIMAL(5,2) | | Height |
| body_fat_percentage | DECIMAL(4,2) | | Body fat % |
| chest_cm | DECIMAL(5,2) | | Chest measurement |
| waist_cm | DECIMAL(5,2) | | Waist measurement |
| hips_cm | DECIMAL(5,2) | | Hip measurement |
| biceps_cm | DECIMAL(5,2) | | Bicep measurement |
| thighs_cm | DECIMAL(5,2) | | Thigh measurement |
| notes | TEXT | | Measurement notes |
| created_at | TIMESTAMPTZ | DEFAULT NOW() | Creation timestamp |

---

### 7. Notifications

#### user_devices
Push notification device tokens.

| Column | Type | Constraints | Description |
|--------|------|-------------|-------------|
| id | UUID | PK, NOT NULL | Primary key |
| user_id | UUID | FK, NOT NULL | Reference to users |
| device_token | VARCHAR(500) | NOT NULL | Push token |
| device_type | VARCHAR(20) | | ANDROID, IOS |
| device_name | VARCHAR(100) | | Device identifier |
| is_active | BOOLEAN | DEFAULT true | Token validity |
| created_at | TIMESTAMPTZ | DEFAULT NOW() | Registration time |
| updated_at | TIMESTAMPTZ | DEFAULT NOW() | Last update |

#### notifications
In-app notification history.

| Column | Type | Constraints | Description |
|--------|------|-------------|-------------|
| id | UUID | PK, NOT NULL | Primary key |
| user_id | UUID | FK, NOT NULL | Reference to users |
| type | VARCHAR(50) | NOT NULL | WORKOUT_REMINDER, MEMBERSHIP_EXPIRY, PLAN_UPDATE |
| title | VARCHAR(200) | NOT NULL | Notification title |
| message | TEXT | | Notification body |
| data | JSONB | | Additional payload |
| is_read | BOOLEAN | DEFAULT false | Read status |
| read_at | TIMESTAMPTZ | | Read timestamp |
| created_at | TIMESTAMPTZ | DEFAULT NOW() | Creation timestamp |

#### notification_preferences
User notification settings.

| Column | Type | Constraints | Description |
|--------|------|-------------|-------------|
| id | UUID | PK, NOT NULL | Primary key |
| user_id | UUID | FK, UNIQUE, NOT NULL | Reference to users |
| workout_reminders | BOOLEAN | DEFAULT true | Workout reminder opt-in |
| membership_alerts | BOOLEAN | DEFAULT true | Membership alert opt-in |
| plan_updates | BOOLEAN | DEFAULT true | Plan update opt-in |
| promotional | BOOLEAN | DEFAULT false | Marketing opt-in |
| reminder_time | TIME | | Preferred reminder time |
| updated_at | TIMESTAMPTZ | DEFAULT NOW() | Last update |

---

## Indexes

| Table | Index Name | Columns | Type |
|-------|------------|---------|------|
| users | idx_users_email | email | B-tree |
| gyms | idx_gyms_owner | owner_id | B-tree |
| gyms | idx_gyms_city | city | B-tree |
| gyms | idx_gyms_status | status | B-tree |
| gyms | idx_gyms_location | location | GiST |
| memberships | idx_memberships_user | user_id | B-tree |
| memberships | idx_memberships_gym | gym_id | B-tree |
| memberships | idx_memberships_status | status | B-tree |
| payments | idx_payments_gateway_id | gateway_payment_id | B-tree |
| payments | idx_payments_status | status | B-tree |
| exercises | idx_exercises_category | category_id | B-tree |
| exercises | idx_exercises_difficulty | difficulty | B-tree |
| exercises | idx_exercises_name | name | B-tree |
| workout_plans | idx_workout_plans_trainer | trainer_id | B-tree |
| workout_assignments | idx_workout_assignments_customer | customer_id | B-tree |
| workout_assignments | idx_workout_assignments_trainer | trainer_id | B-tree |
| workout_logs | idx_workout_logs_user | user_id | B-tree |
| workout_logs | idx_workout_logs_date | workout_date | B-tree |
| body_measurements | idx_body_measurements_user | user_id | B-tree |
| notifications | idx_notifications_user | user_id | B-tree |
| notifications | idx_notifications_read | is_read | B-tree |

---

## Enum Values Reference

### User Gender
- MALE
- FEMALE
- OTHER

### Gym Status
- PENDING
- APPROVED
- REJECTED
- SUSPENDED

### Membership Status
- ACTIVE
- EXPIRED
- FROZEN
- CANCELLED

### Payment Status
- PENDING
- SUCCESS
- FAILED
- REFUNDED

### Exercise Difficulty
- BEGINNER
- INTERMEDIATE
- ADVANCED

### Exercise Type
- STRENGTH
- CARDIO
- FLEXIBILITY
- BALANCE

### Workout Goal
- WEIGHT_LOSS
- MUSCLE_GAIN
- ENDURANCE
- GENERAL

### Assignment Status
- ACTIVE
- COMPLETED
- PAUSED
- CANCELLED

### Workout Log Status
- IN_PROGRESS
- COMPLETED
- SKIPPED

### Device Type
- ANDROID
- IOS

### Notification Type
- WORKOUT_REMINDER
- MEMBERSHIP_EXPIRY
- PLAN_UPDATE
- NEW_ASSIGNMENT
