package traffic.project.tool.kafka;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import traffic.project.dto.ChatGPTRequest;
import traffic.project.dto.ChatGptResponse;
import traffic.project.tool.PromptProcessor;

@Component
@RequiredArgsConstructor
public class KafkaPromptConsumer {

    private final RestTemplate restTemplate;
    private final Logger log = LoggerFactory.getLogger(KafkaPromptConsumer.class);

    @Value("${openai.model}")
    private String model;

    @Value("${openai.api.url}")
    private String apiUrl;

    @KafkaListener(topics = "gpt-prompt-topic", groupId = "gpt-benchmark-group")
    public void listen(String prompt) {
        long start = System.currentTimeMillis();
        try {
            ChatGPTRequest request = new ChatGPTRequest(model, prompt);
            restTemplate.postForObject(apiUrl, request, ChatGptResponse.class);
            long duration = System.currentTimeMillis() - start;
            log.info("[KafkaConsumer] GPT 응답 완료 - 처리 시간: {}ms", duration);
        } catch (Exception e) {
            long duration = System.currentTimeMillis() - start;
            log.error("[KafkaConsumer] GPT 호출 실패 ({}ms): {}", duration, e.getMessage());
        }
    }
}

