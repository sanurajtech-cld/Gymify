package backend.gymify.config;

import backend.gymify.user.entity.Role;
import backend.gymify.user.entity.User;
import backend.gymify.user.repository.RoleRepository;
import backend.gymify.user.repository.UserRepository;
import backend.gymify.gym.entity.*;
import backend.gymify.gym.repository.FacilityRepository;
import backend.gymify.gym.repository.GymRepository;
import backend.gymify.gym.repository.GymTrainerRepository;
import backend.gymify.membership.entity.*;
import backend.gymify.membership.repository.*;
import backend.gymify.exercise.entity.*;
import backend.gymify.exercise.repository.*;
import backend.gymify.workout.entity.*;
import backend.gymify.workout.repository.*;
import backend.gymify.tracking.entity.*;
import backend.gymify.tracking.repository.*;
import backend.gymify.notification.entity.*;
import backend.gymify.notification.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.*;
import java.util.*;

@Slf4j
@Component
@RequiredArgsConstructor
@ConditionalOnProperty(name = "gymify.sample-data.enabled", havingValue = "true")
public class SampleDataLoader implements CommandLineRunner {

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final FacilityRepository facilityRepository;
    private final GymRepository gymRepository;
    private final GymTrainerRepository gymTrainerRepository;
    private final MembershipPlanRepository membershipPlanRepository;
    private final MembershipRepository membershipRepository;
    private final PaymentRepository paymentRepository;
    private final ExerciseCategoryRepository exerciseCategoryRepository;
    private final MuscleGroupRepository muscleGroupRepository;
    private final EquipmentRepository equipmentRepository;
    private final ExerciseRepository exerciseRepository;
    private final WorkoutPlanRepository workoutPlanRepository;
    private final WorkoutDayRepository workoutDayRepository;
    private final WorkoutAssignmentRepository workoutAssignmentRepository;
    private final BodyMeasurementRepository bodyMeasurementRepository;
    private final WorkoutLogRepository workoutLogRepository;
    private final ExerciseLogRepository exerciseLogRepository;
    private final NotificationRepository notificationRepository;
    private final NotificationPreferenceRepository notificationPreferenceRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public void run(String... args) {
        if (userRepository.count() > 0) {
            log.info("Sample data already exists, skipping...");
            return;
        }
        log.info("Loading sample data for Gymify...");
        
        createRoles();
        Map<String, User> users = createUsers();
        List<Facility> facilities = createFacilities();
        List<Gym> gyms = createGyms(users.get("owner"), facilities);
        assignTrainersToGyms(gyms, users);
        List<MembershipPlan> plans = createMembershipPlans(gyms);
        createMemberships(users, gyms, plans);
        Map<String, ExerciseCategory> categories = createExerciseCategories();
        Map<String, MuscleGroup> muscleGroups = createMuscleGroups();
        Map<String, Equipment> equipment = createEquipment();
        List<Exercise> exercises = createExercises(categories, muscleGroups, equipment, users.get("admin"));
        createWorkoutPlans(users, gyms, exercises);
        createBodyMeasurements(users);
        createNotifications(users);
        
        log.info("Sample data loaded successfully!");
        logTestCredentials();
    }

    private void logTestCredentials() {
        log.info("=".repeat(50));
        log.info("TEST CREDENTIALS (Password: Test@123)");
        log.info("=".repeat(50));
        log.info("Admin:    admin@gymify.com");
        log.info("Owner:    owner@fitnessfirst.com");
        log.info("Trainer:  trainer.arjun@gymify.com");
        log.info("Trainer:  trainer.priya@gymify.com");
        log.info("Customer: customer.amit@gmail.com");
        log.info("Customer: customer.sneha@gmail.com");
        log.info("=".repeat(50));
    }

    private void createRoles() {
        if (roleRepository.count() == 0) {
            saveRole(Role.RoleName.CUSTOMER, "Gym customer/member");
            saveRole(Role.RoleName.TRAINER, "Fitness trainer");
            saveRole(Role.RoleName.GYM_OWNER, "Gym owner/manager");
            saveRole(Role.RoleName.ADMIN, "Platform administrator");
        }
    }

