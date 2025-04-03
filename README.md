# 🛣 TrafficProject

ChatGPT로부터 받은 응답을 다양한 메시징/저장 시스템(Redis, Kafka, RabbitMQ, Hadoop 등)을 통해 처리하고, 각 처리 방식의 성능을 테스트하는 Spring Boot 기반 프로젝트입니다.

## 🔧 기술 스택

- Java 21
- Spring Boot 3.4.4
- Redis, Kafka, RabbitMQ
- Hadoop HDFS
- Gradle

## 🧪 테스트 방식

### ✔ 각 Processor 성능 테스트

`PromptProcessor` 인터페이스를 구현한 각 Processor 클래스들은 다음과 같은 방식으로 GPT 응답을 처리합니다:

- `DirectCallProcessor`: 직접 응답 받아 출력
- `RedisCacheProcessor`: 응답을 Redis에 저장
- `KafkaProcessor`: Kafka에 전송
- `RabbitProcessor`: RabbitMQ에 전송
- `HadoopProcessor`: HDFS에 저장

> 모든 처리는 `List<PromptProcessor>`로 관리되어 테스트 코드에서 순차 실행됩니다.

### 단위테스트 결과 :
<img width="1395" alt="Image" src="https://github.com/user-attachments/assets/d57a3150-cc48-441a-8e0e-88530e7cbcef" />

### 통합테스트 결과 : 
<img width="1440" alt="Image" src="https://github.com/user-attachments/assets/68cba415-b148-440c-a4ee-012a379c99ee" />

## 🔍 각 Processor 설명 및 사용 목적

| 프로세서              | 설명 |
|-----------------------|------|
| 🧠 **DirectGPTCall**       | 단순 요청-응답 처리에 적합. 복잡한 연계 없이 결과만 빠르게 필요할 때 사용합니다. |
| 📦 **HadoopProcessor**     | 대용량 데이터를 장기 보관하거나 분석을 위해 HDFS에 저장할 때 활용됩니다. |
| 🚀 **KafkaProcessor**      | 다른 서비스와 비동기 통신이 필요할 때 사용합니다. 실시간 데이터 스트리밍이나 분산 처리가 유용한 상황에 적합합니다. |
| 🐇 **RabbitProcessor**     | 신뢰성 있는 큐 기반 비동기 처리가 필요한 경우 적합합니다. 순서 보장이나 메시지 누락 방지가 중요한 경우 사용합니다. |
| 🧊 **RedisCacheProcessor** | 자주 사용되는 데이터를 빠르게 조회하거나 임시로 캐싱할 때 활용됩니다. 반복 접근이 많은 데이터에 유리합니다. |