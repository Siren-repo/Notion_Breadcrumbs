package dev.jdbc.breadcrumbs.service;

import dev.jdbc.breadcrumbs.dto.PageInfo;
import dev.jdbc.breadcrumbs.model.Page;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PageService {
    private final JdbcTemplate jdbcTemplate;

    @Transactional
    public void save() {
        String insertQuery = "INSERT INTO pages (title, parent_id) VALUES (?, ?)";
        for (int i = 1; i <= 3; i++) {
            jdbcTemplate.update(insertQuery, "제목 " + i, null);
        }
        String insertQuery2 = "INSERT INTO pages (title, parent_id) VALUES (?, ?)";
        for (int i = 1; i <= 3; i++) {
            jdbcTemplate.update(insertQuery2, "제목 2 - " + i, 2);
        }
        String insertQuery3 = "INSERT INTO pages (title, parent_id) VALUES (?, ?)";
        for (int i = 1; i <= 3; i++) {
            jdbcTemplate.update(insertQuery3, "제목 2 - 2 - " + i, 5);
        }
    }

    @Transactional
    public PageInfo findPost(Long pageId) {
        List<String> breadcrumbs = new ArrayList<>();

        String sql = "SELECT *  FROM pages where id = ?";
        Page foundPage = jdbcTemplate.queryForObject(sql, (rs, rowNum) -> {
            Page page = new Page();
            page.setId(pageId);
            page.setTitle(rs.getString("title"));
            page.setParentId(rs.getLong("parent_id"));
            return page;
        }, pageId);
        // id, title, parentId 설정


        sql = "SELECT title FROM pages WHERE parent_id = ?";
        List<String> subPages = jdbcTemplate.queryForList(sql, String.class, pageId);


        Long parentId = foundPage.getParentId();

        while (parentId != 0) {
            sql = "SELECT title, parent_id FROM pages where id = ?";

            Page found = jdbcTemplate.queryForObject(sql, (rs, rowNum) -> {
                Page page = new Page();
                page.setTitle(rs.getString("title"));
                page.setParentId(rs.getLong("parent_id"));
                return page;
            }, parentId);
            breadcrumbs.add(0, found.getTitle());
            parentId = found.getParentId();
        }
        return PageInfo.builder()
                .pageId(foundPage.getId())
                .title(foundPage.getTitle())
                .subPages(Collections.unmodifiableList(subPages))
                .breadcrumbs(Collections.unmodifiableList(breadcrumbs))
                .build();
    }

}