    private void saveRole(Role.RoleName name, String description) {
        Role role = new Role();
        role.setName(name);
        role.setDescription(description);
        roleRepository.save(role);
    }

    private Map<String, User> createUsers() {
        Map<String, User> users = new HashMap<>();
        String encodedPassword = passwordEncoder.encode("Test@123");

        Role adminRole = roleRepository.findByName(Role.RoleName.ADMIN).orElseThrow();
        Role ownerRole = roleRepository.findByName(Role.RoleName.GYM_OWNER).orElseThrow();
        Role trainerRole = roleRepository.findByName(Role.RoleName.TRAINER).orElseThrow();
        Role customerRole = roleRepository.findByName(Role.RoleName.CUSTOMER).orElseThrow();

        users.put("admin", createUser("admin@gymify.com", "+919876543210", encodedPassword,
                "Rajesh", "Kumar", LocalDate.of(1985, 3, 15), User.Gender.MALE, adminRole));
        users.put("owner", createUser("owner@fitnessfirst.com", "+919876543211", encodedPassword,
                "Vikram", "Sharma", LocalDate.of(1980, 7, 20), User.Gender.MALE, ownerRole));
        users.put("trainer1", createUser("trainer.arjun@gymify.com", "+919876543212", encodedPassword,
                "Arjun", "Reddy", LocalDate.of(1992, 11, 10), User.Gender.MALE, trainerRole));
        users.put("trainer2", createUser("trainer.priya@gymify.com", "+919876543213", encodedPassword,
                "Priya", "Nair", LocalDate.of(1995, 4, 25), User.Gender.FEMALE, trainerRole));
        users.put("customer1", createUser("customer.amit@gmail.com", "+919876543214", encodedPassword,
                "Amit", "Patel", LocalDate.of(1998, 8, 12), User.Gender.MALE, customerRole));
        users.put("customer2", createUser("customer.sneha@gmail.com", "+919876543215", encodedPassword,
                "Sneha", "Iyer", LocalDate.of(1996, 2, 28), User.Gender.FEMALE, customerRole));

        return users;
    }

    private User createUser(String email, String phone, String password, String firstName, 
            String lastName, LocalDate dob, User.Gender gender, Role role) {
        User user = new User();
        user.setEmail(email);
        user.setPhone(phone);
        user.setPasswordHash(password);
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setDateOfBirth(dob);
        user.setGender(gender);
        user.setIsActive(true);
        user.setIsEmailVerified(true);
        user.setRoles(Set.of(role));
        return userRepository.save(user);
    }

    private List<Facility> createFacilities() {
        return List.of(
            createFacility("Air Conditioning", "ac_unit", "Fully air-conditioned"),
            createFacility("Parking", "local_parking", "Free parking"),
            createFacility("Locker Room", "lock", "Secure lockers with showers"),
            createFacility("Steam Room", "hot_tub", "Steam and sauna"),
            createFacility("Cardio Zone", "directions_run", "Dedicated cardio area"),
            createFacility("Personal Training", "fitness_center", "Certified trainers")
        );
    }

    private Facility createFacility(String name, String icon, String description) {
        Facility f = new Facility();
        f.setName(name);
        f.setIcon(icon);
        f.setDescription(description);
        return facilityRepository.save(f);
    }

    private List<Gym> createGyms(User owner, List<Facility> facilities) {
        Gym gym1 = createGym(owner, "Fitness First Koramangala",
                "Premium fitness center in Koramangala with state-of-the-art equipment",
                "80 Feet Road, 4th Block", "Near Sony Signal", "Bengaluru", "Karnataka",
                "560034", "+918041234567", "koramangala@fitnessfirst.com", facilities);

        Gym gym2 = createGym(owner, "Fitness First Indiranagar",
                "Modern gym with CrossFit zone and yoga studio",
                "100 Feet Road, HAL 2nd Stage", "Above Starbucks", "Bengaluru", "Karnataka",
                "560038", "+918041234568", "indiranagar@fitnessfirst.com", facilities.subList(0, 5));

        return List.of(gym1, gym2);
    }

