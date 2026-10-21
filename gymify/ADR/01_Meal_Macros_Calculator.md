
export OPENAI_API_KEY=sk-...


Project Plan: AI Food Macro Analyzer
Goal: Build a backend service where a user uploads a photo of food and receives estimated calories, protein, carbohydrates, fat, fibre and other nutritional values.

I recommend building this as a Java backend project first, with MySQL for persistence. You can add an Android/iOS application once the API is working.
1. Final technology stack


REST APIs, business logic, validation and integrations. Spring Boot 4.1.1 supports Java 21.

Home


MySQL 

Store foods, nutrition facts, meal records, portions and calculation results. MySQL 8.4 is an LTS release track.

MySQL


Vision AI API

Analyze food photographs, identify visible items and return structured results. OpenAI's Responses API supports image inputs.

OpenAI API







Java Nutrition Engine

Calculate the final nutrients using verified food records and portion weights, rather than relying on AI-generated macro values.




Maven, JUnit, Mockito, Testcontainers and Flyway

Build management, testing, MySQL integration tests and database migrations.



Use a monolithic Spring Boot application for version one. You do not need Kafka, Redis or microservices yet.
2. Architecture
1. Image upload API

Validate and resize the image

2. Food Recognition Service

Call vision AI and identify food items and estimated portions

3. Food Matching Service

Map detected names to canonical foods in MySQL

4. Nutrition Calculation Service

Calculate nutrients using the confirmed weight and database values

5. Meal Result

Return macros, calories, per-item breakdown and uncertainty


The most important design decision is to separate recognition from calculation. The vision model recognizes food and estimates quantity. Java matches that food to a nutrition record and calculates the result.
3. Project structure
   Use a feature-oriented, layered package structure.
   food-macro-analyzer/
   ├── pom.xml
   ├── src/main/java/com/example/foodmacro/
   │   ├── FoodMacroApplication.java
   │   ├── config/
   │   │   ├── AiConfig.java
   │   │   └── SecurityConfig.java
   │   ├── api/
   │   │   ├── MealAnalysisController.java
   │   │   ├── MealController.java
   │   │   └── NutritionController.java
   │   ├── dto/
   │   │   ├── request/
   │   │   └── response/
   │   ├── service/
   │   │   ├── FoodRecognitionService.java
   │   │   ├── FoodMatchingService.java
   │   │   ├── NutritionCalculationService.java
   │   │   └── MealService.java
   │   ├── integration/
   │   │   ├── ai/
   │   │   │   └── VisionAiClient.java
   │   │   └── nutrition/
   │   │       └── NutritionDataProvider.java
   │   ├── entity/
   │   ├── repository/
   │   └── exception/
   │       ├── GlobalExceptionHandler.java
   │       └── FoodAnalysisException.java
   └── src/main/resources/
   ├── application.yml
   └── db/migration/
   └── V1__create_initial_schema.sql


This design keeps the AI provider replaceable. Your controllers should not directly call the AI API or calculate macros.
4. MySQL database design
   Start with these four core tables, plus a user table if personal accounts are in scope.
   Table	Purpose
   food_catalog	Canonical food names, preparation type and nutrition per 100 g
   food_aliases	Alternative names used to match AI output with foods
   meal_entries	Meal record, analysis status and timestamps
   meal_items	Individual foods, estimated/confirmed weight and calculated nutrients
   users	Accounts and ownership of meal history
   food_catalog
   Recommended columns:
   id
   canonical_name
   preparation_state
   category
   source_name
   source_food_id
   basis_grams
   calories_kcal_per_100g
   protein_g_per_100g
   carbs_g_per_100g
   fat_g_per_100g
   fibre_g_per_100g
   is_active
   created_at
   updated_at


Store separate records for foods such as raw chicken breast and cooked chicken breast. Their nutritional values per 100 g are not necessarily the same.
meal_entries
id
user_id
status
model_name
prompt_version
image_sha256
created_at
confirmed_at


Suggested status values: PROCESSING, NEEDS_CONFIRMATION, CONFIRMED, FAILED.

