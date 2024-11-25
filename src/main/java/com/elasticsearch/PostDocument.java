package com.elasticsearch;

import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Mapping;
import org.springframework.data.elasticsearch.annotations.Setting;
import org.springframework.data.elasticsearch.annotations.WriteTypeHint;

@Getter
@Document(indexName = "post", writeTypeHint = WriteTypeHint.FALSE) // '_class' 필드 제거
@Setting(settingPath = "elastic/post-document-setting.json") // post 인덱스에 대한 분석기, 토크나이저 설정
@Mapping(mappingPath = "elastic/post-document-mapping.json") // post가 저장&검색될 때 사용될 타입과 분석기 지정
public class PostDocument {

    @Id
    private String id;

    private String title;

    private String content;

    public PostDocument(String title, String content) {
        this.title = title;
        this.content = content;
    }
}
