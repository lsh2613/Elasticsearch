### 01. 프로젝트 설명
- Spring + Elasticsearch의 Full Text Index(전문 검색 인덱스)를 적용한 개인 프로젝트
- 자세한 설명과 테스트는 [블로그](https://lsh2613.tistory.com/265)를 통해 확인해볼 수 있다

### 02. 기능
- Spring에서 Elasticsearch의 nori tokenizer를 적용한 인덱스 생성 
- Spring에서 nori tokenizer로 분리된 term을 통한 조회
- 성능 비교 (MySQL의 Like% vs 전문 검색 인덱스)

### 03. 사용 기술
- `Spring Boot 3.1.11`, `Spring Data JPA`, `Spring Data Elasticsearch`
- `Elasticsearch 8.7.1`, `kibana 8.7.1`, `Analysis-Nori`
- `Docker`, `Docker Compose`
- `MySQL`