    private Gym createGym(User owner, String name, String desc, String addr1, String addr2,
            String city, String state, String postal, String phone, String email, List<Facility> facilities) {
        Gym gym = new Gym();
        gym.setOwner(owner);
        gym.setName(name);
        gym.setDescription(desc);
        gym.setAddressLine1(addr1);
        gym.setAddressLine2(addr2);
        gym.setCity(city);
        gym.setState(state);
        gym.setCountry("India");
        gym.setPostalCode(postal);
        gym.setPhone(phone);
        gym.setEmail(email);
        gym.setStatus(Gym.GymStatus.APPROVED);
        gym.setIsActive(true);
        gym.setFacilities(new HashSet<>(facilities));
        gym = gymRepository.save(gym);
        addOperatingHours(gym);
        return gym;
    }

    private void addOperatingHours(Gym gym) {
        for (int day = 0; day <= 6; day++) {
            GymOperatingHours hours = new GymOperatingHours();
            hours.setGym(gym);
            hours.setDayOfWeek((short) day);
            hours.setIsClosed(false);
            hours.setOpenTime(day == 0 ? LocalTime.of(7, 0) : LocalTime.of(5, 30));
            hours.setCloseTime(day == 0 ? LocalTime.of(20, 0) : LocalTime.of(22, 0));
            gym.getOperatingHours().add(hours);
        }
        gymRepository.save(gym);
    }

    private void assignTrainersToGyms(List<Gym> gyms, Map<String, User> users) {
        Gym gym1 = gyms.get(0);
        GymTrainer gt1 = new GymTrainer();
        gt1.setGym(gym1);
        gt1.setTrainer(users.get("trainer1"));
        gt1.setIsActive(true);
        gymTrainerRepository.save(gt1);

        GymTrainer gt2 = new GymTrainer();
        gt2.setGym(gym1);
        gt2.setTrainer(users.get("trainer2"));
        gt2.setIsActive(true);
        gymTrainerRepository.save(gt2);
    }

    private List<MembershipPlan> createMembershipPlans(List<Gym> gyms) {
        Gym gym1 = gyms.get(0);
        return List.of(
            createPlan(gym1, "Monthly Basic", "Basic monthly membership", 30, "1500.00", false, 3),
            createPlan(gym1, "Quarterly Premium", "3-month premium with trainer", 90, "6000.00", true, 10),
            createPlan(gym1, "Annual Gold", "Annual membership with all facilities", 365, "18000.00", true, 30)
        );
    }

    private MembershipPlan createPlan(Gym gym, String name, String desc, int days, String price, 
            boolean trainer, int freeze) {
        MembershipPlan plan = new MembershipPlan();
        plan.setGym(gym);
        plan.setName(name);
        plan.setDescription(desc);
        plan.setDurationDays(days);
        plan.setPrice(new BigDecimal(price));
        plan.setCurrency("INR");
        plan.setIncludesTrainer(trainer);
        plan.setMaxFreezeDays(freeze);
        plan.setIsActive(true);
        return membershipPlanRepository.save(plan);
    }

    private void createMemberships(Map<String, User> users, List<Gym> gyms, List<MembershipPlan> plans) {
        Membership m1 = new Membership();
        m1.setUser(users.get("customer1"));
        m1.setGym(gyms.get(0));
        m1.setPlan(plans.get(1));
        m1.setAssignedTrainer(users.get("trainer1"));
        m1.setStartDate(LocalDate.now());
        m1.setEndDate(LocalDate.now().plusDays(90));
        m1.setStatus(Membership.MembershipStatus.ACTIVE);
        m1 = membershipRepository.save(m1);
        createPayment(m1, users.get("customer1"), "6000.00", "UPI");

        Membership m2 = new Membership();
        m2.setUser(users.get("customer2"));
        m2.setGym(gyms.get(0));
        m2.setPlan(plans.get(0));
        m2.setStartDate(LocalDate.now());
        m2.setEndDate(LocalDate.now().plusDays(30));
        m2.setStatus(Membership.MembershipStatus.ACTIVE);
        m2 = membershipRepository.save(m2);
        createPayment(m2, users.get("customer2"), "1500.00", "CARD");
    }

