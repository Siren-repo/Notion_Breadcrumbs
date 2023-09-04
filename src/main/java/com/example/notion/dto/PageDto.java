package com.example.notion.dto;

import lombok.Builder;
import lombok.Getter;
@Getter
public class PageDto {
    private Long id;
    private Long parentId;
    private String title;
    private String contents;

    @Builder
    public PageDto(Long id, Long parentId, String title, String contents) {
        this.id = id;
        this.parentId = parentId;
        this.title = title;
        this.contents = contents;
    }
}
