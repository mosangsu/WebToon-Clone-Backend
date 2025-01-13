package com.lezhin.clone.backend.service;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.stream.Collectors;
import java.util.List;

import com.lezhin.clone.backend.dto.PageDto;
import com.lezhin.clone.backend.entity.Page;
import com.lezhin.clone.backend.repository.PageRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
@Transactional
public class PageService {

    private final PageRepository pageRepository;

    private final ModelMapper modelMapper;

    public Object getPages() {
        List<Page> page = pageRepository.findAll();
        return page.stream().map(p -> modelMapper.map(p, PageDto.List.Res.class)).collect(Collectors.toList());
    }

    public Object getPage(String linkName) {
        Page page = pageRepository.findByLinkName(linkName).orElse(new Page());
        return modelMapper.map(page, PageDto.Detail.Res.class);
    }
}