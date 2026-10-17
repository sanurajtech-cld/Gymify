# Gymify Mobile App - Complete Frontend Development Plan

## Project Overview

Build a single React Native + Expo mobile application for **Gymify** - a gym management platform. The app serves 4 user roles (Customer, Trainer, Gym Owner, Admin) with role-based navigation and screens.

### Tech Stack
- **Framework**: React Native with Expo SDK 52+
- **Language**: TypeScript
- **Navigation**: Expo Router (file-based routing)
- **State Management**: Zustand + TanStack Query (React Query)
- **Forms**: React Hook Form + Zod validation
- **UI Components**: React Native Paper or NativeBase
- **HTTP Client**: Axios
- **Secure Storage**: Expo SecureStore
- **Icons**: @expo/vector-icons
- **Charts**: react-native-chart-kit
- **Maps**: react-native-maps
- **Push Notifications**: expo-notifications

### Backend API
- **Base URL**: `http://localhost:8080/api` (dev) / `https://api.gymify.com/api` (prod)
- **Authentication**: JWT Bearer tokens
- **Access Token Expiry**: 15 minutes
- **Refresh Token Expiry**: 7 days

---

## Project Structure

```
gymify-mobile/
├── app/                          # Expo Router screens
│   ├── (auth)/                   # Auth screens (public)
│   │   ├── _layout.tsx
│   │   ├── login.tsx
│   │   ├── register.tsx
│   │   ├── forgot-password.tsx
│   │   └── reset-password.tsx
│   ├── (app)/                    # Protected screens
│   │   ├── _layout.tsx           # Tab navigator with role check
│   │   ├── (customer)/           # Customer-specific screens
│   │   │   ├── _layout.tsx
│   │   │   ├── index.tsx         # Customer home/dashboard
│   │   │   ├── gyms/
│   │   │   │   ├── index.tsx     # Gym discovery
│   │   │   │   ├── [id].tsx      # Gym details
│   │   │   │   └── search.tsx    # Search gyms
│   │   │   ├── memberships/
│   │   │   │   ├── index.tsx     # My memberships
│   │   │   │   ├── plans/[gymId].tsx  # View plans
│   │   │   │   └── purchase.tsx  # Purchase flow
│   │   │   ├── workouts/
│   │   │   │   ├── index.tsx     # Today's workout
│   │   │   │   ├── plan/[id].tsx # Workout plan details
│   │   │   │   └── history.tsx   # Workout history
│   │   │   ├── exercises/
│   │   │   │   ├── index.tsx     # Exercise library
│   │   │   │   └── [id].tsx      # Exercise details
│   │   │   └── progress/
│   │   │       ├── index.tsx     # Progress dashboard
│   │   │       └── measurements.tsx
│   │   ├── (trainer)/            # Trainer-specific screens
│   │   │   ├── _layout.tsx
│   │   │   ├── index.tsx         # Trainer dashboard
│   │   │   ├── customers/
│   │   │   │   ├── index.tsx     # Assigned customers
│   │   │   │   └── [id].tsx      # Customer details
│   │   │   ├── plans/
│   │   │   │   ├── index.tsx     # My workout plans
│   │   │   │   ├── create.tsx    # Create plan
│   │   │   │   ├── [id]/
│   │   │   │   │   ├── index.tsx # Plan details
│   │   │   │   │   └── edit.tsx  # Edit plan
│   │   │   │   └── assign.tsx    # Assign plan
│   │   │   └── exercises/
│   │   │       └── index.tsx     # Exercise library
│   │   ├── (owner)/              # Gym Owner screens
│   │   │   ├── _layout.tsx
│   │   │   ├── index.tsx         # Owner dashboard
│   │   │   ├── gyms/
│   │   │   │   ├── index.tsx     # My gyms
│   │   │   │   ├── create.tsx    # Create gym
│   │   │   │   └── [id]/
│   │   │   │       ├── index.tsx # Gym details
│   │   │   │       ├── edit.tsx  # Edit gym
│   │   │   │       ├── plans.tsx # Membership plans
│   │   │   │       ├── members.tsx # Members list
│   │   │   │       ├── trainers.tsx # Trainers
│   │   │   │       └── payments.tsx # Payments
│   │   ├── (admin)/              # Admin screens
│   │   │   ├── _layout.tsx
│   │   │   ├── index.tsx         # Admin dashboard
│   │   │   ├── gyms/
│   │   │   │   ├── pending.tsx   # Pending approvals
│   │   │   │   └── [id].tsx      # Gym review
│   │   │   ├── exercises/
│   │   │   │   ├── index.tsx     # Manage exercises
│   │   │   │   └── create.tsx    # Create exercise
│   │   │   └── users/
│   │   │       └── index.tsx     # User management
│   │   ├── profile/
│   │   │   ├── index.tsx         # My profile
│   │   │   ├── edit.tsx          # Edit profile
│   │   │   └── settings.tsx      # App settings
│   │   └── notifications/
│   │       ├── index.tsx         # Notifications list
│   │       └── preferences.tsx   # Notification settings
│   ├── _layout.tsx               # Root layout
│   └── index.tsx                 # Entry redirect
├── components/
│   ├── ui/                       # Reusable UI components
│   │   ├── Button.tsx
│   │   ├── Input.tsx
│   │   ├── Card.tsx
│   │   ├── Avatar.tsx
│   │   ├── Badge.tsx
│   │   ├── Modal.tsx
│   │   ├── LoadingSpinner.tsx
│   │   ├── EmptyState.tsx
│   │   ├── ErrorState.tsx
│   │   └── BottomSheet.tsx
│   ├── forms/                    # Form components
│   │   ├── FormInput.tsx
│   │   ├── FormSelect.tsx
│   │   ├── FormDatePicker.tsx
│   │   └── FormTimePicker.tsx
│   ├── gym/
│   │   ├── GymCard.tsx
│   │   ├── GymListItem.tsx
│   │   ├── FacilityBadge.tsx
│   │   └── OperatingHours.tsx
│   ├── workout/
│   │   ├── ExerciseCard.tsx
│   │   ├── WorkoutDayCard.tsx
│   │   ├── SetRepInput.tsx
│   │   └── ExerciseTimer.tsx
│   ├── membership/
│   │   ├── PlanCard.tsx
│   │   ├── MembershipCard.tsx
│   │   └── PaymentCard.tsx
│   └── charts/
│       ├── ProgressChart.tsx
│       └── WeightChart.tsx
├── services/
│   ├── api/
│   │   ├── client.ts             # Axios instance with interceptors
│   │   ├── auth.api.ts
│   │   ├── user.api.ts
│   │   ├── gym.api.ts
│   │   ├── membership.api.ts
│   │   ├── exercise.api.ts
│   │   ├── workout.api.ts
│   │   ├── tracking.api.ts
│   │   ├── notification.api.ts
│   │   └── admin.api.ts
│   └── storage.ts                # SecureStore wrapper
├── hooks/
│   ├── useAuth.ts
│   ├── useLocation.ts
│   ├── useNotifications.ts
│   └── queries/                  # TanStack Query hooks
│       ├── useGyms.ts
│       ├── useMemberships.ts
│       ├── useExercises.ts
│       ├── useWorkouts.ts
│       └── useTracking.ts
├── stores/
│   ├── authStore.ts              # Zustand auth store
│   └── appStore.ts               # App-wide state
├── types/
│   ├── api.types.ts              # API response types
│   ├── auth.types.ts
│   ├── user.types.ts
│   ├── gym.types.ts
│   ├── membership.types.ts
│   ├── exercise.types.ts
│   ├── workout.types.ts
│   ├── tracking.types.ts
│   └── notification.types.ts
├── utils/
│   ├── constants.ts
│   ├── helpers.ts
│   ├── validators.ts
│   └── formatters.ts
├── theme/
│   ├── colors.ts
│   ├── typography.ts
│   └── spacing.ts
├── app.json
├── package.json
├── tsconfig.json
└── babel.config.js
```

---

## User Roles & Permissions

| Role | Code | Description |
|------|------|-------------|
| Customer | `CUSTOMER` | Gym member who discovers gyms, purchases memberships, follows workouts |
| Trainer | `TRAINER` | Creates workout plans, assigns to customers, tracks progress |
| Gym Owner | `GYM_OWNER` | Manages gyms, membership plans, trainers, views payments |
| Admin | `ADMIN` | Platform admin - approves gyms, manages exercises, user management |

