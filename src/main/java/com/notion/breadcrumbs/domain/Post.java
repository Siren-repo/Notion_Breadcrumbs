package com.notion.breadcrumbs.domain;


import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class Post {
    private int pageId;
    private String title;
    private List subPages = new ArrayList<>();
    private List breadCrumbs = new ArrayList<>();

    public void addSubPages(int pageId){
        this.subPages.add(pageId);
    }
    public void addBreadCrumbs(String title){
        this.breadCrumbs.add(title);
    }
}
