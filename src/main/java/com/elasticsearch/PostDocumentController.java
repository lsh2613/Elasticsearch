package com.elasticsearch;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
public class PostDocumentController {

    private final PostDocumentRepository postDocumentRepository;

    @PostMapping("/posts")
    public ResponseEntity postDocument(@RequestBody PostCreateReq postCreateReq) {
        PostDocument postDocument = new PostDocument(postCreateReq.getTitle(), postCreateReq.getContent());
        postDocumentRepository.save(postDocument);
        return ResponseEntity.ok(postDocument.getId());
    }

    @GetMapping("/posts/_search")
    public ResponseEntity search(@RequestParam String keyword,
                                 @PageableDefault Pageable pageable) {
        Page<PostDocument> postDocuments = postDocumentRepository.findByContentContaining(keyword, pageable);
        return ResponseEntity.ok(postDocuments);
    }

    @Data
    public static class PostCreateReq {
        private String title;
        private String content;
    }
}
