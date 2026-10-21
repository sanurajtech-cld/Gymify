package backend.gymify.nutrition.integration.ai;

import backend.gymify.nutrition.dto.response.AiRecognitionResult;
import backend.gymify.nutrition.dto.response.DetectedFoodItem;
import backend.gymify.nutrition.exception.FoodAnalysisException;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.math.BigDecimal;
import java.util.Base64;
import java.util.List;
import java.util.Map;

@Component
public class VisionAiClient {

    private static final Logger log = LoggerFactory.getLogger(VisionAiClient.class);

    private static final String MODEL = "gpt-4o";
    private static final String PROMPT_VERSION = "v1.0";
    private static final String SYSTEM_PROMPT = """
            You are a food recognition assistant. Analyze the food image and return a JSON object with this exact structure:
            {
              "foodItems": [
                {
                  "detectedName": "food name",
                  "estimatedWeightGrams": 150,
                  "minimumWeightGrams": 120,
                  "maximumWeightGrams": 180,
                  "confidence": "high|medium|low"
                }
              ],
              "needsUserConfirmation": true
            }
            Only return valid JSON. Do not include markdown or explanation.
            """;

    private final WebClient webClient;
    private final ObjectMapper objectMapper;

    @Value("${gymify.ai.openai.api-key:}")
    private String apiKey;

    public VisionAiClient(WebClient.Builder webClientBuilder, ObjectMapper objectMapper) {
        this.webClient = webClientBuilder
                .baseUrl("https://api.openai.com/v1")
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();
        this.objectMapper = objectMapper;
    }

    public AiRecognitionResult recognizeFood(byte[] imageBytes, String mimeType) {
        if (apiKey == null || apiKey.isBlank()) {
            log.warn("OpenAI API key not configured — returning stub recognition result");
            return stubResult();
        }

        String base64Image = Base64.getEncoder().encodeToString(imageBytes);
        String dataUrl = "data:" + mimeType + ";base64," + base64Image;

        Map<String, Object> requestBody = Map.of(
                "model", MODEL,
                "messages", List.of(
                        Map.of("role", "system", "content", SYSTEM_PROMPT),
                        Map.of("role", "user", "content", List.of(
                                Map.of("type", "image_url", "image_url", Map.of("url", dataUrl)),
                                Map.of("type", "text", "text", "Identify all food items in this image with estimated weights.")
                        ))
                ),
                "max_tokens", 1000
        );

        try {
            String rawJson = webClient.post()
                    .uri("/chat/completions")
                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + apiKey)
                    .bodyValue(requestBody)
                    .retrieve()
                    .bodyToMono(OpenAiChatResponse.class)
                    .map(r -> r.choices().get(0).message().content())
                    .block();

            AiRawResult raw = objectMapper.readValue(rawJson, AiRawResult.class);
            return new AiRecognitionResult(raw.foodItems(), raw.needsUserConfirmation(), MODEL, PROMPT_VERSION);

        } catch (Exception e) {
            log.error("Vision AI call failed", e);
            throw new FoodAnalysisException("Food recognition failed: " + e.getMessage());
        }
    }

    private AiRecognitionResult stubResult() {
        return new AiRecognitionResult(
                List.of(new DetectedFoodItem("Unknown food", new BigDecimal("100"),
                        new BigDecimal("80"), new BigDecimal("120"), "low")),
                true, "stub", PROMPT_VERSION
        );
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    record OpenAiChatResponse(List<Choice> choices) {}

    @JsonIgnoreProperties(ignoreUnknown = true)
    record Choice(Message message) {}

    @JsonIgnoreProperties(ignoreUnknown = true)
    record Message(String content) {}

    @JsonIgnoreProperties(ignoreUnknown = true)
    record AiRawResult(
            @JsonProperty("foodItems") List<DetectedFoodItem> foodItems,
            @JsonProperty("needsUserConfirmation") boolean needsUserConfirmation
    ) {}
}
