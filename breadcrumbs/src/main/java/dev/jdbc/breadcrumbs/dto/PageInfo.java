package dev.jdbc.breadcrumbs.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Builder
@Getter
public class PageInfo {
    private Long pageId;
    private String title;
    private List<String> subPages;
    private List<String> breadcrumbs;
}
