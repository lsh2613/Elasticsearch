package com.elasticsearch;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostEntityRepository extends JpaRepository<PostEntity, Long> {

//    SELECT * FROM post WHERE content LIKE %?%;
    List<PostEntity> findByContentContaining(String keyword);
}
