package com.example.notion.repository;

import com.example.notion.domain.PageEntity;
import com.example.notion.dto.PageDto;
import com.example.notion.dto.PageResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class PageRepository {
    private final NamedParameterJdbcTemplate jdbcTemplate;
    private final String TABLE = "page";

    public Optional<PageEntity> findById(Long id){
        String sql = String.format("SELECT * FROM %s WHERE id = :id", TABLE);
        MapSqlParameterSource param = new MapSqlParameterSource().addValue("id", id);

        RowMapper<PageEntity> rowMapper = (ResultSet resultSet, int rowNum) -> PageEntity.builder()
                .id(resultSet.getLong("id"))
                .title(resultSet.getString("title"))
                .contents(resultSet.getString("contents"))
                .parentId(resultSet.getLong("parent_id"))
                .build();

        PageEntity page = jdbcTemplate.queryForObject(sql, param, rowMapper);
        return Optional.ofNullable(page);
    }
    public List<PageDto> findSubPagesById(Long parentId){
        String sql = String.format("SELECT * FROM %s WHERE parent_id = :parentId", TABLE);
        MapSqlParameterSource param = new MapSqlParameterSource().addValue("parentId", parentId);

        RowMapper<PageDto> rowMapper = (ResultSet resultSet, int rowNum) -> PageDto.builder()
                .id(resultSet.getLong("id"))
                .title(resultSet.getString("title"))
                .contents(resultSet.getString("contents"))
                .parentId(resultSet.getLong("parent_id"))
                .build();

        return jdbcTemplate.query(sql, param, rowMapper);
    }
    public String findBreadCrumbsById(Long id){
        String sql = String.format("WITH RECURSIVE breadcrumbs AS (\n" +
                "    SELECT id, parent_id, title AS breadcrumbs\n" +
                "    FROM %s\n" +
                "    WHERE id = :id\n" +
                "\n" +
                "    UNION ALL\n" +
                "\n" +
                "    SELECT p.id, p.parent_id, CONCAT(p.title, ' / ', b.breadcrumbs) AS breadcrumbs\n" +
                "    FROM %s AS p\n" +
                "             JOIN breadcrumbs AS b ON p.id = b.parent_id\n" +
                ")\n" +
                "SELECT breadcrumbs\n" +
                "FROM breadcrumbs\n" +
                "ORDER BY id", TABLE, TABLE);
        MapSqlParameterSource param = new MapSqlParameterSource().addValue("id", id);

        RowMapper<String> rowMapper = (ResultSet resultSet, int rowNum) -> resultSet.getString("breadcrumbs");
        return jdbcTemplate.query(sql, param, rowMapper).get(0);
    }
}