---

## API Endpoints Reference

### Authentication APIs
```typescript
POST /auth/register
Body: { email, password, firstName, lastName, phone, role }
Response: { accessToken, refreshToken, userId }

POST /auth/login
Body: { email, password }
Response: { accessToken, refreshToken, userId }

POST /auth/refresh
Body: { refreshToken }
Response: { accessToken, refreshToken }

POST /auth/logout
Headers: Authorization: Bearer {accessToken}

POST /auth/password/reset-request
Body: { email }

POST /auth/password/reset-confirm
Body: { token, newPassword }
```

### User APIs
```typescript
GET /users/me
Headers: Authorization: Bearer {accessToken}
Response: { id, email, firstName, lastName, phone, dateOfBirth, gender, roles[], ... }

PUT /users/me
Body: { firstName, lastName, phone, dateOfBirth, gender }

PUT /users/me/password
Body: { currentPassword, newPassword }

DELETE /users/me  // Deactivate account

GET /users/{userId}
```

### Gym APIs (Public)
```typescript
GET /gyms?page=0&size=20
Response: { content: Gym[], totalPages, totalElements }

GET /gyms/{gymId}
Response: Gym with facilities, operatingHours, photos

GET /gyms/search?query={query}&page=0&size=20

GET /gyms/nearby?latitude={lat}&longitude={lng}&radiusKm=10

GET /gyms/city/{cityName}?page=0&size=20
```

### Gym Owner APIs
```typescript
GET /owner/gyms
Response: Gym[]

POST /owner/gyms
Body: { name, description, addressLine1, city, state, country, postalCode, latitude, longitude, phone, email }

PUT /owner/gyms/{gymId}
Body: { name, description, ... }

POST /owner/memberships/plans/{gymId}
Body: { name, description, durationDays, price, currency, includesTrainer, maxFreezeDays }

GET /owner/memberships/gym/{gymId}?page=0&size=20

PUT /owner/memberships/{membershipId}/assign-trainer/{trainerId}

GET /owner/memberships/payments/{gymId}?page=0&size=20
```

### Membership APIs
```typescript
GET /memberships/plans/{gymId}
Response: MembershipPlan[]

GET /memberships/my
Response: Membership[]

GET /memberships/{membershipId}

POST /memberships/purchase/{gymId}
Body: { planId, startDate, trainerId? }

GET /memberships/payments
Response: Payment[]
```

### Exercise APIs
```typescript
GET /exercises?page=0&size=20
Response: { content: Exercise[], ... }

GET /exercises/{exerciseId}

GET /exercises/search?query={query}

GET /exercises/category/{categoryId}

GET /exercises/muscle-group/{muscleGroupId}

GET /exercises/categories
Response: ExerciseCategory[]

GET /exercises/muscle-groups
Response: MuscleGroup[]

GET /exercises/equipment
Response: Equipment[]
```

### Workout APIs (Customer)
```typescript
GET /workouts/my-assignments
Response: WorkoutAssignment[]

GET /workouts/plans/{workoutPlanId}
Response: WorkoutPlan with days and exercises
```

### Workout APIs (Trainer)
```typescript
GET /trainer/workouts/plans
Response: WorkoutPlan[]

POST /trainer/workouts/plans
Body: {
  name, description, goal, difficulty, durationWeeks, isTemplate,
  days: [{ dayOfWeek, name, isRestDay, exercises: [{ exerciseId, displayOrder, sets, reps, restSeconds }] }]
}

PUT /trainer/workouts/plans/{planId}/publish

POST /trainer/workouts/assign
Body: { planId, customerId, gymId, startDate, endDate }

GET /trainer/workouts/assignments
```

### Tracking APIs
```typescript
POST /tracking/workouts
Body: {
  assignmentId, workoutDate, notes,
  exercises: [{ exerciseId, setsCompleted, repsCompleted, weightUsedKg, isCompleted }]
}
Response: WorkoutLog

PUT /tracking/workouts/{workoutLogId}/complete

GET /tracking/workouts?page=0&size=20

GET /tracking/workouts/range?start={date}&end={date}

POST /tracking/measurements
Body: { measuredAt, weightKg, heightCm, bodyFatPercentage, chestCm, waistCm, ... }

GET /tracking/measurements?page=0&size=20

GET /tracking/measurements/history?start={date}&end={date}
```

### Notification APIs
```typescript
GET /notifications?page=0&size=20

GET /notifications/unread?page=0&size=20

GET /notifications/unread/count
Response: { count: number }

PUT /notifications/{notificationId}/read

PUT /notifications/read-all

GET /notifications/preferences
Response: NotificationPreference

PUT /notifications/preferences
Body: { workoutReminders, membershipAlerts, planUpdates, promotional, reminderTime }

POST /notifications/devices
Body: { deviceToken, deviceType, deviceName }

DELETE /notifications/devices/{deviceToken}
```

### Admin APIs
```typescript
GET /admin/gyms/pending?page=0&size=20

PUT /admin/gyms/{gymId}/approve

PUT /admin/gyms/{gymId}/reject

PUT /admin/gyms/{gymId}/suspend

POST /admin/exercises
Body: { name, description, instructions, difficulty, exerciseType, caloriesPerMinute }
```

---


## TypeScript Types

