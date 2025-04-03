package traffic.project.tool.hadoop;

import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import lombok.RequiredArgsConstructor;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import traffic.project.tool.PromptProcessor;

@Component
@RequiredArgsConstructor
public class HadoopProcessor implements PromptProcessor {

    private final Logger log = LoggerFactory.getLogger(HadoopProcessor.class);

    @Override
    public String name() {
        return "HadoopProcessor : HDFS 저장";
    }

    @Override
    public void process(String prompt) {
        // 필요 없다면 비워두거나 예외 처리 가능
        throw new UnsupportedOperationException("HadoopProcessor는 process(prompt)를 지원하지 않습니다. processFromContent()를 사용하세요.");
    }

    @Override
    public void processFromContent(String content) {
        long start = System.currentTimeMillis();
        try {
            String fileName = "gpt_output_" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss")) + ".txt";
            String hdfsPath = "/gpt/outputs/" + fileName;

            Configuration conf = new Configuration();
            conf.addResource(new ClassPathResource("hadoop/core-site.xml").getInputStream());
            conf.addResource(new ClassPathResource("hadoop/hdfs-site.xml").getInputStream());
            FileSystem fs = FileSystem.get(conf);

            try (OutputStream os = fs.create(new Path(hdfsPath))) {
                os.write(content.getBytes(StandardCharsets.UTF_8));
            }

            long duration = System.currentTimeMillis() - start;
            log.info("[{}] GPT 응답 저장 완료 - 처리 시간: {}ms", name(), duration);

        } catch (Exception e) {
            long duration = System.currentTimeMillis() - start;
            log.error("[{}] 처리 실패 ({}ms): {}", name(), duration, e.getMessage());
        }
    }
}
