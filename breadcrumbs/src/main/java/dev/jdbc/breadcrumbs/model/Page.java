package dev.jdbc.breadcrumbs.model;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class Page {
    private Long id;
    private String title;
    private Long parentId;
}