    private void createPayment(Membership membership, User user, String amount, String method) {
        Payment payment = new Payment();
        payment.setMembership(membership);
        payment.setUser(user);
        payment.setAmount(new BigDecimal(amount));
        payment.setCurrency("INR");
        payment.setPaymentMethod(method);
        payment.setGateway("RAZORPAY");
        payment.setStatus(Payment.PaymentStatus.SUCCESS);
        payment.setPaidAt(Instant.now());
        paymentRepository.save(payment);
    }

    private Map<String, ExerciseCategory> createExerciseCategories() {
        Map<String, ExerciseCategory> categories = new HashMap<>();
        categories.put("strength", createCategory("Strength Training", "Building muscle strength", "fitness_center"));
        categories.put("cardio", createCategory("Cardio", "Cardiovascular exercises", "directions_run"));
        categories.put("flexibility", createCategory("Flexibility", "Stretching exercises", "self_improvement"));
        return categories;
    }

    private ExerciseCategory createCategory(String name, String desc, String icon) {
        ExerciseCategory cat = new ExerciseCategory();
        cat.setName(name);
        cat.setDescription(desc);
        cat.setIcon(icon);
        return exerciseCategoryRepository.save(cat);
    }

    private Map<String, MuscleGroup> createMuscleGroups() {
        Map<String, MuscleGroup> groups = new HashMap<>();
        groups.put("chest", createMuscleGroup("Chest", "UPPER_BODY"));
        groups.put("back", createMuscleGroup("Back", "UPPER_BODY"));
        groups.put("shoulders", createMuscleGroup("Shoulders", "UPPER_BODY"));
        groups.put("biceps", createMuscleGroup("Biceps", "ARMS"));
        groups.put("triceps", createMuscleGroup("Triceps", "ARMS"));
        groups.put("quads", createMuscleGroup("Quadriceps", "LEGS"));
        groups.put("core", createMuscleGroup("Core", "CORE"));
        return groups;
    }

    private MuscleGroup createMuscleGroup(String name, String region) {
        MuscleGroup mg = new MuscleGroup();
        mg.setName(name);
        mg.setBodyRegion(region);
        return muscleGroupRepository.save(mg);
    }

    private Map<String, Equipment> createEquipment() {
        Map<String, Equipment> equip = new HashMap<>();
        equip.put("barbell", createEquipment("Barbell", "Olympic barbell"));
        equip.put("dumbbell", createEquipment("Dumbbell", "Free weights"));
        equip.put("treadmill", createEquipment("Treadmill", "Cardio machine"));
        equip.put("pullup", createEquipment("Pull-up Bar", "Fixed bar"));
        equip.put("bench", createEquipment("Bench Press", "Flat/incline bench"));
        equip.put("bodyweight", createEquipment("Bodyweight", "No equipment"));
        return equip;
    }

    private Equipment createEquipment(String name, String desc) {
        Equipment eq = new Equipment();
        eq.setName(name);
        eq.setDescription(desc);
        return equipmentRepository.save(eq);
    }

