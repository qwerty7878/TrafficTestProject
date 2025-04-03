package traffic.project.tool.hadoop;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class LocalHdfsStarter {

    private Process nameNodeProcess;
    private Process dataNodeProcess;

    @PostConstruct
    public void startHdfs() {
        try {
            log.info("🧩 HDFS Namenode, Datanode 실행 시도");

            nameNodeProcess = new ProcessBuilder("bash", "-c", "hdfs --daemon start namenode").start();
            dataNodeProcess = new ProcessBuilder("bash", "-c", "hdfs --daemon start datanode").start();

            log.info("✅ HDFS 프로세스 시작됨");
        } catch (IOException e) {
            log.error("🚨 HDFS 프로세스 시작 실패: {}", e.getMessage());
        }
    }

    @PreDestroy
    public void stopHdfs() {
        try {
            log.info("🧹 Spring 종료 → HDFS 프로세스 종료 시도");

            new ProcessBuilder("bash", "-c", "hdfs --daemon stop datanode").start();
            new ProcessBuilder("bash", "-c", "hdfs --daemon stop namenode").start();

            log.info("🛑 HDFS 종료 완료");
        } catch (IOException e) {
            log.error("❌ HDFS 종료 중 에러: {}", e.getMessage());
        }
    }
}