```typescript
// types/api.types.ts
export interface ApiResponse<T> {
  success: boolean;
  message: string;
  data: T;
  timestamp: string;
}

export interface PageResponse<T> {
  content: T[];
  page: number;
  size: number;
  totalElements: number;
  totalPages: number;
  last: boolean;
}

// types/auth.types.ts
export interface AuthResponse {
  accessToken: string;
  refreshToken: string;
  userId: string;
  email: string;
  roles: string[];
}

export interface LoginRequest {
  email: string;
  password: string;
}

export interface RegisterRequest {
  email: string;
  password: string;
  firstName: string;
  lastName: string;
  phone: string;
  role: 'CUSTOMER' | 'TRAINER' | 'GYM_OWNER';
}

// types/user.types.ts
export type Gender = 'MALE' | 'FEMALE' | 'OTHER';
export type RoleName = 'CUSTOMER' | 'TRAINER' | 'GYM_OWNER' | 'ADMIN';

export interface User {
  id: string;
  email: string;
  phone: string;
  firstName: string;
  lastName: string;
  profileImageUrl?: string;
  dateOfBirth?: string;
  gender?: Gender;
  isActive: boolean;
  isEmailVerified: boolean;
  roles: RoleName[];
  createdAt: string;
}

// types/gym.types.ts
export type GymStatus = 'PENDING' | 'APPROVED' | 'REJECTED' | 'SUSPENDED';

export interface Gym {
  id: string;
  name: string;
  description?: string;
  logoUrl?: string;
  addressLine1: string;
  addressLine2?: string;
  city: string;
  state?: string;
  country: string;
  postalCode?: string;
  latitude?: number;
  longitude?: number;
  phone?: string;
  email?: string;
  website?: string;
  status: GymStatus;
  isActive: boolean;
  facilities: Facility[];
  operatingHours: OperatingHours[];
  photos: GymPhoto[];
  createdAt: string;
}

export interface Facility {
  id: string;
  name: string;
  icon?: string;
  description?: string;
}

export interface OperatingHours {
  id: string;
  dayOfWeek: number; // 0=Sunday, 6=Saturday
  openTime?: string;
  closeTime?: string;
  isClosed: boolean;
}

export interface GymPhoto {
  id: string;
  imageUrl: string;
  caption?: string;
  displayOrder: number;
}

// types/membership.types.ts
export type MembershipStatus = 'ACTIVE' | 'EXPIRED' | 'CANCELLED' | 'FROZEN';
export type PaymentStatus = 'PENDING' | 'COMPLETED' | 'FAILED' | 'REFUNDED';

export interface MembershipPlan {
  id: string;
  gymId: string;
  name: string;
  description?: string;
  durationDays: number;
  price: number;
  currency: string;
  includesTrainer: boolean;
  maxFreezeDays: number;
  isActive: boolean;
}

export interface Membership {
  id: string;
  userId: string;
  gymId: string;
  gymName: string;
  planId: string;
  planName: string;
  assignedTrainerId?: string;
  assignedTrainerName?: string;
  startDate: string;
  endDate: string;
  status: MembershipStatus;
  createdAt: string;
}

export interface Payment {
  id: string;
  membershipId: string;
  amount: number;
  currency: string;
  paymentMethod?: string;
  gateway?: string;
  status: PaymentStatus;
  paidAt?: string;
  createdAt: string;
}

// types/exercise.types.ts
export type Difficulty = 'BEGINNER' | 'INTERMEDIATE' | 'ADVANCED';
export type ExerciseType = 'STRENGTH' | 'CARDIO' | 'FLEXIBILITY' | 'BALANCE';

export interface ExerciseCategory {
  id: string;
  name: string;
  description?: string;
  icon?: string;
}

export interface MuscleGroup {
  id: string;
  name: string;
  bodyRegion?: string;
  imageUrl?: string;
}

export interface Equipment {
  id: string;
  name: string;
  description?: string;
  imageUrl?: string;
}

export interface Exercise {
  id: string;
  name: string;
  description?: string;
  instructions?: string;
  category?: ExerciseCategory;
  difficulty?: Difficulty;
  exerciseType?: ExerciseType;
  imageUrl?: string;
  videoUrl?: string;
  thumbnailUrl?: string;
  caloriesPerMinute?: number;
  muscleGroups: MuscleGroup[];
  equipment: Equipment[];
  isActive: boolean;
}

// types/workout.types.ts
export type AssignmentStatus = 'ACTIVE' | 'COMPLETED' | 'CANCELLED';
export type WorkoutLogStatus = 'IN_PROGRESS' | 'COMPLETED' | 'SKIPPED';

export interface WorkoutPlan {
  id: string;
  trainerId: string;
  trainerName: string;
  name: string;
  description?: string;
  goal?: string;
  difficulty?: Difficulty;
  durationWeeks?: number;
  isTemplate: boolean;
  isPublished: boolean;
  days: WorkoutDay[];
  createdAt: string;
}

export interface WorkoutDay {
  id: string;
  dayOfWeek: number;
  name?: string;
  isRestDay: boolean;
  notes?: string;
  exercises: WorkoutExercise[];
}

export interface WorkoutExercise {
  id: string;
  exerciseId: string;
  exerciseName: string;
  displayOrder: number;
  sets?: number;
  reps?: string;
  durationSeconds?: number;
  restSeconds?: number;
  weightKg?: number;
  notes?: string;
}

export interface WorkoutAssignment {
  id: string;
  planId: string;
  planName: string;
  customerId: string;
  customerName: string;
  trainerId: string;
  trainerName: string;
  gymId?: string;
  gymName?: string;
  startDate: string;
  endDate?: string;
  status: AssignmentStatus;
  notes?: string;
}

// types/tracking.types.ts
export interface WorkoutLog {
  id: string;
  userId: string;
  assignmentId?: string;
  workoutDayId?: string;
  workoutDate: string;
  startedAt?: string;
  completedAt?: string;
  durationMinutes?: number;
  caloriesBurned?: number;
  status: WorkoutLogStatus;
  notes?: string;
  exerciseLogs: ExerciseLog[];
}

export interface ExerciseLog {
  id: string;
  exerciseId: string;
  exerciseName: string;
  setsCompleted?: number;
  repsCompleted?: string;
  weightUsedKg?: number;
  durationSeconds?: number;
  isCompleted: boolean;
  notes?: string;
}

export interface BodyMeasurement {
  id: string;
  userId: string;
  measuredAt: string;
  weightKg?: number;
  heightCm?: number;
  bodyFatPercentage?: number;
  chestCm?: number;
  waistCm?: number;
  hipsCm?: number;
  bicepsCm?: number;
  thighsCm?: number;
  notes?: string;
}

// types/notification.types.ts
export type NotificationType = 'WORKOUT_REMINDER' | 'MEMBERSHIP_ALERT' | 'PLAN_UPDATE' | 'SYSTEM' | 'PROMOTIONAL';
export type DeviceType = 'ANDROID' | 'IOS' | 'WEB';

export interface Notification {
  id: string;
  type: NotificationType;
  title: string;
  message?: string;
  data?: Record<string, any>;
  isRead: boolean;
  readAt?: string;
  createdAt: string;
}

export interface NotificationPreference {
  id: string;
  workoutReminders: boolean;
  membershipAlerts: boolean;
  planUpdates: boolean;
  promotional: boolean;
  reminderTime?: string;
}
```

---


## Screen Specifications by Role

### Common Screens (All Roles)

#### 1. Login Screen (`/login`)
- Email input with validation
- Password input with show/hide toggle
- "Forgot Password?" link
- Login button
- "Don't have an account? Register" link
- Social login buttons (optional)

#### 2. Register Screen (`/register`)
- First Name, Last Name inputs
- Email input with validation
- Phone input with country code (+91)
- Password input with strength indicator
- Confirm Password input
- Role selector: Customer / Trainer / Gym Owner
- Terms & Conditions checkbox
- Register button

#### 3. Forgot Password Screen (`/forgot-password`)
- Email input
- Send Reset Link button
- Back to Login link

#### 4. Profile Screen (`/profile`)
- Profile photo with edit option
- Display name, email, phone
- Date of birth, gender
- Edit Profile button
- Change Password option
- Notification Settings link
- Logout button
- Deactivate Account option

#### 5. Notifications Screen (`/notifications`)
- List of notifications with unread indicator
- Pull to refresh
- Mark all as read button
- Tap to mark individual as read
- Filter: All / Unread

---

### Customer Screens

#### Customer Home Dashboard (`/(customer)/index`)
```
┌─────────────────────────────────────┐
│  Good Morning, Amit! 👋             │
│  ─────────────────────────────────  │
│  ┌─────────────────────────────┐   │
│  │  TODAY'S WORKOUT            │   │
│  │  Push Day - Chest & Triceps │   │
│  │  5 exercises • ~45 min      │   │
│  │  [Start Workout]            │   │
│  └─────────────────────────────┘   │
│                                     │
│  MY MEMBERSHIP                      │
│  ┌─────────────────────────────┐   │
│  │ 🏋️ Fitness First Koramangala│   │
│  │ Quarterly Premium           │   │
│  │ Expires: 45 days            │   │
│  │ Trainer: Arjun Reddy        │   │
│  └─────────────────────────────┘   │
│                                     │
│  QUICK STATS                        │
│  ┌────┐ ┌────┐ ┌────┐             │
│  │ 12 │ │ 73 │ │ 19%│             │
│  │Wrkt│ │ kg │ │Fat │             │
│  └────┘ └────┘ └────┘             │
│                                     │
│  RECENT ACTIVITY                    │
│  • Completed Pull Day - Yesterday   │
│  • Added measurement - 2 days ago   │
└─────────────────────────────────────┘
```

#### Gym Discovery (`/(customer)/gyms/index`)
- Search bar at top
- Location permission request
- Filter chips: Distance, Price, Facilities
- List/Map toggle
- Gym cards showing:
  - Gym photo
  - Name, rating
  - Distance
  - Starting price
  - Key facilities icons

#### Gym Details (`/(customer)/gyms/[id]`)
- Photo carousel
- Gym name, address
- Operating hours (expandable)
- Facilities list with icons
- Membership plans section
- Trainers section
- Contact info
- "View Plans" CTA button

#### My Memberships (`/(customer)/memberships/index`)
- Active membership card (highlighted)
- Membership details:
  - Gym name
  - Plan name
  - Start/End dates
  - Days remaining
  - Assigned trainer
- Past memberships (collapsed)
- "Find a Gym" button if no active membership

#### Today's Workout (`/(customer)/workouts/index`)
- Current day workout card
- Exercise list with:
  - Exercise name
  - Sets x Reps
  - Rest time
  - Checkbox for completion
- Start Workout button
- Timer for rest periods
- Complete Workout button

#### Exercise Library (`/(customer)/exercises/index`)
- Search bar
- Category filter tabs
- Muscle group filter
- Equipment filter
- Exercise cards:
  - Thumbnail
  - Name
  - Difficulty badge
  - Target muscles

#### Exercise Details (`/(customer)/exercises/[id]`)
- Exercise image/video
- Name, difficulty
- Target muscles
- Equipment needed
- Step-by-step instructions
- Tips section

#### Progress Tracking (`/(customer)/progress/index`)
- Weight chart (line graph)
- Body measurements summary
- Workout completion rate
- Add Measurement button
- Measurement history list

---

### Trainer Screens