You can avoid storing the original photo by default. The hash can help identify repeat uploads without retaining the image itself.
meal_items
id
meal_id
food_id
detected_food_name
estimated_weight_g
confirmed_weight_g
confidence_score
calories_kcal
protein_g
carbs_g
fat_g
fibre_g
created_at
updated_at


Keep the calculated nutrient values as a snapshot for each meal item. This prevents a future nutrition-database correction from silently changing the user's historical meal results.

Use Flyway to create and version the schema rather than relying on Hibernate to update production tables.
5. Nutrition data: where do the values come from?
   You need a reliable source of nutritional facts.

For the MVP:
- Use USDA FoodData Central for general food records; its API offers food search and food-detail endpoints. It requires an API key and documents a default rate limit of 1,000 requests per hour per IP.

  USDA FoodData Central
- Build a curated catalogue of commonly consumed Indian foods and cooked dishes.
- Store approved nutrition records in MySQL so each meal does not require a separate external nutrition API call.
- Before incorporating ICMR-NIN's Indian Food Composition Tables into a product, review its reuse terms: the published notice requires prior written permission for storing the data electronically to create a product.

  National Institute of Nutrition
  For prepared dishes such as chicken curry, dosa, biryani and paneer gravy, eventually add recipes and ingredient-level calculations. A single generic nutrient value may not reflect differences in oil, ghee or ingredients.
6. API design
   Build these endpoints first.
   HTTP method	Endpoint	Function
   POST	/api/v1/meal-analyses	Upload the food photo and start analysis
   GET	/api/v1/meal-analyses/{id}	Retrieve detected items and analysis status
   PATCH	/api/v1/meal-analyses/{id}/items	Correct food names and serving sizes
   POST	/api/v1/meal-analyses/{id}/confirm	Confirm and save the meal
   GET	/api/v1/meals	Retrieve meal history
   GET	/api/v1/nutrition/daily?date=2026-10-21	Retrieve daily macro totals
   For the upload endpoint, accept multipart/form-data containing the photo.

An illustrative AI recognition result could look like this:
{
"foodItems": [
{
"detectedName": "cooked rice",
"estimatedWeightGrams": 180,
"minimumWeightGrams": 140,
"maximumWeightGrams": 220,
"confidence": "medium"
},
{
"detectedName": "chapati",
"estimatedWeightGrams": 80,
"minimumWeightGrams": 60,
"maximumWeightGrams": 100,
"confidence": "medium"
}
],
"needsUserConfirmation": true
}


These are example values for the API contract, not actual measurements.

The next screen should let the user change the food or weight. That confirmation step is important because a photo alone cannot reliably reveal exact portion weight or hidden ingredients.
7. Java nutrition calculation engine
   For a confirmed portion, calculate each nutrient independently:
   \[
   \text{Nutrient consumed}
   =
   \frac{\text{weight in grams}\times\text{nutrient per 100 g}}{100}
   \]

For example, if a food has 8 g of protein per 100 g and the serving weighs 150 g, the estimated protein intake is 12 g.

Implement this with BigDecimal, not floating-point arithmetic, for consistent rounding.
import java.math.BigDecimal;
import java.math.RoundingMode;

public final class NutritionCalculator {

    private static final BigDecimal HUNDRED =
            new BigDecimal("100");

    private NutritionCalculator() {
    }

    public static BigDecimal calculateNutrient(
            BigDecimal weightGrams,
            BigDecimal nutrientPer100Grams) {

        if (weightGrams == null
                || nutrientPer100Grams == null
                || weightGrams.signum() < 0
                || nutrientPer100Grams.signum() < 0) {
            throw new IllegalArgumentException(
                    "Weight and nutrient values must be non-negative");
        }

        return weightGrams
                .multiply(nutrientPer100Grams)
                .divide(HUNDRED, 2, RoundingMode.HALF_UP);
    }
}


Call this method for protein, carbohydrates, fat, fibre and calories. For each meal, sum the corresponding values across all confirmed food items.

Do not ask the vision model to determine the final macros. Retrieve those from your nutrition records and calculate them in Java.