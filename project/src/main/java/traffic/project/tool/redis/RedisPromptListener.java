package traffic.project.tool.redis;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import traffic.project.dto.ChatGPTRequest;
import traffic.project.dto.ChatGptResponse;

@Component
@RequiredArgsConstructor
public class RedisPromptListener {

    private final StringRedisTemplate redisTemplate;
    private final RestTemplate restTemplate;

    private final Logger log = LoggerFactory.getLogger(RedisPromptListener.class);

    @Value("${openai.model}")
    private String model;

    @Value("${openai.api.url}")
    private String apiUrl;

    private static final String QUEUE_KEY = "prompt:queue";

    @Scheduled(fixedDelay = 1000)
    public void processPromptFromQueue() {
        String prompt = redisTemplate.opsForList().leftPop(QUEUE_KEY);
        if (prompt != null) {
            long start = System.currentTimeMillis();
            try {
                ChatGPTRequest request = new ChatGPTRequest(model, prompt);
                restTemplate.postForObject(apiUrl, request, ChatGptResponse.class);
                long duration = System.currentTimeMillis() - start;
                log.info("[RedisConsumer] GPT 응답 완료 - 처리 시간: {}ms", duration);
            } catch (Exception e) {
                long duration = System.currentTimeMillis() - start;
                log.error("[RedisConsumer] GPT 호출 실패 ({}ms): {}", duration, e.getMessage());
            }
        }
    }
}