#### Trainer Dashboard (`/(trainer)/index`)
```
┌─────────────────────────────────────┐
│  Welcome, Arjun! 💪                 │
│  ─────────────────────────────────  │
│  MY CUSTOMERS                       │
│  ┌─────────────────────────────┐   │
│  │ 👤 Amit Patel               │   │
│  │    Last workout: Yesterday   │   │
│  │    Plan: Beginner Full Body  │   │
│  ├─────────────────────────────┤   │
│  │ 👤 Sneha Iyer               │   │
│  │    Last workout: 3 days ago  │   │
│  │    Plan: Fat Loss Express    │   │
│  └─────────────────────────────┘   │
│                                     │
│  QUICK ACTIONS                      │
│  [Create Plan] [View Exercises]     │
│                                     │
│  MY WORKOUT PLANS                   │
│  • Beginner Full Body (Published)   │
│  • Muscle Building Pro (Draft)      │
│  • Fat Loss Express (Published)     │
│                                     │
│  RECENT ACTIVITY                    │
│  • Amit completed Push Day          │
│  • Sneha skipped Cardio session     │
└─────────────────────────────────────┘
```

#### Assigned Customers (`/(trainer)/customers/index`)
- Customer list with:
  - Profile photo
  - Name
  - Gym name
  - Current plan
  - Last activity
- Tap to view customer details

#### Customer Details (`/(trainer)/customers/[id]`)
- Customer profile info
- Current workout plan
- Workout completion history
- Progress charts
- Body measurements
- Assign/Change Plan button

#### My Workout Plans (`/(trainer)/plans/index`)
- Plan cards:
  - Plan name
  - Difficulty badge
  - Duration
  - Published/Draft status
- Create New Plan FAB
- Filter: All / Published / Drafts

#### Create Workout Plan (`/(trainer)/plans/create`)
- Plan name input
- Description textarea
- Goal selector
- Difficulty selector
- Duration (weeks)
- Day-by-day builder:
  - Add Day button
  - Day name
  - Rest day toggle
  - Add Exercise button
  - Exercise list with drag-to-reorder
- Save as Draft / Publish buttons

#### Assign Plan (`/(trainer)/plans/assign`)
- Select customer dropdown
- Select plan dropdown
- Start date picker
- End date picker
- Notes textarea
- Assign button

---

### Gym Owner Screens

#### Owner Dashboard (`/(owner)/index`)
```
┌─────────────────────────────────────┐
│  Hello, Vikram! 🏢                  │
│  ─────────────────────────────────  │
│  OVERVIEW                           │
│  ┌────┐ ┌────┐ ┌────┐ ┌────┐      │
│  │ 2  │ │ 45 │ │ 3  │ │₹1.2L│      │
│  │Gyms│ │Mbrs│ │Trnr│ │Rev │      │
│  └────┘ └────┘ └────┘ └────┘      │
│                                     │
│  MY GYMS                            │
│  ┌─────────────────────────────┐   │
│  │ 🏋️ Fitness First Koramangala│   │
│  │    ✅ Approved • 30 members  │   │
│  │    [Manage]                  │   │
│  ├─────────────────────────────┤   │
│  │ 🏋️ Fitness First Indiranagar│   │
│  │    ✅ Approved • 15 members  │   │
│  │    [Manage]                  │   │
│  └─────────────────────────────┘   │
│                                     │
│  [+ Add New Gym]                    │
│                                     │
│  RECENT PAYMENTS                    │
│  • ₹6,000 - Amit Patel (Today)     │
│  • ₹1,500 - Sneha Iyer (Yesterday) │
└─────────────────────────────────────┘
```

#### My Gyms (`/(owner)/gyms/index`)
- Gym cards with status badge
- Member count
- Revenue summary
- Manage button
- Add New Gym FAB

#### Create Gym (`/(owner)/gyms/create`)
- Gym name input
- Description textarea
- Address fields (line1, line2, city, state, postal)
- Map picker for coordinates
- Phone, email inputs
- Operating hours for each day
- Facilities multi-select
- Photo upload
- Submit for Approval button

#### Gym Management (`/(owner)/gyms/[id]/index`)
- Gym overview stats
- Quick links:
  - Edit Details
  - Membership Plans
  - Members
  - Trainers
  - Payments

#### Membership Plans (`/(owner)/gyms/[id]/plans`)
- Plan cards with price, duration
- Active/Inactive toggle
- Edit plan
- Create New Plan button

#### Members List (`/(owner)/gyms/[id]/members`)
- Search members
- Filter by status
- Member cards:
  - Name, photo
  - Plan name
  - Expiry date
  - Assigned trainer
- Assign trainer action

#### Trainers (`/(owner)/gyms/[id]/trainers`)
- Trainer list
- Add trainer (by email)
- Remove trainer
- View trainer's customers

#### Payments (`/(owner)/gyms/[id]/payments`)
- Date range filter
- Payment list:
  - Member name
  - Amount
  - Date
  - Status badge
- Export option

---

### Admin Screens

#### Admin Dashboard (`/(admin)/index`)
```
┌─────────────────────────────────────┐
│  Admin Dashboard 🛡️                 │
│  ─────────────────────────────────  │
│  PENDING ACTIONS                    │
│  ┌─────────────────────────────┐   │
│  │ ⏳ 3 Gyms pending approval   │   │
│  │    [Review Now]              │   │
│  └─────────────────────────────┘   │
│                                     │
│  PLATFORM STATS                     │
│  ┌────┐ ┌────┐ ┌────┐ ┌────┐      │
│  │150 │ │ 25 │ │ 40 │ │ 8  │      │
│  │User│ │Gyms│ │Trnr│ │Pend│      │
│  └────┘ └────┘ └────┘ └────┘      │
│                                     │
│  QUICK ACTIONS                      │
│  [Manage Exercises] [View Users]    │
│                                     │
│  RECENT ACTIVITY                    │
│  • New gym registered: FitZone      │
│  • New trainer: Priya Nair          │
│  • Gym approved: PowerHouse         │
└─────────────────────────────────────┘
```

#### Pending Gyms (`/(admin)/gyms/pending`)
- Gym cards with:
  - Gym name
  - Owner name
  - Location
  - Submitted date
- Tap to review

#### Gym Review (`/(admin)/gyms/[id]`)
- Full gym details
- Owner information
- Approve / Reject buttons
- Rejection reason input

#### Exercise Management (`/(admin)/exercises/index`)
- Exercise list
- Search, filter
- Edit exercise
- Deactivate exercise
- Create Exercise FAB

#### Create Exercise (`/(admin)/exercises/create`)
- Exercise name
- Description
- Instructions (rich text)
- Category selector
- Muscle groups multi-select
- Equipment multi-select
- Difficulty selector
- Exercise type selector
- Calories per minute
- Image/Video upload
- Save button

---


## Core Implementation Code

### 1. API Client Setup

```typescript
// services/api/client.ts
import axios, { AxiosError, InternalAxiosRequestConfig } from 'axios';
import * as SecureStore from 'expo-secure-store';
import { router } from 'expo-router';

const API_BASE_URL = process.env.EXPO_PUBLIC_API_URL || 'http://localhost:8080/api';

export const apiClient = axios.create({
  baseURL: API_BASE_URL,
  timeout: 30000,
  headers: {
    'Content-Type': 'application/json',
  },
});

// Request interceptor - add auth token
apiClient.interceptors.request.use(
  async (config: InternalAxiosRequestConfig) => {
    const token = await SecureStore.getItemAsync('accessToken');
    if (token) {
      config.headers.Authorization = `Bearer ${token}`;
    }
    return config;
  },
  (error) => Promise.reject(error)
);

// Response interceptor - handle token refresh
apiClient.interceptors.response.use(
  (response) => response,
  async (error: AxiosError) => {
    const originalRequest = error.config as InternalAxiosRequestConfig & { _retry?: boolean };
    
    if (error.response?.status === 401 && !originalRequest._retry) {
      originalRequest._retry = true;
      
      try {
        const refreshToken = await SecureStore.getItemAsync('refreshToken');
        if (!refreshToken) throw new Error('No refresh token');
        
        const response = await axios.post(`${API_BASE_URL}/auth/refresh`, {
          refreshToken,
        });
        
        const { accessToken, refreshToken: newRefreshToken } = response.data.data;
        await SecureStore.setItemAsync('accessToken', accessToken);
        await SecureStore.setItemAsync('refreshToken', newRefreshToken);
        
        originalRequest.headers.Authorization = `Bearer ${accessToken}`;
        return apiClient(originalRequest);
      } catch (refreshError) {
        // Clear tokens and redirect to login
        await SecureStore.deleteItemAsync('accessToken');
        await SecureStore.deleteItemAsync('refreshToken');
        router.replace('/login');
        return Promise.reject(refreshError);
      }
    }
    
    return Promise.reject(error);
  }
);
```

