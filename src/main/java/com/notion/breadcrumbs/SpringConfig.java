package com.notion.breadcrumbs;

import com.notion.breadcrumbs.repository.JdbcRepository;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
public class SpringConfig {
    private final DataSource dataSource;

    public SpringConfig(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Bean
    public JdbcRepository test() {
        return new JdbcRepository(dataSource);
    }
}
