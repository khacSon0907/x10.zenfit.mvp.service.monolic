package x10.zenfit.mvp.service.service.chatService;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class OpenAiChatAdvisorService {

    private static final String SYSTEM_PROMPT = """
            Ban la tu van vien thoi trang cua shop PoloMan.
            Chi tu van dua tren du lieu san pham duoc cung cap trong context.
            Neu khach hoi so sanh, hay so sanh gia, mau, size, ton kho, uu/nhuoc diem theo nhu cau.
            Neu khach hoi goi y, hay dua ra 1-3 lua chon ro rang va ly do ngan gon.
            Khong bia san pham, gia, size, mau hoac ton kho khong co trong context.
            Neu context khong du de tra loi, hay noi can admin ho tro them.
            Tra loi bang tieng Viet tu nhien, than thien, ngan gon, khong markdown bang.
            """;

    private final RestTemplate restTemplate;

    @Value("${app.openai.enabled:true}")
    private boolean enabled;

    @Value("${app.openai.api-key:}")
    private String apiKey;

    @Value("${app.openai.model:gpt-5.1}")
    private String model;

    @Value("${app.openai.responses-url:https://api.openai.com/v1/responses}")
    private String responsesUrl;

    @Value("${app.openai.max-output-tokens:700}")
    private int maxOutputTokens;

    public Optional<String> advise(String customerQuestion, String productContext) {
        if (!enabled || !StringUtils.hasText(apiKey) || !StringUtils.hasText(customerQuestion)
                || !StringUtils.hasText(productContext)) {
            return Optional.empty();
        }

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(apiKey);

        Map<String, Object> body = Map.of(
                "model", model,
                "instructions", SYSTEM_PROMPT,
                "input", buildInput(customerQuestion, productContext),
                "max_output_tokens", maxOutputTokens
        );

        try {
            JsonNode response = restTemplate.postForObject(
                    responsesUrl,
                    new HttpEntity<>(body, headers),
                    JsonNode.class
            );
            return extractOutputText(response);
        } catch (RestClientException ex) {
            log.warn("OpenAI response failed: {}", ex.getMessage());
            return Optional.empty();
        }
    }

    private List<Map<String, String>> buildInput(String customerQuestion, String productContext) {
        return List.of(
                Map.of(
                        "role", "user",
                        "content", "Du lieu san pham trong database:\n" + productContext
                                + "\n\nCau hoi khach hang:\n" + customerQuestion
                )
        );
    }

    private Optional<String> extractOutputText(JsonNode response) {
        if (response == null) {
            return Optional.empty();
        }

        JsonNode outputText = response.path("output_text");
        if (outputText.isTextual() && StringUtils.hasText(outputText.asText())) {
            return Optional.of(outputText.asText().trim());
        }

        JsonNode output = response.path("output");
        if (output.isArray()) {
            StringBuilder text = new StringBuilder();
            for (JsonNode item : output) {
                JsonNode content = item.path("content");
                if (!content.isArray()) {
                    continue;
                }
                for (JsonNode contentItem : content) {
                    JsonNode value = contentItem.path("text");
                    if (value.isTextual() && StringUtils.hasText(value.asText())) {
                        if (!text.isEmpty()) {
                            text.append("\n");
                        }
                        text.append(value.asText().trim());
                    }
                }
            }
            if (!text.isEmpty()) {
                return Optional.of(text.toString());
            }
        }

        return Optional.empty();
    }
}

