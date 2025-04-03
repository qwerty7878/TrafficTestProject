package traffic.project;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import traffic.project.tool.Rabbit.RabbitProcessor;
import traffic.project.tool.direct.DirectCallProcessor;
import traffic.project.tool.hadoop.HadoopProcessor;
import traffic.project.tool.kafka.KafkaProcessor;
import traffic.project.tool.redis.RedisCacheProcessor;

@SpringBootTest
public class AllProcessorsTest {

    private final Logger log = LoggerFactory.getLogger(AllProcessorsTest.class);

    @Autowired
    private DirectCallProcessor directCallProcessor;

    @Autowired
    private HadoopProcessor hadoopProcessor;

    @Autowired
    private KafkaProcessor kafkaProcessor;

    @Autowired
    private RabbitProcessor rabbitProcessor;

    @Autowired
    private RedisCacheProcessor redisCacheProcessor;

    @Test
    void directProcessorTest() {
        String mockContent = "🔥 Direct 단위 테스트용 데이터입니다.";
        directCallProcessor.processFromContent(mockContent);
        log.info("Direct processor 테스트 완료");
    }

    @Test
    void hadoopProcessorTest() {
        String mockContent = "🔥 HDFS 단위 테스트용 데이터입니다.";
        hadoopProcessor.processFromContent(mockContent);
        log.info("Hadoop processor 테스트 완료");
    }

    @Test
    void kafkaProcessorTest() {
        String mockContent = "🔥 Kafka 단위 테스트용 데이터입니다.";
        kafkaProcessor.processFromContent(mockContent);
        log.info("Kafka processor 테스트 완료");
    }

    @Test
    void rabbitProcessorTest() {
        String mockContent = "🔥 RabbitMQ 단위 테스트용 데이터입니다.";
        rabbitProcessor.processFromContent(mockContent);
        log.info("RabbitMQ processor 테스트 완료");
    }

    @Test
    void redisCacheProcessorTest() {
        String mockContent = "🔥 Redis 단위 테스트용 데이터입니다.";
        redisCacheProcessor.processFromContent(mockContent);
        log.info("Redis processor 테스트 완료");
    }
}