### 2. Auth Store (Zustand)

```typescript
// stores/authStore.ts
import { create } from 'zustand';
import * as SecureStore from 'expo-secure-store';
import { User, RoleName } from '@/types/user.types';
import { authApi } from '@/services/api/auth.api';
import { userApi } from '@/services/api/user.api';

interface AuthState {
  user: User | null;
  isAuthenticated: boolean;
  isLoading: boolean;
  primaryRole: RoleName | null;
  
  login: (email: string, password: string) => Promise<void>;
  register: (data: RegisterRequest) => Promise<void>;
  logout: () => Promise<void>;
  loadUser: () => Promise<void>;
  setUser: (user: User) => void;
}

export const useAuthStore = create<AuthState>((set, get) => ({
  user: null,
  isAuthenticated: false,
  isLoading: true,
  primaryRole: null,

  login: async (email, password) => {
    const response = await authApi.login({ email, password });
    const { accessToken, refreshToken } = response.data;
    
    await SecureStore.setItemAsync('accessToken', accessToken);
    await SecureStore.setItemAsync('refreshToken', refreshToken);
    
    await get().loadUser();
  },

  register: async (data) => {
    const response = await authApi.register(data);
    const { accessToken, refreshToken } = response.data;
    
    await SecureStore.setItemAsync('accessToken', accessToken);
    await SecureStore.setItemAsync('refreshToken', refreshToken);
    
    await get().loadUser();
  },

  logout: async () => {
    try {
      await authApi.logout();
    } catch (e) {
      // Ignore logout errors
    }
    await SecureStore.deleteItemAsync('accessToken');
    await SecureStore.deleteItemAsync('refreshToken');
    set({ user: null, isAuthenticated: false, primaryRole: null });
  },

  loadUser: async () => {
    try {
      const token = await SecureStore.getItemAsync('accessToken');
      if (!token) {
        set({ isLoading: false, isAuthenticated: false });
        return;
      }
      
      const response = await userApi.getMe();
      const user = response.data;
      const primaryRole = determinePrimaryRole(user.roles);
      
      set({ user, isAuthenticated: true, isLoading: false, primaryRole });
    } catch (error) {
      set({ user: null, isAuthenticated: false, isLoading: false, primaryRole: null });
    }
  },

  setUser: (user) => {
    const primaryRole = determinePrimaryRole(user.roles);
    set({ user, primaryRole });
  },
}));

// Determine primary role (priority: ADMIN > GYM_OWNER > TRAINER > CUSTOMER)
function determinePrimaryRole(roles: RoleName[]): RoleName {
  if (roles.includes('ADMIN')) return 'ADMIN';
  if (roles.includes('GYM_OWNER')) return 'GYM_OWNER';
  if (roles.includes('TRAINER')) return 'TRAINER';
  return 'CUSTOMER';
}
```

### 3. Root Layout with Auth Check

```typescript
// app/_layout.tsx
import { useEffect } from 'react';
import { Stack } from 'expo-router';
import { QueryClient, QueryClientProvider } from '@tanstack/react-query';
import { useAuthStore } from '@/stores/authStore';
import { LoadingScreen } from '@/components/ui/LoadingScreen';

const queryClient = new QueryClient({
  defaultOptions: {
    queries: {
      retry: 2,
      staleTime: 5 * 60 * 1000, // 5 minutes
    },
  },
});

export default function RootLayout() {
  const { isLoading, loadUser } = useAuthStore();

  useEffect(() => {
    loadUser();
  }, []);

  if (isLoading) {
    return <LoadingScreen />;
  }

  return (
    <QueryClientProvider client={queryClient}>
      <Stack screenOptions={{ headerShown: false }}>
        <Stack.Screen name="(auth)" />
        <Stack.Screen name="(app)" />
      </Stack>
    </QueryClientProvider>
  );
}
```

### 4. App Layout with Role-Based Navigation

```typescript
// app/(app)/_layout.tsx
import { Redirect, Tabs } from 'expo-router';
import { useAuthStore } from '@/stores/authStore';
import { Ionicons } from '@expo/vector-icons';

export default function AppLayout() {
  const { isAuthenticated, primaryRole } = useAuthStore();

  if (!isAuthenticated) {
    return <Redirect href="/login" />;
  }

  // Redirect to role-specific home
  const getInitialRoute = () => {
    switch (primaryRole) {
      case 'ADMIN': return '(admin)';
      case 'GYM_OWNER': return '(owner)';
      case 'TRAINER': return '(trainer)';
      default: return '(customer)';
    }
  };

  return (
    <Tabs
      screenOptions={{
        tabBarActiveTintColor: '#6366F1',
        headerShown: false,
      }}
      initialRouteName={getInitialRoute()}
    >
      {/* Customer Tabs */}
      {primaryRole === 'CUSTOMER' && (
        <>
          <Tabs.Screen
            name="(customer)/index"
            options={{
              title: 'Home',
              tabBarIcon: ({ color, size }) => (
                <Ionicons name="home" size={size} color={color} />
              ),
            }}
          />
          <Tabs.Screen
            name="(customer)/gyms"
            options={{
              title: 'Gyms',
              tabBarIcon: ({ color, size }) => (
                <Ionicons name="fitness" size={size} color={color} />
              ),
            }}
          />
          <Tabs.Screen
            name="(customer)/workouts"
            options={{
              title: 'Workouts',
              tabBarIcon: ({ color, size }) => (
                <Ionicons name="barbell" size={size} color={color} />
              ),
            }}
          />
          <Tabs.Screen
            name="(customer)/progress"
            options={{
              title: 'Progress',
              tabBarIcon: ({ color, size }) => (
                <Ionicons name="trending-up" size={size} color={color} />
              ),
            }}
          />
        </>
      )}

      {/* Trainer Tabs */}
      {primaryRole === 'TRAINER' && (
        <>
          <Tabs.Screen
            name="(trainer)/index"
            options={{
              title: 'Dashboard',
              tabBarIcon: ({ color, size }) => (
                <Ionicons name="home" size={size} color={color} />
              ),
            }}
          />
          <Tabs.Screen
            name="(trainer)/customers"
            options={{
              title: 'Customers',
              tabBarIcon: ({ color, size }) => (
                <Ionicons name="people" size={size} color={color} />
              ),
            }}
          />
          <Tabs.Screen
            name="(trainer)/plans"
            options={{
              title: 'Plans',
              tabBarIcon: ({ color, size }) => (
                <Ionicons name="clipboard" size={size} color={color} />
              ),
            }}
          />
          <Tabs.Screen
            name="(trainer)/exercises"
            options={{
              title: 'Exercises',
              tabBarIcon: ({ color, size }) => (
                <Ionicons name="barbell" size={size} color={color} />
              ),
            }}
          />
        </>
      )}

      {/* Owner Tabs */}
      {primaryRole === 'GYM_OWNER' && (
        <>
          <Tabs.Screen
            name="(owner)/index"
            options={{
              title: 'Dashboard',
              tabBarIcon: ({ color, size }) => (
                <Ionicons name="home" size={size} color={color} />
              ),
            }}
          />
          <Tabs.Screen
            name="(owner)/gyms"
            options={{
              title: 'My Gyms',
              tabBarIcon: ({ color, size }) => (
                <Ionicons name="business" size={size} color={color} />
              ),
            }}
          />
        </>
      )}

      {/* Admin Tabs */}
      {primaryRole === 'ADMIN' && (
        <>
          <Tabs.Screen
            name="(admin)/index"
            options={{
              title: 'Dashboard',
              tabBarIcon: ({ color, size }) => (
                <Ionicons name="home" size={size} color={color} />
              ),
            }}
          />
          <Tabs.Screen
            name="(admin)/gyms"
            options={{
              title: 'Gyms',
              tabBarIcon: ({ color, size }) => (
                <Ionicons name="business" size={size} color={color} />
              ),
            }}
          />
          <Tabs.Screen
            name="(admin)/exercises"
            options={{
              title: 'Exercises',
              tabBarIcon: ({ color, size }) => (
                <Ionicons name="barbell" size={size} color={color} />
              ),
            }}
          />
        </>
      )}

      {/* Common - Profile (all roles) */}
      <Tabs.Screen
        name="profile"
        options={{
          title: 'Profile',
          tabBarIcon: ({ color, size }) => (
            <Ionicons name="person" size={size} color={color} />
          ),
        }}
      />
    </Tabs>
  );
}
```

