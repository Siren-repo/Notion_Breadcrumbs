package com.notion.breadcrumbs.controller;

import com.notion.breadcrumbs.domain.Post;
import com.notion.breadcrumbs.service.postService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class PostController {

    @Autowired
    private postService postService;


    @GetMapping("/{pageId}")
    @ResponseBody
    public Post findPost(@PathVariable int pageId){
        Post post = postService.find(pageId);
        return post;
    }
}