    private List<Exercise> createExercises(Map<String, ExerciseCategory> categories,
            Map<String, MuscleGroup> muscleGroups, Map<String, Equipment> equipment, User admin) {
        List<Exercise> exercises = new ArrayList<>();

        exercises.add(createExercise("Barbell Bench Press", "Classic chest exercise",
                "Lie on bench, grip barbell, lower to chest, press up",
                categories.get("strength"), Exercise.Difficulty.INTERMEDIATE, Exercise.ExerciseType.STRENGTH,
                "8.5", admin, Set.of(muscleGroups.get("chest"), muscleGroups.get("triceps")),
                Set.of(equipment.get("barbell"), equipment.get("bench"))));

        exercises.add(createExercise("Squats", "Fundamental leg exercise",
                "Stand shoulder-width, lower by bending knees, return to standing",
                categories.get("strength"), Exercise.Difficulty.BEGINNER, Exercise.ExerciseType.STRENGTH,
                "9.0", admin, Set.of(muscleGroups.get("quads")), Set.of(equipment.get("bodyweight"))));

        exercises.add(createExercise("Pull-ups", "Upper body pulling exercise",
                "Hang from bar, pull up until chin over bar, lower with control",
                categories.get("strength"), Exercise.Difficulty.INTERMEDIATE, Exercise.ExerciseType.STRENGTH,
                "8.0", admin, Set.of(muscleGroups.get("back"), muscleGroups.get("biceps")),
                Set.of(equipment.get("pullup"))));

        exercises.add(createExercise("Treadmill Running", "Cardio on treadmill",
                "Set speed and incline, maintain steady pace",
                categories.get("cardio"), Exercise.Difficulty.BEGINNER, Exercise.ExerciseType.CARDIO,
                "12.0", admin, Set.of(), Set.of(equipment.get("treadmill"))));

        exercises.add(createExercise("Plank", "Core stabilization",
                "Hold push-up position on forearms, keep body straight",
                categories.get("strength"), Exercise.Difficulty.BEGINNER, Exercise.ExerciseType.STRENGTH,
                "5.0", admin, Set.of(muscleGroups.get("core")), Set.of(equipment.get("bodyweight"))));

        return exercises;
    }

    private Exercise createExercise(String name, String desc, String instructions, ExerciseCategory cat,
            Exercise.Difficulty diff, Exercise.ExerciseType type, String calories, User admin,
            Set<MuscleGroup> muscles, Set<Equipment> equip) {
        Exercise ex = new Exercise();
        ex.setName(name);
        ex.setDescription(desc);
        ex.setInstructions(instructions);
        ex.setCategory(cat);
        ex.setDifficulty(diff);
        ex.setExerciseType(type);
        ex.setCaloriesPerMinute(new BigDecimal(calories));
        ex.setIsActive(true);
        ex.setCreatedBy(admin);
        ex.setMuscleGroups(muscles);
        ex.setEquipment(equip);
        return exerciseRepository.save(ex);
    }

    private void createWorkoutPlans(Map<String, User> users, List<Gym> gyms, List<Exercise> exercises) {
        WorkoutPlan plan = new WorkoutPlan();
        plan.setTrainer(users.get("trainer1"));
        plan.setName("Beginner Full Body");
        plan.setDescription("Perfect for beginners starting their fitness journey");
        plan.setGoal("General Fitness");
        plan.setDifficulty(WorkoutPlan.Difficulty.BEGINNER);
        plan.setDurationWeeks(4);
        plan.setIsTemplate(true);
        plan.setIsPublished(true);
        plan = workoutPlanRepository.save(plan);

        WorkoutDay day1 = new WorkoutDay();
        day1.setPlan(plan);
        day1.setDayOfWeek((short) 1);
        day1.setName("Push Day");
        day1.setIsRestDay(false);
        day1.setNotes("Focus on chest, shoulders, triceps");
        day1 = workoutDayRepository.save(day1);

        WorkoutExercise we = new WorkoutExercise();
        we.setWorkoutDay(day1);
        we.setExercise(exercises.get(0));
        we.setDisplayOrder(1);
        we.setSets(4);
        we.setReps("8-10");
        we.setRestSeconds(90);
        we.setWeightKg(new BigDecimal("40.0"));
        day1.getExercises().add(we);
        workoutDayRepository.save(day1);

        WorkoutAssignment assignment = new WorkoutAssignment();
        assignment.setPlan(plan);
        assignment.setCustomer(users.get("customer1"));
        assignment.setTrainer(users.get("trainer1"));
        assignment.setGym(gyms.get(0));
        assignment.setStartDate(LocalDate.now());
        assignment.setEndDate(LocalDate.now().plusDays(28));
        assignment.setStatus(WorkoutAssignment.AssignmentStatus.ACTIVE);
        assignment.setNotes("Starting beginner plan");
        assignment = workoutAssignmentRepository.save(assignment);

        WorkoutLog wlog = new WorkoutLog();
        wlog.setUser(users.get("customer1"));
        wlog.setAssignment(assignment);
        wlog.setWorkoutDay(day1);
        wlog.setWorkoutDate(LocalDate.now().minusDays(1));
        wlog.setStartedAt(Instant.now().minusSeconds(3600));
        wlog.setCompletedAt(Instant.now());
        wlog.setDurationMinutes(55);
        wlog.setCaloriesBurned(420);
        wlog.setStatus(WorkoutLog.WorkoutLogStatus.COMPLETED);
        wlog.setNotes("Great session");
        wlog = workoutLogRepository.save(wlog);

        ExerciseLog elog = new ExerciseLog();
        elog.setWorkoutLog(wlog);
        elog.setExercise(exercises.get(0));
        elog.setSetsCompleted(4);
        elog.setRepsCompleted("10,10,8,8");
        elog.setWeightUsedKg(new BigDecimal("40.0"));
        elog.setIsCompleted(true);
        elog.setNotes("Felt strong");
        exerciseLogRepository.save(elog);
    }

