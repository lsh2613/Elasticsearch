## 01. 프로젝트 설명
- Spring + Elasticsearch의 Full Text Index(전문 검색 인덱스)를 적용한 개인 프로젝트
- [Nori](https://esbook.kimjmin.net/06-text-analysis/6.7-stemming/6.7.2-nori) 분석기를 적용한 인덱스를 통해 검색 성능 비교
- 10,000개의 테스트 데이터를 위한 csv, ES bulk query, H2 bulk query 활용

## 02. 기능
- Nori 분석기가 적용된 인덱스에 다큐먼트 추가 
- Nori tokenizer로 분리된 term을 통한 다큐먼트 검색
- H2, ES에 10,000 건 데이터 삽입 성능 비교 
- H2, ES에 10,000 건 데이터 검색 성능 비교

## 03. 사용 기술
- `Spring Boot 3.1.11`, `Spring Data JPA`, `Spring Data Elasticsearch`
- `Elasticsearch 8.7.1`, `kibana 8.7.1`, `Analysis-Nori`
- `Docker`, `Docker Compose`
- `H2`

## 04. 관련 포스팅
- [Elasticsearch 학습](https://lsh2613.tistory.com/265)
- [H2, ES에 대한 삽입/검색 성능 비교](https://lsh2613.tistory.com/266#6.%20Elasticsearch%EC%9D%98%20%EC%A0%84%EB%AC%B8%20%EA%B2%80%EC%83%89%20%EC%9D%B8%EB%8D%B1%EC%8A%A4%20vs%20H2%EC%9D%98%20LIKE%20%25%20%EC%84%B1%EB%8A%A5%20%EB%B9%84%EA%B5%90-1)

## 05. 시작하기
**1. 프로젝트 불러오기**
``` bash
git clone https://github.com/lsh2613/Elasticsearch.git <원하는 경로>
cd <원하는 경로>
```

**2. docker-compose 실행**<br>
``` bash
docker-compose up --build -d
```

**3. 애플리케이션 실행**<br>
``` bash
./gradlew bootRun
```

**3. 다큐먼트 추가**
``` bash
curl -X POST http://localhost:8080/post-documents \
  -H "Content-Type: application/json" \
  -d '{
    "title": "엘라스틱서치 테스트",
    "content": "nori 분석기를 활용한 한글 형태소 분석 테스트"
}'
```

**4. 다큐먼트 검색**
```
curl -G "http://localhost:8080/post-documents" \
     --data-urlencode "keyword=한글"
```    

## 06. 성능 비교

### PerformanceTest.벌크_삽입_성능_테스트()
> 10,000건의 데이터를 삽입하는데 걸린 시간
> 
> <img src="https://github.com/user-attachments/assets/6dcd5eef-645c-4303-b4c7-0933346fd56b" width="450" />
> 
> | 항목     | ns          | ms        | s          | 성능 차이                |
> |----------|-------------|------------|-------------|---------------------------|
> | H2 삽입  | 74,866,834  | 74.87 ms   | 0.07487 s   | 기준                      |
> | ES 삽입  | 641,827,792 | 641.83 ms  | 0.64183 s   | 🔽 약 **8.57배 더 느림**  |
>
> ⮕ ES는 역인덱싱이 필요하기 때문에 H2에 비해 삽입 속도가 많이 느리다.

---

### PerformanceTest.검색_성능_테스트()
> 10,000건의 데이터를 검색하는데 걸린 시간
> 
> <img src="https://github.com/user-attachments/assets/46d6a143-0ccc-459d-aac7-a673dd278edf" width="500" />
>
> | 항목     | ns          | ms        | s          | 성능 차이               |
> |----------|-------------|------------|-------------|--------------------------|
> | H2 검색  | 123,008,167 | 123.01 ms  | 0.12301 s   | 기준                     |
> | ES 검색  | 80,488,542  | 80.49 ms   | 0.08049 s   | 🔼 약 **1.53배 더 빠름**  |
>
> ⮕ ES가 역인덱스를 활용하여 검색하기 때문에 H2에 비해 **빠른 검색 성능**을 보인다.

---

### 결론
- 검색이 잦고 성능이 중요한 경우, **Elasticsearch를 함께 사용하는 하이브리드 구조**가 효율적일 수 있음