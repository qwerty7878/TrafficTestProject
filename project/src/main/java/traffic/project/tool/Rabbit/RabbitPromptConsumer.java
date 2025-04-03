package traffic.project.tool.Rabbit;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import traffic.project.dto.ChatGPTRequest;
import traffic.project.dto.ChatGptResponse;

@Component
@RequiredArgsConstructor
public class RabbitPromptConsumer {

    private final RestTemplate restTemplate;
    private final Logger log = LoggerFactory.getLogger(RabbitPromptConsumer.class);

    @Value("${openai.model}")
    private String model;

    @Value("${openai.api.url}")
    private String apiUrl;

    @RabbitListener(queues = "gpt-prompt-queue")
    public void receive(String prompt) {
        long start = System.currentTimeMillis();
        try {
            ChatGPTRequest request = new ChatGPTRequest(model, prompt);
            restTemplate.postForObject(apiUrl, request, ChatGptResponse.class);
            long duration = System.currentTimeMillis() - start;
            log.info("[RabbitProcessor : RabbitMQ 비동기] 총 처리 시간: {}ms", duration);
        } catch (Exception e) {
            long duration = System.currentTimeMillis() - start;
            log.error("[RabbitProcessor] 오류 발생 ({}ms): {}", duration, e.getMessage());
        }
    }
}
