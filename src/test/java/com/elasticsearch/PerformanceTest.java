package com.elasticsearch;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.data.elasticsearch.core.query.IndexQuery;
import org.springframework.data.elasticsearch.core.query.IndexQueryBuilder;
import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.util.StopWatch;

import java.io.*;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import static java.lang.Thread.sleep;
import static org.assertj.core.api.Assertions.*;

@SpringBootTest
class PerformanceTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private ElasticsearchOperations elasticsearchOperations;
    @Autowired
    private PostEntityRepository postEntityRepository;
    @Autowired
    private PostDocumentRepository postDocumentRepository;

    @AfterEach
    void cleanUp() throws InterruptedException {
        jdbcTemplate.execute("DELETE FROM post_entity");
        elasticsearchOperations.delete(Query.findAll(), PostDocument.class, IndexCoordinates.of("post_document"));
        elasticsearchOperations.indexOps(IndexCoordinates.of("post_document")).refresh(); // 동기화
        sleep(3000);
    }

    @Test
    void 벌크_삽입_성능_테스트() {
        StopWatch bulkWatch = new StopWatch("Bulk Watch");

        List<String[]> rows = getRowsFromCSV();

        bulkWatch.start("H2 - bulk insert");
        batchInsertToH2(rows);
        bulkWatch.stop();

        bulkWatch.start("ES - bulk insert");
        batchInsertToES(rows);
        bulkWatch.stop();

        System.out.println(bulkWatch.prettyPrint());
    }

    @Test
    void 검생_성능_테스트() throws InterruptedException {
        List<String[]> rows = getRowsFromCSV();
        batchInsertToH2(rows);
        batchInsertToES(rows);
        sleep(3000);

        String keyword = "수정";
        StopWatch searchWatch = new StopWatch("Search Watch");

        searchWatch.start("H2 - search keyword");
        List<PostEntity> postEntities = postEntityRepository.findByContentContaining(keyword);
        searchWatch.stop();

        searchWatch.start("ES - search keyword");
        List<PostDocument> postDocuments = postDocumentRepository.findByContentContaining(keyword);
        searchWatch.stop();

        System.out.println(searchWatch.prettyPrint());

        assertThat(postEntities.size()).isEqualTo(postDocuments.size());
    }

    private List<String[]> getRowsFromCSV() {
        String filePath = "data/test_data.csv";

        List<String[]> rows = new ArrayList<>();
        try (InputStream resourceAsStream = getClass().getClassLoader().getResourceAsStream(filePath);
             BufferedReader br = new BufferedReader(new InputStreamReader(resourceAsStream, Charset.forName("EUC-KR")))
        ) {
            // 헤더 건너 뛰기
            br.readLine();

            String line;
            while ((line = br.readLine()) != null) {
                rows.add(line.split(","));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // 약 10,000 건으로 만들기
        List<String[]> repeatedRows = new ArrayList<>();
        for (int i = 0; i < 33; i++) {
            repeatedRows.addAll(rows);
        }

        return repeatedRows;
    }

    private void batchInsertToH2(List<String[]> rows) {
        String sql = "INSERT INTO post_entity (title, content) VALUES (?, ?)";

        List<Object[]> batchArgs = new ArrayList<>();
        for (String[] row : rows) {
            batchArgs.add(new Object[]{row[0], row[1]});
        }

        jdbcTemplate.batchUpdate(sql, batchArgs);
    }

    private void batchInsertToES(List<String[]> rows) {
        List<IndexQuery> indexQueryList = new ArrayList<>();
        for (String[] row : rows) {
            IndexQuery indexQuery = new IndexQueryBuilder()
                    .withObject(new PostDocument(row[0], row[1]))
                    .build();
            indexQueryList.add(indexQuery);
        }
        elasticsearchOperations.bulkIndex(indexQueryList, IndexCoordinates.of("post_document"));
    }

}
