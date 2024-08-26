package com.lezhin.clone.backend.controller;

import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.lezhin.clone.backend.dto.Response;
import com.lezhin.clone.backend.enums.EpisodeSchedule;
import com.lezhin.clone.backend.service.ComicService;


@RestController
@RequiredArgsConstructor
@RequestMapping("/comic")
public class ComicController {

    private final ComicService comicService;

    @GetMapping("/scheduled")
    public Response getScheduled(@RequestParam(name = "day") EpisodeSchedule episodeSchedule) {
        Response response = new Response();
        Object result = comicService.getScheduled(episodeSchedule);

        response.setCode("S");
        response.setMessage("만화 정보입니다.");
        response.setData(result);

        return response;
    }

    @GetMapping("/{title}")
    public Response getComic(@PathVariable String title) {
        Response response = new Response();
        Object result = comicService.getComic(title);

        response.setCode("S");
        response.setMessage("만화 정보입니다.");
        response.setData(result);

        return response;
    }
}