    private void createBodyMeasurements(Map<String, User> users) {
        createMeasurement(users.get("customer1"), LocalDate.now().minusDays(30),
                "75.5", "175.0", "22.0", "98.0", "86.0", "96.0", "32.0", "54.0", "Initial measurement");
        createMeasurement(users.get("customer1"), LocalDate.now(),
                "73.0", "175.0", "19.0", "100.0", "82.0", "94.0", "34.0", "56.0", "Great progress!");
    }

    private void createMeasurement(User user, LocalDate date, String weight, String height,
            String fat, String chest, String waist, String hips, String biceps, String thighs, String notes) {
        BodyMeasurement bm = new BodyMeasurement();
        bm.setUser(user);
        bm.setMeasuredAt(date);
        bm.setWeightKg(new BigDecimal(weight));
        bm.setHeightCm(new BigDecimal(height));
        bm.setBodyFatPercentage(new BigDecimal(fat));
        bm.setChestCm(new BigDecimal(chest));
        bm.setWaistCm(new BigDecimal(waist));
        bm.setHipsCm(new BigDecimal(hips));
        bm.setBicepsCm(new BigDecimal(biceps));
        bm.setThighsCm(new BigDecimal(thighs));
        bm.setNotes(notes);
        bodyMeasurementRepository.save(bm);
    }

    private void createNotifications(Map<String, User> users) {
        createNotification(users.get("customer1"), Notification.NotificationType.WORKOUT_REMINDER,
                "Time to workout!", "Your Push Day workout is scheduled for today.", false);
        createNotification(users.get("customer1"), Notification.NotificationType.MEMBERSHIP_EXPIRY,
                "Membership Active", "Your Quarterly Premium membership is now active.", true);

        createNotificationPref(users.get("customer1"), true, true, true, false, LocalTime.of(7, 0));
        createNotificationPref(users.get("customer2"), true, true, true, true, LocalTime.of(6, 30));
    }

    private void createNotification(User user, Notification.NotificationType type, String title, 
            String message, boolean read) {
        Notification n = new Notification();
        n.setUser(user);
        n.setType(type);
        n.setTitle(title);
        n.setMessage(message);
        n.setIsRead(read);
        if (read) n.setReadAt(Instant.now());
        notificationRepository.save(n);
    }

    private void createNotificationPref(User user, boolean workout, boolean membership, 
            boolean plan, boolean promo, LocalTime time) {
        NotificationPreference np = new NotificationPreference();
        np.setUser(user);
        np.setWorkoutReminders(workout);
        np.setMembershipAlerts(membership);
        np.setPlanUpdates(plan);
        np.setPromotional(promo);
        np.setReminderTime(time);
        notificationPreferenceRepository.save(np);
    }
}
