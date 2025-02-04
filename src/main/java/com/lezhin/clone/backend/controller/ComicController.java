package com.lezhin.clone.backend.controller;

import lombok.RequiredArgsConstructor;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.lezhin.clone.backend.dto.ComicDto;
import com.lezhin.clone.backend.dto.Response;
import com.lezhin.clone.backend.enums.EpisodeSchedule;
import com.lezhin.clone.backend.enums.RankingType;
import com.lezhin.clone.backend.service.ComicService;
import com.lezhin.clone.backend.service.EpisodeService;


@RestController
@RequiredArgsConstructor
@RequestMapping("/comic")
public class ComicController {

    private final ComicService comicService;

    private final EpisodeService episodeService;

    /**
     * 연재주기 기준 웹툰 리스트 조회
     * @param com.lezhin.clone.backend.enums.EpisodeSchedule
     * @return com.lezhin.clone.backend.dto.Response
     */
    @GetMapping("/scheduled")
    public Response getScheduled(@RequestParam(name = "day") EpisodeSchedule episodeSchedule) {
        Response response = new Response();
        Object result = comicService.getScheduled(episodeSchedule);

        response.setCode("S");
        response.setMessage("만화 정보입니다.");
        response.setData(result);

        return response;
    }

    /**
     * 성인 웹툰 조회
     * @param 
     * @return com.lezhin.clone.backend.dto.Response
     */
    @GetMapping("/adult")
    public Response getAdultComicList(@PageableDefault(size = 36) Pageable pageable) {
        
        Response response = new Response();
        Object result = comicService.getAdultComicList(pageable);

        response.setCode("S");
        response.setMessage("만화 정보입니다.");
        response.setData(result);

        return response;
    }

    /**
     * 최신 웹툰 조회
     * @param 
     * @return com.lezhin.clone.backend.dto.Response
     */
    @GetMapping("/new")
    public Response getNewComicList(@PageableDefault(size = 12) Pageable pageable) {
        
        Response response = new Response();
        Object result = comicService.getNewComicList(pageable);

        response.setCode("S");
        response.setMessage("만화 정보입니다.");
        response.setData(result);

        return response;
    }

    /**
     * 완결 웹툰 조회
     * @param 
     * @return com.lezhin.clone.backend.dto.Response
     */
    @GetMapping("/completed")
    public Response getCompletedComicList(@PageableDefault(size = 36) Pageable pageable) {
        
        Response response = new Response();
        Object result = comicService.getCompletedComicList(pageable);

        response.setCode("S");
        response.setMessage("만화 정보입니다.");
        response.setData(result);

        return response;
    }

    /**
     * 무료 웹툰 조회
     * @param 
     * @return com.lezhin.clone.backend.dto.Response
     */
    @GetMapping("/free")
    public Response getFreeComicList(@PageableDefault(size = 36) Pageable pageable) {
        
        Response response = new Response();
        Object result = comicService.getFreeComicList(pageable);

        response.setCode("S");
        response.setMessage("만화 정보입니다.");
        response.setData(result);

        return response;
    }

    /**
     * 선공개 웹툰 조회
     * @param 
     * @return com.lezhin.clone.backend.dto.Response
     */
    @GetMapping("/presub")
    public Response getPresubComicList(@PageableDefault(size = 36) Pageable pageable) {
        
        Response response = new Response();
        Object result = comicService.getPresubComicList(pageable);

        response.setCode("S");
        response.setMessage("만화 정보입니다.");
        response.setData(result);

        return response;
    }

    /**
     * 최근 연재 웹툰 조회
     * @param 
     * @return com.lezhin.clone.backend.dto.Response
     */
    @GetMapping("/updated")
    public Response getUpdatedComicList(@PageableDefault(size = 36) Pageable pageable) {
        
        Response response = new Response();
        Object result = comicService.getUpdatedComicList(pageable);

        response.setCode("S");
        response.setMessage("만화 정보입니다.");
        response.setData(result);

        return response;
    }

    /**
     * 이벤트 중인 웹툰 조회
     * @param 
     * @return com.lezhin.clone.backend.dto.Response
     */
    @GetMapping("/event")
    public Response getEventComicList(@PageableDefault(size = 36) Pageable pageable) {
        
        Response response = new Response();
        Object result = comicService.getEventComicList(pageable);

        response.setCode("S");
        response.setMessage("만화 정보입니다.");
        response.setData(result);

        return response;
    }

    /**
     * 수상작 웹툰 조회
     * @param 
     * @return com.lezhin.clone.backend.dto.Response
     */
    @GetMapping("/awarded")
    public Response getAwardedComicList(@PageableDefault(size = 36) Pageable pageable) {
        
        Response response = new Response();
        Object result = comicService.getAwardedComicList(pageable);

        response.setCode("S");
        response.setMessage("만화 정보입니다.");
        response.setData(result);

        return response;
    }

