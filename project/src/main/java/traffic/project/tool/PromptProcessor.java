package traffic.project.tool;

public interface PromptProcessor {
    String name();
    void process(String prompt); // 기존: GPT 호출 포함
    void processFromContent(String content); // 벤치마크 시 공통 content로 실행
}
