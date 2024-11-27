### 01. 프로젝트 설명
- Spring + Elasticsearch의 Full Text Index(전문 검색 인덱스)를 적용한 개인 프로젝트
- Nori 분석기를 적용한 인덱스를 통해 검색 성능 비교
- 10,000개의 테스트 데이터를 위한 csv, ES bulk query, H2 bulk query 활용
- 자세한 설명과 테스트는 [블로그](https://lsh2613.tistory.com/265)를 통해 확인해볼 수 있다

### 02. 기능
- Nori 분석기가 적용된 인덱스에 다큐먼트 추가 
- Nori tokenizer로 분리된 term을 통한 다큐먼트 검색
- H2, ES에 10,000 건 데이터 삽입 성능 비교 
- H2, ES에 10,000 건 데이터 검색 성능 비교

### 03. 사용 기술
- `Spring Boot 3.1.11`, `Spring Data JPA`, `Spring Data Elasticsearch`
- `Elasticsearch 8.7.1`, `kibana 8.7.1`, `Analysis-Nori`
- `Docker`, `Docker Compose`
- `H2`

### 04. 이슈
- [Elasticsearch 학습](https://lsh2613.tistory.com/266)
- [H2, ES에 대한 삽입/검색 성능 비교](https://lsh2613.tistory.com/265#1.%20Elasticsearch%EB%9E%80%3F-1)

### 05. 시작하기
**1. Elasticsearch, kibana 실행**<br>
``` shell
# docker-compose.yml이 위치한 루트 디렉토리로 이동 후
docker-compose up --build -d
```

**2. 프로젝트 실행**<br>

**3. 다큐먼트 추가**
```
[POST] loacalhost:8080/posts?title=애국가&content=동해물과 백두산이
```

**4. 다큐먼트 검색**
```
[GET] loacalhost:8080/posts/_search?content=동해
```    

**5. 성능 비교 확인**<br>
```
PerformanceTest 코드 실행
```
<img src="https://github.com/user-attachments/assets/6dcd5eef-645c-4303-b4c7-0933346fd56b" width="450" />
<img src="https://github.com/user-attachments/assets/46d6a143-0ccc-459d-aac7-a673dd278edf" width="500" />

