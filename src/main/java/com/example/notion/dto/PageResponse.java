package com.example.notion.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class PageResponse {
    private Long pageId;
    private String title;
    private List<PageDto> subpages;
    private String breadCrumbs;
    @Builder
    public PageResponse(Long pageId, String title, List<PageDto> subpages, String breadCrumbs) {
        this.pageId = pageId;
        this.title = title;
        this.subpages = subpages;
        this.breadCrumbs = breadCrumbs;
    }
}
