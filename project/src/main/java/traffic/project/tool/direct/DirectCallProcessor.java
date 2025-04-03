package traffic.project.tool.direct;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import traffic.project.dto.ChatGPTRequest;
import traffic.project.dto.ChatGptResponse;
import traffic.project.tool.PromptProcessor;

@Component
@RequiredArgsConstructor
public class DirectCallProcessor implements PromptProcessor {

    private final RestTemplate restTemplate;
    private final Logger log = LoggerFactory.getLogger(DirectCallProcessor.class);

    @Value("${openai.model}")
    private String model;

    @Value("${openai.api.url}")
    private String apiUrl;

    @Override
    public String name() {
        return "DirectGPTCall : 단순 호출";
    }

    // GPT 직접 호출용
    @Override
    public void process(String prompt) {
        long start = System.currentTimeMillis();
        try {
            ChatGPTRequest request = new ChatGPTRequest(model, prompt);
            ChatGptResponse response = restTemplate.postForObject(apiUrl, request, ChatGptResponse.class);

            if (response != null && response.getChoices() != null && !response.getChoices().isEmpty()) {
                String content = response.getChoices().get(0).getMessage().getContent();
                long duration = System.currentTimeMillis() - start;
                log.info("[{}] 총 처리 시간: {}ms", name(), duration);
                log.info("GPT 응답: {}", content);
            }
        } catch (Exception e) {
            long duration = System.currentTimeMillis() - start;
            log.error("[{}] 처리 실패 ({}ms): {}", name(), duration, e.getMessage());
        }
    }

    // 벤치마크 content로부터 실행 (GPT 호출 없이 저장만 할 수도 있음)
    @Override
    public void processFromContent(String content) {
        long start = System.currentTimeMillis();
        try {
            // 단순 처리용 예시
            log.info("[{}] content 길이: {}, 처리 시간 측정 중...", name(), content.length());

            Thread.sleep(50); // 예시: 가짜 처리

            long duration = System.currentTimeMillis() - start;
            log.info("[{}] 처리 완료 - 처리 시간: {}ms", name(), duration);
        } catch (Exception e) {
            long duration = System.currentTimeMillis() - start;
            log.error("[{}] 처리 실패 ({}ms): {}", name(), duration, e.getMessage());
        }
    }
}