### 5. API Service Examples

```typescript
// services/api/auth.api.ts
import { apiClient } from './client';
import { ApiResponse, AuthResponse, LoginRequest, RegisterRequest } from '@/types';

export const authApi = {
  login: (data: LoginRequest) =>
    apiClient.post<ApiResponse<AuthResponse>>('/auth/login', data),

  register: (data: RegisterRequest) =>
    apiClient.post<ApiResponse<AuthResponse>>('/auth/register', data),

  logout: () =>
    apiClient.post('/auth/logout'),

  refreshToken: (refreshToken: string) =>
    apiClient.post<ApiResponse<AuthResponse>>('/auth/refresh', { refreshToken }),

  requestPasswordReset: (email: string) =>
    apiClient.post('/auth/password/reset-request', { email }),

  confirmPasswordReset: (token: string, newPassword: string) =>
    apiClient.post('/auth/password/reset-confirm', { token, newPassword }),
};

// services/api/gym.api.ts
import { apiClient } from './client';
import { ApiResponse, PageResponse, Gym } from '@/types';

export const gymApi = {
  getAll: (page = 0, size = 20) =>
    apiClient.get<ApiResponse<PageResponse<Gym>>>(`/gyms?page=${page}&size=${size}`),

  getById: (id: string) =>
    apiClient.get<ApiResponse<Gym>>(`/gyms/${id}`),

  search: (query: string, page = 0, size = 20) =>
    apiClient.get<ApiResponse<PageResponse<Gym>>>(`/gyms/search?query=${query}&page=${page}&size=${size}`),

  getNearby: (latitude: number, longitude: number, radiusKm = 10) =>
    apiClient.get<ApiResponse<Gym[]>>(`/gyms/nearby?latitude=${latitude}&longitude=${longitude}&radiusKm=${radiusKm}`),

  getByCity: (city: string, page = 0, size = 20) =>
    apiClient.get<ApiResponse<PageResponse<Gym>>>(`/gyms/city/${city}?page=${page}&size=${size}`),
};

// services/api/workout.api.ts
import { apiClient } from './client';
import { ApiResponse, WorkoutAssignment, WorkoutPlan } from '@/types';

export const workoutApi = {
  // Customer APIs
  getMyAssignments: () =>
    apiClient.get<ApiResponse<WorkoutAssignment[]>>('/workouts/my-assignments'),

  getPlanById: (id: string) =>
    apiClient.get<ApiResponse<WorkoutPlan>>(`/workouts/plans/${id}`),

  // Trainer APIs
  getMyPlans: () =>
    apiClient.get<ApiResponse<WorkoutPlan[]>>('/trainer/workouts/plans'),

  createPlan: (data: CreateWorkoutPlanRequest) =>
    apiClient.post<ApiResponse<WorkoutPlan>>('/trainer/workouts/plans', data),

  publishPlan: (planId: string) =>
    apiClient.put(`/trainer/workouts/plans/${planId}/publish`),

  assignPlan: (data: AssignWorkoutRequest) =>
    apiClient.post<ApiResponse<WorkoutAssignment>>('/trainer/workouts/assign', data),

  getMyAssignmentsAsTrainer: () =>
    apiClient.get<ApiResponse<WorkoutAssignment[]>>('/trainer/workouts/assignments'),
};
```

### 6. TanStack Query Hooks

```typescript
// hooks/queries/useGyms.ts
import { useQuery, useInfiniteQuery } from '@tanstack/react-query';
import { gymApi } from '@/services/api/gym.api';

export const useGyms = (page = 0, size = 20) => {
  return useQuery({
    queryKey: ['gyms', page, size],
    queryFn: () => gymApi.getAll(page, size),
    select: (response) => response.data.data,
  });
};

export const useGym = (id: string) => {
  return useQuery({
    queryKey: ['gym', id],
    queryFn: () => gymApi.getById(id),
    select: (response) => response.data.data,
    enabled: !!id,
  });
};

export const useNearbyGyms = (latitude: number, longitude: number, radiusKm = 10) => {
  return useQuery({
    queryKey: ['gyms', 'nearby', latitude, longitude, radiusKm],
    queryFn: () => gymApi.getNearby(latitude, longitude, radiusKm),
    select: (response) => response.data.data,
    enabled: !!latitude && !!longitude,
  });
};

export const useInfiniteGyms = (size = 20) => {
  return useInfiniteQuery({
    queryKey: ['gyms', 'infinite'],
    queryFn: ({ pageParam = 0 }) => gymApi.getAll(pageParam, size),
    getNextPageParam: (lastPage) => {
      const data = lastPage.data.data;
      return data.last ? undefined : data.page + 1;
    },
    initialPageParam: 0,
  });
};

// hooks/queries/useWorkouts.ts
import { useQuery, useMutation, useQueryClient } from '@tanstack/react-query';
import { workoutApi } from '@/services/api/workout.api';

export const useMyAssignments = () => {
  return useQuery({
    queryKey: ['workouts', 'my-assignments'],
    queryFn: () => workoutApi.getMyAssignments(),
    select: (response) => response.data.data,
  });
};

export const useWorkoutPlan = (id: string) => {
  return useQuery({
    queryKey: ['workout-plan', id],
    queryFn: () => workoutApi.getPlanById(id),
    select: (response) => response.data.data,
    enabled: !!id,
  });
};

export const useCreateWorkoutPlan = () => {
  const queryClient = useQueryClient();
  
  return useMutation({
    mutationFn: workoutApi.createPlan,
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['trainer', 'plans'] });
    },
  });
};

export const useAssignWorkoutPlan = () => {
  const queryClient = useQueryClient();
  
  return useMutation({
    mutationFn: workoutApi.assignPlan,
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['trainer', 'assignments'] });
    },
  });
};
```

---


## Sample Screen Implementations

### Login Screen

```typescript
// app/(auth)/login.tsx
import { useState } from 'react';
import { View, Text, StyleSheet, TouchableOpacity, KeyboardAvoidingView, Platform } from 'react-native';
import { Link, router } from 'expo-router';
import { useForm, Controller } from 'react-hook-form';
import { zodResolver } from '@hookform/resolvers/zod';
import { z } from 'zod';
import { useAuthStore } from '@/stores/authStore';
import { Button, Input, LoadingOverlay } from '@/components/ui';

const loginSchema = z.object({
  email: z.string().email('Invalid email address'),
  password: z.string().min(6, 'Password must be at least 6 characters'),
});

type LoginForm = z.infer<typeof loginSchema>;

export default function LoginScreen() {
  const [isLoading, setIsLoading] = useState(false);
  const [error, setError] = useState('');
  const { login } = useAuthStore();

  const { control, handleSubmit, formState: { errors } } = useForm<LoginForm>({
    resolver: zodResolver(loginSchema),
    defaultValues: { email: '', password: '' },
  });

  const onSubmit = async (data: LoginForm) => {
    setIsLoading(true);
    setError('');
    try {
      await login(data.email, data.password);
      router.replace('/(app)');
    } catch (err: any) {
      setError(err.response?.data?.message || 'Login failed. Please try again.');
    } finally {
      setIsLoading(false);
    }
  };

  return (
    <KeyboardAvoidingView 
      style={styles.container}
      behavior={Platform.OS === 'ios' ? 'padding' : 'height'}
    >
      <LoadingOverlay visible={isLoading} />
      
      <View style={styles.header}>
        <Text style={styles.logo}>💪 Gymify</Text>
        <Text style={styles.title}>Welcome Back</Text>
        <Text style={styles.subtitle}>Sign in to continue</Text>
      </View>

      <View style={styles.form}>
        {error ? <Text style={styles.error}>{error}</Text> : null}

        <Controller
          control={control}
          name="email"
          render={({ field: { onChange, onBlur, value } }) => (
            <Input
              label="Email"
              placeholder="Enter your email"
              keyboardType="email-address"
              autoCapitalize="none"
              value={value}
              onChangeText={onChange}
              onBlur={onBlur}
              error={errors.email?.message}
            />
          )}
        />

        <Controller
          control={control}
          name="password"
          render={({ field: { onChange, onBlur, value } }) => (
            <Input
              label="Password"
              placeholder="Enter your password"
              secureTextEntry
              value={value}
              onChangeText={onChange}
              onBlur={onBlur}
              error={errors.password?.message}
            />
          )}
        />

        <Link href="/forgot-password" asChild>
          <TouchableOpacity>
            <Text style={styles.forgotPassword}>Forgot Password?</Text>
          </TouchableOpacity>
        </Link>

        <Button title="Sign In" onPress={handleSubmit(onSubmit)} />
      </View>

      <View style={styles.footer}>
        <Text style={styles.footerText}>Don't have an account? </Text>
        <Link href="/register" asChild>
          <TouchableOpacity>
            <Text style={styles.link}>Sign Up</Text>
          </TouchableOpacity>
        </Link>
      </View>
    </KeyboardAvoidingView>
  );
}

const styles = StyleSheet.create({
  container: { flex: 1, backgroundColor: '#fff', padding: 24 },
  header: { marginTop: 60, marginBottom: 40 },
  logo: { fontSize: 32, marginBottom: 20 },
  title: { fontSize: 28, fontWeight: 'bold', color: '#1F2937' },
  subtitle: { fontSize: 16, color: '#6B7280', marginTop: 8 },
  form: { flex: 1 },
  error: { color: '#EF4444', marginBottom: 16, textAlign: 'center' },
  forgotPassword: { color: '#6366F1', textAlign: 'right', marginBottom: 24 },
  footer: { flexDirection: 'row', justifyContent: 'center', paddingBottom: 24 },
  footerText: { color: '#6B7280' },
  link: { color: '#6366F1', fontWeight: '600' },
});
```

