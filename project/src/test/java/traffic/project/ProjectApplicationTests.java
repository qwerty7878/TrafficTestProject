package traffic.project;

import java.util.*;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import traffic.project.tool.PromptProcessor;

@SpringBootTest
@ActiveProfiles("test")
class ProjectApplicationTests {

    private static final Logger log = LoggerFactory.getLogger(ProjectApplicationTests.class);

    @Autowired
    private List<PromptProcessor> processors;

    private final String mockContent = "이것은 GPT에서 받아온 응답입니다. 성능 측정용 테스트입니다.";

    @Test
    public void 각_프로세서별_처리속도_테스트() {
        log.info("📊 프로세서 성능 테스트 시작");

        Map<String, Long> results = new LinkedHashMap<>();

        for (PromptProcessor processor : processors) {
            try {
                log.info("▶️ {} 시작", processor.name());
                long start = System.currentTimeMillis();
                processor.processFromContent(mockContent);
                long end = System.currentTimeMillis();
                results.put(processor.name(), end - start);
            } catch (Exception e) {
                log.error("❌ {} 처리 실패: {}", processor.name(), e.getMessage());
                results.put(processor.name(), -1L); // 실패한 프로세서는 -1 처리
            }
        }

        log.info("\n\n📦 최종 처리 결과:");
        results.forEach((name, time) -> {
            if (time >= 0) {
                log.info("✅ {} 처리 시간: {}ms", name, time);
            } else {
                log.warn("❌ {} 처리 실패", name);
            }
        });

        log.info("🏁 모든 프로세서 테스트 완료");
    }
}