    /**
     * 소장중인 웹툰 조회
     * @param 
     * @return com.lezhin.clone.backend.dto.Response
     */
    @GetMapping("/owned")
    public Response getOwnedComicList(@RequestAttribute @NonNull Long memberId, @RequestParam(required = false) String q, @PageableDefault(size = 36) Pageable pageable) {
        

        Response response = new Response();
        Object result = comicService.getOwnedComicList(memberId, q, pageable);

        response.setCode("S");
        response.setMessage("만화 정보입니다.");
        response.setData(result);

        return response;
    }

    /**
     * 찜한 웹툰 조회
     * @param 
     * @return com.lezhin.clone.backend.dto.Response
     */
    @GetMapping("/subscribed")
    public Response getSubscribedComicList(@RequestAttribute @NonNull Long memberId, @RequestParam(required = false) String q, @PageableDefault(size = 36) Pageable pageable) {
        
        Response response = new Response();
        Object result = comicService.getSubscribedComicList(memberId, q, pageable);

        response.setCode("S");
        response.setMessage("만화 정보입니다.");
        response.setData(result);

        return response;
    }

    /**
     * 본 웹툰 조회
     * @param 
     * @return com.lezhin.clone.backend.dto.Response
     */
    @GetMapping("/read")
    public Response getReadComicList(@RequestParam Integer[] idList, @RequestParam(required = false) String q, @PageableDefault(size = 36) Pageable pageable) {
        
        Response response = new Response();
        Object result = comicService.getReadComicList(idList, q, pageable);

        response.setCode("S");
        response.setMessage("만화 정보입니다.");
        response.setData(result);

        return response;
    }

    /**
     * 웹툰 top3 조회
     * @param RankingType
     * @return com.lezhin.clone.backend.dto.Response
     */
    @GetMapping("/top3")
    public Response getTop3Comics(@RequestParam(defaultValue = "D") RankingType type) {
        Response response = new Response();
        Object result = comicService.getTop3Comics(type);

        response.setCode("S");
        response.setMessage("만화 정보입니다.");
        response.setData(result);

        return response;
    }

    /**
     * 웹툰 리스트 조회
     * @param String
     * @return com.lezhin.clone.backend.dto.Response
     */
    @GetMapping("")
    public Response getComics(@RequestParam(required = false) String genre, @RequestParam(required = false) String tag, @RequestParam(required = false) String q, @RequestParam(required = false, defaultValue = "all") String t, @RequestParam(required = false) Boolean isCompleted, @RequestParam(required = false) Boolean isFree, @RequestParam(required = false) String order, @PageableDefault(size = 36) Pageable pageable) {
        Response response = new Response();
        Object result = comicService.getComics(tag, genre, isCompleted, isFree, order, q, t, pageable);

        response.setCode("S");
        response.setMessage("만화 정보입니다.");
        response.setData(result);

        return response;
    }

    /**
     * 웹툰 랭킹 조회
     * @param String
     * @return com.lezhin.clone.backend.dto.Response
     */
    @GetMapping("/top100")
    public Response getTop100Comics(@RequestParam(required = false) String genre, @RequestParam(required = false) Integer year, @RequestParam(required = false) Boolean isEvent, @RequestParam(required = false) Boolean isNew) {
        Response response = new Response();
        Object result = comicService.getTop100Comics(genre, year, isEvent, isNew);

        response.setCode("S");
        response.setMessage("만화 정보입니다.");
        response.setData(result);

        return response;
    }
    /**
     * 웹툰 제목으로 조회
     * @param String
     * @return com.lezhin.clone.backend.dto.Response
     */
    @GetMapping("/{comicLinkName}")
    public Response getComic(@RequestAttribute(required = false) Long memberId, @PathVariable String comicLinkName) {
        Response response = new Response();
        Object result = comicService.getComic(memberId, comicLinkName);

        response.setCode("S");
        response.setMessage("만화 정보입니다.");
        response.setData(result);

        return response;
    }

    /**
     * 에피소드 조회
     * @param com.lezhin.clone.backend.enums.EpisodeSchedule
     * @return com.lezhin.clone.backend.dto.Response
     */
    @GetMapping("/{comicLinkName}/episode/{episodeLinkName}")
    public Response getEpisodeByLinkName(@RequestAttribute(required = false) Long memberId, @PathVariable String comicLinkName, @PathVariable String episodeLinkName) {
        Response response = new Response();
        Object result = episodeService.getEpisodeByLinkName(memberId, comicLinkName, episodeLinkName);

        response.setCode("S");
        response.setMessage("만화 정보입니다.");
        response.setData(result);

        return response;
    }

