package com.example.notion.serivce;

import com.example.notion.domain.PageEntity;
import com.example.notion.dto.PageDto;
import com.example.notion.dto.PageResponse;
import com.example.notion.repository.PageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PageService {
    private final PageRepository pageRepository;

    public PageResponse getPage(Long id){
        Optional<PageEntity> pageOptional = pageRepository.findById(id);
        PageEntity page = pageOptional.orElseThrow(() -> new RuntimeException("Page not found"));
        List<PageDto> subPages = pageRepository.findSubPagesById(page.getParentId());
        String breadCrumbs = pageRepository.findBreadCrumbsById(id);

        return PageResponse.builder()
                .pageId(page.getId())
                .title(page.getTitle())
                .subpages(subPages)
                .breadCrumbs(breadCrumbs)
                .build();
    }
}
