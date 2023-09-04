package com.notion.breadcrumbs.service;


import com.notion.breadcrumbs.domain.Post;
import com.notion.breadcrumbs.repository.JdbcRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@Service
public class postService {
    @Autowired
    private JdbcRepository jdbcRepository;
    Connection connection =null;
    PreparedStatement statement = null;
    ResultSet resultSet = null;
    public Post find(int pageId) {
        Post post = new Post();
        try {
            int prePageId= 0;
            connection = jdbcRepository.getConnection();
            statement = connection.prepareStatement("SELECT * FROM tb_post WHERE id = ?");
            statement.setInt(1,pageId);
            resultSet = statement.executeQuery();
            while (resultSet.next()) {
                post.setPageId(resultSet.getInt(1));
                post.setTitle(resultSet.getString(6));
                post.addBreadCrumbs(resultSet.getString(6));
                prePageId = resultSet.getInt(7);
            }
            /**
             * SubPages
             * */
            statement = connection.prepareStatement("SELECT id,title FROM tb_post WHERE pre_post = ?");
            statement.setInt(1,post.getPageId());
            resultSet = statement.executeQuery();
            while (resultSet.next()) {
                post.addSubPages(resultSet.getInt(1));
            }

            /**
             * BreadCrumbs
             * */
            while (prePageId != 0){
                statement = connection.prepareStatement("SELECT title,pre_post FROM tb_post WHERE id = ?");
                statement.setInt(1,prePageId);
                resultSet = statement.executeQuery();
                while (resultSet.next()) {
                    post.addBreadCrumbs(resultSet.getString(1));
                    prePageId = resultSet.getInt(2);
                }
            }

        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }finally {
            jdbcRepository.close(connection,statement,resultSet);
        }
        return post;
    }


}
