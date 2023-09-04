package dev.jdbc.breadcrumbs.controller;

import dev.jdbc.breadcrumbs.dto.PageInfo;
import dev.jdbc.breadcrumbs.service.PageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequiredArgsConstructor
public class PageController {
    private final PageService pageService;

    @GetMapping("/create")
    public void create(){
        pageService.save();
    }

    @GetMapping("/{postId}")
    public ResponseEntity<PageInfo> search(@PathVariable Long postId){
        PageInfo pageInfo = pageService.findPost(postId);
        return ResponseEntity.ok(pageInfo);

    }
}