    /**
     * 웹툰 구독
     * @param 
     * @return com.lezhin.clone.backend.dto.Response
     */
    @PostMapping("/subscribe")
    public Response subscribeComic(@RequestAttribute(required = false) Long memberId, @RequestBody ComicDto.Subscribe.Req req) {
        
        Response response = new Response();
        Object result = null;

        try {
            result = comicService.subscribeComic(memberId, req.getComicId());
            response.setCode("S");
            response.setMessage("만화를 구독하였습니다.");
            response.setData(result);    
        } catch (NullPointerException e) {
            response.setCode("F");
            response.setMessage("유저 또는 만화의 정보가 불확실합니다.");
            response.setData(e.getMessage());
        }

        return response;
    }

    /**
     * 웹툰 구독 취소
     * @param 
     * @return com.lezhin.clone.backend.dto.Response
     */
    @DeleteMapping("/subscribe")
    public Response unsubscribeComic(@RequestAttribute(required = false) Long memberId, @RequestBody ComicDto.Unsubscribe.Req req) {
        
        Response response = new Response();
        Object result = null;

        try {
            result = comicService.unsubscribeComic(memberId, req.getComicIds());
            response.setCode("S");
            response.setMessage("만화를 구독취소 하였습니다.");
            response.setData(result);    
        } catch (NullPointerException e) {
            response.setCode("F");
            response.setMessage("유저 또는 만화의 정보가 불확실합니다.");
            response.setData(e.getMessage());
        }

        return response;
    }

    /**
     * 웹툰 좋아요/싫어요
     * @param 
     * @return com.lezhin.clone.backend.dto.Response
     */
    @PostMapping("/like")
    public Response likedComic(@RequestAttribute(required = false) Long memberId, @RequestBody ComicDto.Like.Req req) {
        
        Response response = new Response();
        Object result = null;

        try {
            result = comicService.likeComic(memberId, req.getComicId(), req.isLiked());
            response.setCode("S");
            response.setMessage("성공적으로 만화를 좋아요 표시 하였습니다.");
            response.setData(result);    
        } catch (NullPointerException e) {
            response.setCode("F");
            response.setMessage("유저 또는 만화의 정보가 불확실합니다.");
            response.setData(e.getMessage());
        }

        return response;
    }

    /**
     * 웹툰 좋아요/싫어요 취소
     * @param 
     * @return com.lezhin.clone.backend.dto.Response
     */
    @DeleteMapping("/like")
    public Response unlikedComic(@RequestAttribute(required = false) Long memberId, @RequestBody ComicDto.Like.Req req) {
        
        Response response = new Response();
        Object result = null;

        try {
            result = comicService.unlikeComic(memberId, req.getComicId());
            response.setCode("S");
            response.setMessage("만화를 좋아요 취소 하였습니다.");
            response.setData(result);    
        } catch (NullPointerException e) {
            response.setCode("F");
            response.setMessage("유저 또는 만화의 정보가 불확실합니다.");
            response.setData(e.getMessage());
        }

        return response;
    }

    /**
     * 웹툰 소장
     * @param 
     * @return com.lezhin.clone.backend.dto.Response
     */
    @PostMapping("/own")
    public Response ownComic(@RequestAttribute(required = false) Long memberId, @RequestBody ComicDto.Subscribe.Req req) {
        
        Response response = new Response();
        Object result = null;

        try {
            result = comicService.ownComic(memberId, req.getComicId());
            response.setCode("S");
            response.setMessage("성공적으로 만화를 구독하였습니다.");
            response.setData(result);    
        } catch (NullPointerException e) {
            response.setCode("F");
            response.setMessage("유저 또는 만화의 정보가 불확실합니다.");
            response.setData(e.getMessage());
        }

        return response;
    }

    /**
     * 웹툰 소장 취소
     * @param 
     * @return com.lezhin.clone.backend.dto.Response
     */
    @DeleteMapping("/own")
    public Response disownComic(@RequestAttribute(required = false) Long memberId, @RequestBody ComicDto.Disown.Req req) {
        
        Response response = new Response();
        Object result = null;

        try {
            result = comicService.disownComic(memberId, req.getComicIds());
            response.setCode("S");
            response.setMessage("만화를 구독취소 하였습니다.");
            response.setData(result);    
        } catch (NullPointerException e) {
            response.setCode("F");
            response.setMessage("유저 또는 만화의 정보가 불확실합니다.");
            response.setData(e.getMessage());
        }

        return response;
    }
}