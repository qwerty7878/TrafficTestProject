package traffic.project.tool.kafka;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import traffic.project.tool.PromptProcessor;

@Component
@RequiredArgsConstructor
public class KafkaProcessor implements PromptProcessor {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final Logger log = LoggerFactory.getLogger(KafkaProcessor.class);

    @Value("${kafka.topic}")
    private String topic;

    @Override
    public String name() {
        return "KafkaProcessor : Kafka 전송";
    }

    @Override
    public void process(String prompt) {
        throw new UnsupportedOperationException("KafkaProcessor는 process(prompt)를 지원하지 않습니다. processFromContent()를 사용하세요.");
    }

    @Override
    public void processFromContent(String content) {
        long start = System.currentTimeMillis();
        try {
            kafkaTemplate.send(topic, content);
            long duration = System.currentTimeMillis() - start;
            log.info("[{}] 메시지 전송 시간: {}ms", name(), duration);
        } catch (Exception e) {
            long duration = System.currentTimeMillis() - start;
            log.error("[{}] 처리 실패 ({}ms): {}", name(), duration, e.getMessage());
        }
    }
}