### Customer Home Dashboard

```typescript
// app/(app)/(customer)/index.tsx
import { View, Text, StyleSheet, ScrollView, RefreshControl } from 'react-native';
import { useAuthStore } from '@/stores/authStore';
import { useMyAssignments } from '@/hooks/queries/useWorkouts';
import { useMyMemberships } from '@/hooks/queries/useMemberships';
import { useMyMeasurements } from '@/hooks/queries/useTracking';
import { Card, Button, LoadingSpinner } from '@/components/ui';
import { TodayWorkoutCard } from '@/components/workout/TodayWorkoutCard';
import { MembershipCard } from '@/components/membership/MembershipCard';
import { QuickStats } from '@/components/dashboard/QuickStats';

export default function CustomerHomeScreen() {
  const { user } = useAuthStore();
  const { data: assignments, isLoading: loadingAssignments, refetch: refetchAssignments } = useMyAssignments();
  const { data: memberships, isLoading: loadingMemberships, refetch: refetchMemberships } = useMyMemberships();
  const { data: measurements } = useMyMeasurements();

  const activeMembership = memberships?.find(m => m.status === 'ACTIVE');
  const activeAssignment = assignments?.find(a => a.status === 'ACTIVE');
  const latestMeasurement = measurements?.[0];

  const isLoading = loadingAssignments || loadingMemberships;

  const onRefresh = () => {
    refetchAssignments();
    refetchMemberships();
  };

  const getGreeting = () => {
    const hour = new Date().getHours();
    if (hour < 12) return 'Good Morning';
    if (hour < 17) return 'Good Afternoon';
    return 'Good Evening';
  };

  if (isLoading) {
    return <LoadingSpinner />;
  }

  return (
    <ScrollView 
      style={styles.container}
      refreshControl={<RefreshControl refreshing={isLoading} onRefresh={onRefresh} />}
    >
      {/* Header */}
      <View style={styles.header}>
        <Text style={styles.greeting}>{getGreeting()}, {user?.firstName}! 👋</Text>
      </View>

      {/* Today's Workout */}
      {activeAssignment && (
        <View style={styles.section}>
          <Text style={styles.sectionTitle}>TODAY'S WORKOUT</Text>
          <TodayWorkoutCard assignment={activeAssignment} />
        </View>
      )}

      {/* Active Membership */}
      {activeMembership ? (
        <View style={styles.section}>
          <Text style={styles.sectionTitle}>MY MEMBERSHIP</Text>
          <MembershipCard membership={activeMembership} />
        </View>
      ) : (
        <View style={styles.section}>
          <Card style={styles.noMembershipCard}>
            <Text style={styles.noMembershipText}>No active membership</Text>
            <Button 
              title="Find a Gym" 
              onPress={() => router.push('/(customer)/gyms')}
              variant="outline"
            />
          </Card>
        </View>
      )}

      {/* Quick Stats */}
      <View style={styles.section}>
        <Text style={styles.sectionTitle}>QUICK STATS</Text>
        <QuickStats
          workoutsCompleted={12}
          currentWeight={latestMeasurement?.weightKg}
          bodyFat={latestMeasurement?.bodyFatPercentage}
        />
      </View>
    </ScrollView>
  );
}

const styles = StyleSheet.create({
  container: { flex: 1, backgroundColor: '#F9FAFB' },
  header: { padding: 20, paddingTop: 60 },
  greeting: { fontSize: 24, fontWeight: 'bold', color: '#1F2937' },
  section: { padding: 20, paddingTop: 0 },
  sectionTitle: { fontSize: 12, fontWeight: '600', color: '#6B7280', marginBottom: 12, letterSpacing: 0.5 },
  noMembershipCard: { alignItems: 'center', padding: 24 },
  noMembershipText: { fontSize: 16, color: '#6B7280', marginBottom: 16 },
});
```

### Trainer Dashboard

