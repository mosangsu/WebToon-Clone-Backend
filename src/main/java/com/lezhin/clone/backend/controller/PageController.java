package com.lezhin.clone.backend.controller;

import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lezhin.clone.backend.dto.Response;
import com.lezhin.clone.backend.service.PageService;


@RestController
@RequiredArgsConstructor
@RequestMapping("/page")
public class PageController {

    private final PageService pageService;

    /**
     * 페이지 리스트 조회
     * @param String
     * @return com.lezhin.clone.backend.dto.Response
     */
    @GetMapping("")
    public Response getPages() {
        Response response = new Response();
        Object result = pageService.getPages();

        response.setCode("S");
        response.setMessage("이벤트 페이지 정보입니다.");
        response.setData(result);

        return response;
    }

    /**
     * 페이지 제목으로 조회
     * @param String
     * @return com.lezhin.clone.backend.dto.Response
     */
    @GetMapping("/{title}")
    public Response getPage(@PathVariable String title) {
        Response response = new Response();
        Object result = pageService.getPage(title);

        response.setCode("S");
        response.setMessage("만화 정보입니다.");
        response.setData(result);

        return response;
    }
}