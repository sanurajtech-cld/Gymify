Summary

Test@123

Files Created/Modified:
009-sample-data.yaml - Liquibase changelog with sample data (for PostgreSQL/prod with Liquibase)

Uses context: sample-data so it only runs when that context is enabled

SampleDataLoader.java - Java-based data loader (for H2/dev mode)

Controlled by gymify.sample-data.enabled=true flag

Runs on application startup via CommandLineRunner

application.yml - Updated with:

gymify.sample-data.enabled: true for dev profile

gymify.sample-data.enabled: false for prod profile

liquibase.contexts: "!sample-data" for prod (excludes sample data)

db.changelog-master.yaml - Added reference to 009-sample-data.yaml

Sample Data Includes:
Entity	Count	Details
Users	6	Admin, Owner, 2 Trainers, 2 Customers
Roles	4	CUSTOMER, TRAINER, GYM_OWNER, ADMIN
Facilities	6	AC, Parking, Locker, Steam, Cardio, Training
Gyms	2	Koramangala & Indiranagar (Bengaluru)
Membership Plans	3	Monthly, Quarterly, Annual
Memberships	2	Active memberships for customers
Payments	2	Completed payments
Exercise Categories	3	Strength, Cardio, Flexibility
Muscle Groups	7	Chest, Back, Shoulders, Biceps, Triceps, Quads, Core
Equipment	6	Barbell, Dumbbell, Treadmill, Pull-up Bar, Bench, Bodyweight
Exercises	5	Bench Press, Squats, Pull-ups, Treadmill, Plank
Workout Plans	1	Beginner Full Body
Workout Assignments	1	Active assignment
Body Measurements	2	Progress tracking
Notifications	2	Sample notifications
Test Credentials (Password: Test@123):
Admin:

Owner:

Trainer: ,

Customer: ,

To Disable Sample Data:
Set gymify.sample-data.enabled: false in application.yml or via environment variable.

