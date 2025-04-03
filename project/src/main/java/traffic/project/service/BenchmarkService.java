package traffic.project.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.client.RestTemplate;
import traffic.project.dto.ChatGPTRequest;
import traffic.project.dto.ChatGptResponse;
import traffic.project.tool.PromptProcessor;

public class BenchmarkService {

    private final List<PromptProcessor> processors;
    private final RestTemplate restTemplate;
    private final String model;
    private final String apiUrl;

    public BenchmarkService(List<PromptProcessor> processors,
                            RestTemplate restTemplate,
                            @Value("${openai.model}") String model,
                            @Value("${openai.api.url}") String apiUrl) {
        this.processors = processors;
        this.restTemplate = restTemplate;
        this.model = model;
        this.apiUrl = apiUrl;
    }

    public void runBenchmark(String prompt) {
        System.out.println("🚀 벤치마크 시작");

        // 1️⃣ GPT 한 번만 호출
        ChatGPTRequest request = new ChatGPTRequest(model, prompt);
        ChatGptResponse response = restTemplate.postForObject(apiUrl, request, ChatGptResponse.class);
        String content = response.getChoices().get(0).getMessage().getContent();

        // 2️⃣ 저장 방식별 처리 시간 측정
        for (PromptProcessor processor : processors) {
            long start = System.currentTimeMillis();
            processor.process(content); // 저장만 수행
            long duration = System.currentTimeMillis() - start;

            System.out.printf("[%s] 처리 시간: %dms%n", processor.name(), duration);
        }

        System.out.println("✅ 벤치마크 종료");
    }
}
