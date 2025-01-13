package com.lezhin.clone.backend.controller;

import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lezhin.clone.backend.dto.Response;
import com.lezhin.clone.backend.service.GiftService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/gift")
public class GiftController {

    private final GiftService giftService;

    /**
     * 선물함 조회
     * @param 
     * @return com.lezhin.clone.backend.dto.Response
     */
    @GetMapping("")
    public Response getGift(@RequestAttribute @NonNull Long memberId, @PageableDefault(size = 10) Pageable pageable) {
        Response response = new Response();
        try {
            response.setData(giftService.getGifts(memberId, pageable));
            response.setCode("S");
            response.setMessage("선물함 정보입니다.");
        }
        catch(Exception e){
            response.setCode("F");
            response.setMessage("선물함 정보를 가져오는 도중 오류가 발생했습니다.");
            response.setData(e.getMessage());
        }
        
        return response;
    }

    /**
     * 선물 받기
     * @param 
     * @return com.lezhin.clone.backend.dto.Response
     */
    @PostMapping("/{giftId}")
    public Response receiveGift(@RequestAttribute @NonNull Long memberId, @PathVariable Long giftId) {
        Response response = new Response();
        try {
            response.setData(giftService.receiveGift(memberId, giftId));
            response.setCode("S");
            response.setMessage("선물을 성공적으로 수령했습니다.");
        }
        catch(Exception e){
            response.setCode("F");
            response.setMessage("선물을 수령하는 도중 오류가 발생했습니다.");
            response.setData(e.getMessage());
        }
        
        return response;
    }
}