```typescript
// app/(app)/(trainer)/index.tsx
import { View, Text, StyleSheet, ScrollView, FlatList, TouchableOpacity } from 'react-native';
import { router } from 'expo-router';
import { useAuthStore } from '@/stores/authStore';
import { useTrainerAssignments } from '@/hooks/queries/useWorkouts';
import { useTrainerPlans } from '@/hooks/queries/useWorkouts';
import { Card, Button, Avatar } from '@/components/ui';
import { Ionicons } from '@expo/vector-icons';

export default function TrainerDashboardScreen() {
  const { user } = useAuthStore();
  const { data: assignments } = useTrainerAssignments();
  const { data: plans } = useTrainerPlans();

  const activeAssignments = assignments?.filter(a => a.status === 'ACTIVE') || [];

  return (
    <ScrollView style={styles.container}>
      {/* Header */}
      <View style={styles.header}>
        <Text style={styles.greeting}>Welcome, {user?.firstName}! 💪</Text>
      </View>

      {/* My Customers */}
      <View style={styles.section}>
        <Text style={styles.sectionTitle}>MY CUSTOMERS</Text>
        {activeAssignments.length > 0 ? (
          <Card>
            {activeAssignments.map((assignment, index) => (
              <TouchableOpacity
                key={assignment.id}
                style={[styles.customerItem, index > 0 && styles.customerItemBorder]}
                onPress={() => router.push(`/(trainer)/customers/${assignment.customerId}`)}
              >
                <Avatar name={assignment.customerName} size={40} />
                <View style={styles.customerInfo}>
                  <Text style={styles.customerName}>{assignment.customerName}</Text>
                  <Text style={styles.customerPlan}>Plan: {assignment.planName}</Text>
                </View>
                <Ionicons name="chevron-forward" size={20} color="#9CA3AF" />
              </TouchableOpacity>
            ))}
          </Card>
        ) : (
          <Card style={styles.emptyCard}>
            <Text style={styles.emptyText}>No customers assigned yet</Text>
          </Card>
        )}
      </View>

      {/* Quick Actions */}
      <View style={styles.section}>
        <Text style={styles.sectionTitle}>QUICK ACTIONS</Text>
        <View style={styles.actionsRow}>
          <Button
            title="Create Plan"
            onPress={() => router.push('/(trainer)/plans/create')}
            icon={<Ionicons name="add-circle" size={20} color="#fff" />}
            style={styles.actionButton}
          />
          <Button
            title="Exercises"
            onPress={() => router.push('/(trainer)/exercises')}
            variant="outline"
            icon={<Ionicons name="barbell" size={20} color="#6366F1" />}
            style={styles.actionButton}
          />
        </View>
      </View>

      {/* My Workout Plans */}
      <View style={styles.section}>
        <View style={styles.sectionHeader}>
          <Text style={styles.sectionTitle}>MY WORKOUT PLANS</Text>
          <TouchableOpacity onPress={() => router.push('/(trainer)/plans')}>
            <Text style={styles.viewAll}>View All</Text>
          </TouchableOpacity>
        </View>
        {plans?.slice(0, 3).map(plan => (
          <TouchableOpacity
            key={plan.id}
            style={styles.planItem}
            onPress={() => router.push(`/(trainer)/plans/${plan.id}`)}
          >
            <View style={styles.planInfo}>
              <Text style={styles.planName}>{plan.name}</Text>
              <Text style={styles.planMeta}>
                {plan.difficulty} • {plan.durationWeeks} weeks
              </Text>
            </View>
            <View style={[styles.statusBadge, plan.isPublished ? styles.published : styles.draft]}>
              <Text style={styles.statusText}>
                {plan.isPublished ? 'Published' : 'Draft'}
              </Text>
            </View>
          </TouchableOpacity>
        ))}
      </View>
    </ScrollView>
  );
}

const styles = StyleSheet.create({
  container: { flex: 1, backgroundColor: '#F9FAFB' },
  header: { padding: 20, paddingTop: 60 },
  greeting: { fontSize: 24, fontWeight: 'bold', color: '#1F2937' },
  section: { padding: 20, paddingTop: 0 },
  sectionHeader: { flexDirection: 'row', justifyContent: 'space-between', alignItems: 'center', marginBottom: 12 },
  sectionTitle: { fontSize: 12, fontWeight: '600', color: '#6B7280', letterSpacing: 0.5 },
  viewAll: { color: '#6366F1', fontWeight: '600' },
  customerItem: { flexDirection: 'row', alignItems: 'center', padding: 12 },
  customerItemBorder: { borderTopWidth: 1, borderTopColor: '#E5E7EB' },
  customerInfo: { flex: 1, marginLeft: 12 },
  customerName: { fontSize: 16, fontWeight: '600', color: '#1F2937' },
  customerPlan: { fontSize: 14, color: '#6B7280', marginTop: 2 },
  actionsRow: { flexDirection: 'row', gap: 12 },
  actionButton: { flex: 1 },
  planItem: { flexDirection: 'row', alignItems: 'center', backgroundColor: '#fff', padding: 16, borderRadius: 12, marginBottom: 8 },
  planInfo: { flex: 1 },
  planName: { fontSize: 16, fontWeight: '600', color: '#1F2937' },
  planMeta: { fontSize: 14, color: '#6B7280', marginTop: 2 },
  statusBadge: { paddingHorizontal: 8, paddingVertical: 4, borderRadius: 4 },
  published: { backgroundColor: '#D1FAE5' },
  draft: { backgroundColor: '#FEF3C7' },
  statusText: { fontSize: 12, fontWeight: '600' },
  emptyCard: { alignItems: 'center', padding: 24 },
  emptyText: { color: '#6B7280' },
});
```

---

## Development Phases

### Phase 1: Project Setup (Week 1)
- [ ] Initialize Expo project with TypeScript
- [ ] Configure Expo Router
- [ ] Set up folder structure
- [ ] Install dependencies (axios, zustand, tanstack-query, react-hook-form, zod)
- [ ] Configure theme (colors, typography, spacing)
- [ ] Create base UI components (Button, Input, Card, etc.)
- [ ] Set up API client with interceptors

### Phase 2: Authentication (Week 2)
- [ ] Implement auth store (Zustand)
- [ ] Create Login screen
- [ ] Create Register screen with role selection
- [ ] Create Forgot Password flow
- [ ] Implement token refresh logic
- [ ] Add secure storage for tokens
- [ ] Create root layout with auth check

### Phase 3: Customer Features (Weeks 3-4)
- [ ] Customer home dashboard
- [ ] Gym discovery with search/filters
- [ ] Gym details screen
- [ ] Membership plans view
- [ ] Purchase membership flow
- [ ] My memberships screen
- [ ] Exercise library
- [ ] Today's workout screen
- [ ] Workout logging
- [ ] Progress tracking & charts
- [ ] Body measurements

### Phase 4: Trainer Features (Weeks 5-6)
- [ ] Trainer dashboard
- [ ] Assigned customers list
- [ ] Customer details & progress view
- [ ] Workout plan builder
- [ ] Exercise selection
- [ ] Plan assignment flow
- [ ] View customer workout logs

### Phase 5: Gym Owner Features (Weeks 7-8)
- [ ] Owner dashboard
- [ ] Create/Edit gym
- [ ] Membership plan management
- [ ] Members list
- [ ] Trainer management
- [ ] Payment history
- [ ] Revenue summary

### Phase 6: Admin Features (Week 9)
- [ ] Admin dashboard
- [ ] Pending gym approvals
- [ ] Gym review & approve/reject
- [ ] Exercise management
- [ ] Create exercise form

### Phase 7: Common Features (Week 10)
- [ ] Profile management
- [ ] Change password
- [ ] Notification preferences
- [ ] Push notifications setup
- [ ] App settings

### Phase 8: Polish & Testing (Weeks 11-12)
- [ ] Error handling & empty states
- [ ] Loading states
- [ ] Pull to refresh
- [ ] Offline handling
- [ ] Performance optimization
- [ ] Bug fixes
- [ ] App store preparation

---

## Test Credentials

Use these credentials to test the app with sample data:

| Role | Email | Password |
|------|-------|----------|
| Admin | admin@gymify.com | Test@123 |
| Gym Owner | owner@fitnessfirst.com | Test@123 |
| Trainer | trainer.arjun@gymify.com | Test@123 |
| Trainer | trainer.priya@gymify.com | Test@123 |
| Customer | customer.amit@gmail.com | Test@123 |
| Customer | customer.sneha@gmail.com | Test@123 |

---

## Environment Variables

```bash
# .env
EXPO_PUBLIC_API_URL=http://localhost:8080/api
EXPO_PUBLIC_ENV=development
```

---

## Package.json Dependencies

```json
{
  "dependencies": {
    "expo": "~52.0.0",
    "expo-router": "~4.0.0",
    "expo-secure-store": "~14.0.0",
    "expo-location": "~18.0.0",
    "expo-notifications": "~0.29.0",
    "expo-image-picker": "~16.0.0",
    "react": "18.3.1",
    "react-native": "0.76.0",
    "react-native-safe-area-context": "4.12.0",
    "react-native-screens": "~4.1.0",
    "react-native-gesture-handler": "~2.20.0",
    "react-native-reanimated": "~3.16.0",
    "@react-navigation/native": "^7.0.0",
    "axios": "^1.7.0",
    "zustand": "^5.0.0",
    "@tanstack/react-query": "^5.60.0",
    "react-hook-form": "^7.53.0",
    "@hookform/resolvers": "^3.9.0",
    "zod": "^3.23.0",
    "@expo/vector-icons": "^14.0.0",
    "react-native-chart-kit": "^6.12.0",
    "react-native-maps": "1.18.0",
    "date-fns": "^4.1.0"
  },
  "devDependencies": {
    "@types/react": "~18.3.0",
    "typescript": "~5.3.0"
  }
}
```

---

## Getting Started Commands

```bash
# Create new Expo project
npx create-expo-app@latest gymify-mobile --template expo-template-blank-typescript

# Navigate to project
cd gymify-mobile

# Install dependencies
npx expo install expo-router expo-secure-store expo-location expo-notifications
npm install axios zustand @tanstack/react-query react-hook-form @hookform/resolvers zod
npm install react-native-chart-kit date-fns

# Start development server
npx expo start
```

---

## Notes for LLM Implementation

1. **Single App, Multiple Roles**: This is ONE app that shows different screens based on user role after login
2. **Role Detection**: After login, check `user.roles` array and determine primary role
3. **Navigation**: Use Expo Router's file-based routing with conditional tab rendering
4. **State Management**: Use Zustand for auth state, TanStack Query for server state
5. **API Calls**: All API calls go through the axios client with automatic token refresh
6. **Secure Storage**: Store tokens in Expo SecureStore, never in AsyncStorage
7. **Forms**: Use React Hook Form + Zod for all forms
8. **India Context**: Phone numbers use +91, currency is INR (₹), cities like Bengaluru
9. **Offline**: Handle network errors gracefully, show appropriate messages
10. **Pull to Refresh**: Implement on all list screens
