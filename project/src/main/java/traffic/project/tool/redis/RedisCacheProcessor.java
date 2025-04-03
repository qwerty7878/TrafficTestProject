package traffic.project.tool.redis;

import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import traffic.project.tool.PromptProcessor;

@Component
@RequiredArgsConstructor
public class RedisCacheProcessor implements PromptProcessor {

    private final RedisTemplate<String, String> redisTemplate;
    private final Logger log = LoggerFactory.getLogger(RedisCacheProcessor.class);

    @Override
    public String name() {
        return "RedisCacheProcessor : 분산캐싱";
    }

    @Override
    public void process(String prompt) {
        throw new UnsupportedOperationException("RedisCacheProcessor는 process(prompt)를 지원하지 않습니다. processFromContent()를 사용하세요.");
    }

    @Override
    public void processFromContent(String content) {
        long start = System.currentTimeMillis();
        try {
            String key = "gpt:" + UUID.randomUUID();
            redisTemplate.opsForValue().set(key, content);
            long duration = System.currentTimeMillis() - start;
            log.info("[{}] 총 처리 시간: {}ms", name(), duration);
        } catch (Exception e) {
            long duration = System.currentTimeMillis() - start;
            log.error("[{}] 처리 실패 ({}ms): {}", name(), duration, e.getMessage());
        }
    }
}
