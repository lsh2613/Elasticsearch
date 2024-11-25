package com.elasticsearch;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostDocumentRepository extends ElasticsearchRepository<PostDocument, String> {

//    @Query("{\"match\": {\"content\": {\"query\": \"?0\"}}}")
    @Query("{\"match\": {\"content\": \"?0\"}}")
    Page<PostDocument> findByContentContaining(String keyword, Pageable pageable);
}
