package traffic.project.tool.Rabbit;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import traffic.project.tool.PromptProcessor;

@Component
@RequiredArgsConstructor
public class RabbitProcessor implements PromptProcessor {

    private final RabbitTemplate rabbitTemplate;
    private final Logger log = LoggerFactory.getLogger(RabbitProcessor.class);

    @Value("${rabbitmq.queue}")
    private String queue;

    @Override
    public String name() {
        return "RabbitProcessor : 비동기";
    }

    @Override
    public void process(String prompt) {
        throw new UnsupportedOperationException("RabbitProcessor는 process(prompt)를 지원하지 않습니다. processFromContent()를 사용하세요.");
    }

    @Override
    public void processFromContent(String content) {
        long start = System.currentTimeMillis();
        try {
            rabbitTemplate.convertAndSend(queue, content);
            long duration = System.currentTimeMillis() - start;
            log.info("[{}] 메시지 전송 완료 - 처리 시간: {}ms", name(), duration);
        } catch (Exception e) {
            long duration = System.currentTimeMillis() - start;
            log.error("[{}] 처리 실패 ({}ms): {}", name(), duration, e.getMessage());
        }
    }
}
