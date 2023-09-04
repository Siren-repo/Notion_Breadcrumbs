package com.example.notion.controller;

import com.example.notion.dto.PageResponse;
import com.example.notion.serivce.PageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/pages")
@RequiredArgsConstructor
public class PageController{
    private final PageService pageService;

    @GetMapping("/{pageId}")
    public ResponseEntity<PageResponse> getPage(@PathVariable Long pageId){
        return ResponseEntity.ok(pageService.getPage(pageId));
    }
}
