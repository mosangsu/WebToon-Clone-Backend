package com.lezhin.clone.backend.controller;

import lombok.RequiredArgsConstructor;

import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lezhin.clone.backend.dto.EpisodeDto;
import com.lezhin.clone.backend.dto.Response;
import com.lezhin.clone.backend.exception.PaymentException;
import com.lezhin.clone.backend.service.EpisodeService;


@RestController
@RequiredArgsConstructor
@RequestMapping("/episode")
public class EpisodeController {

    private final EpisodeService episodeService;

    /**
     * 에피소드 조회
     * @param com.lezhin.clone.backend.enums.EpisodeSchedule
     * @return com.lezhin.clone.backend.dto.Response
     */
    @GetMapping("/{episodeId}")
    public Response getEpisode(@PathVariable Long episodeId) {
        Response response = new Response();
        Object result = episodeService.getEpisode(episodeId);

        response.setCode("S");
        response.setMessage("만화 정보입니다.");
        response.setData(result);

        return response;
    }

    /**
     * 에피소드 구매
     * @param com.lezhin.clone.backend.enums.EpisodeSchedule
     * @return com.lezhin.clone.backend.dto.Response
     */
    @PostMapping("/{episodeId}/purchase")
    public Response purchaseEpisode(@RequestAttribute @NonNull Long memberId, @PathVariable Long episodeId) {
        Response response = new Response();
        Object result = null;

        try {
            result = episodeService.purchaseEpisode(memberId, episodeId);
            response.setCode("S");
            response.setMessage("에피소드를 구매했습니다.");
            response.setData(result);
        } catch (PaymentException e) {
            EpisodeDto.PaymentException.Res errRes = (EpisodeDto.PaymentException.Res) e.getValue();
            errRes.setMessage(e.getMessage());
            response.setCode("F");
            response.setMessage("에피소드를 구매하는 도중 문제가 발생하였습니다.");
            response.setData(errRes);
        }

        return response;
    }
}