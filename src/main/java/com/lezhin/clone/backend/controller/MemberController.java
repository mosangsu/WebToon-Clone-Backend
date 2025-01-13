package com.lezhin.clone.backend.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.lezhin.clone.backend.dto.MemberDto;
import com.lezhin.clone.backend.dto.Response;
import com.lezhin.clone.backend.enums.CoinType;
import com.lezhin.clone.backend.enums.RankingType;
import com.lezhin.clone.backend.service.AuthService;
import com.lezhin.clone.backend.service.MemberService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class MemberController {
    
    private final AuthService authService;

    private final MemberService memberService;

    /**
     * 회원가입
     * @param 
     * @return com.lezhin.clone.backend.dto.Response
     */

    @PostMapping("/signup")
    public Response signup(@RequestBody MemberDto.SignUp.Req req, HttpServletResponse res) {
        Response response = new Response();

        response.setData(memberService.signup(req, res));
        response.setCode("S");
        response.setMessage("회원가입을 성공적으로 완료하였습니다.");

        return response;
    }

    /**
     * OAuth 회원가입
     * @param 
     * @return com.lezhin.clone.backend.dto.Response
     */

    @PostMapping("/signup/oauth2")
    public Response signupWithOauth2(@CookieValue(name = "validationToken", required = false) String validationToken, @RequestBody MemberDto.OAuth2.Req req, HttpServletResponse res) {
        Response response = new Response();

        response.setData(memberService.signupWithOauth2(validationToken, req, res));
        response.setCode("S");
        response.setMessage("회원가입을 성공적으로 완료하였습니다.");

        return response;
    }

    /**
     * 회원탈퇴
     * @param 
     * @return com.lezhin.clone.backend.dto.Response
     */

    @DeleteMapping("/{reqMemberId}")
    public Response deleteMember(@RequestAttribute @NonNull Long memberId, @PathVariable Long reqMemberId, HttpServletResponse res) {
        Response response = new Response();

        try {
            response.setData(memberService.deleteMember(memberId, reqMemberId, res));
            response.setCode("S");
            response.setMessage("회원탈퇴를 성공적으로 완료하였습니다.");
        }
        catch(Exception e) {
            response.setCode("F");
            response.setMessage("회원탈퇴를 하는 도중 오류가 발생했습니다.");
            response.setData(e.getMessage());
        }

        return response;
    }

    /**
     * 로그인
     * @param 
     * @return com.lezhin.clone.backend.dto.Response
     */
    @PostMapping("/login")
    public Response login(@RequestBody MemberDto.Auth.Req user, HttpServletResponse res) {
        Response response = new Response();
        try {
            response.setData(authService.loginUser(user, res));
            response.setCode("S");
            response.setMessage("로그인을 성공적으로 완료했습니다.");
        }
        catch(Exception e){
            response.setCode("F");
            response.setMessage("로그인을 하는 도중 오류가 발생했습니다.");
            response.setData(e.getMessage());
        }
        
        return response;
    }

    /**
     * 토큰 갱신
     * @param 
     * @return com.lezhin.clone.backend.dto.Response
     */
    @PostMapping("/token/refresh")
    @ResponseBody
    public Response refreshAccessToken(HttpServletRequest request) {
        Response response = new Response();

        response.setCode("S");
        response.setMessage("토큰을 성공적으로 갱신하였습니다.");
        // response.setData(authService.refreshAccessToken(request));
        return response;
    }

    /**
     * 계정 정보
     * @param 
     * @return com.lezhin.clone.backend.dto.Response
     */
    @GetMapping("/account")
    public Response account(@RequestAttribute @NonNull Long memberId) {
        Response response = new Response();
        try {
            response.setData(memberService.getAccount(memberId));
            response.setCode("S");
            response.setMessage("내 정보입니다.");
        }
        catch(Exception e){
            response.setCode("F");
            response.setMessage("내 정보를 가져오는 도중 오류가 발생했습니다.");
            response.setData(e.getMessage());
        }
        
        return response;
    }

    /**
     * 계정 정보
     * @param 
     * @return com.lezhin.clone.backend.dto.Response
     */
    @PutMapping("/password")
    public Response changePassword(@RequestAttribute @NonNull Long memberId) {
        Response response = new Response();
        try {
            response.setData(memberService.changePassword(memberId));
            response.setCode("S");
            response.setMessage("패스워드를 변경했습니다.");
        }
        catch(Exception e){
            response.setCode("F");
            response.setMessage("패스워드를 변경하는 도중 오류가 발생했습니다.");
            response.setData(e.getMessage());
        }
        
        return response;
    }

    /**
     * 코인 조회
     * @param 
     * @return com.lezhin.clone.backend.dto.Response
     */
    @GetMapping("/coin")
    public Response getCoin(@RequestAttribute @NonNull Long memberId) {
        Response response = new Response();
        try {
            response.setData(memberService.getCoin(memberId));
            response.setCode("S");
            response.setMessage("내 코인정보입니다.");
        }
        catch(Exception e){
            response.setCode("F");
            response.setMessage("내 코인정보를 가져오는 도중 오류가 발생했습니다.");
            response.setData(e.getMessage());
        }
        
        return response;
    }

    /**
     * 코인 소멸 예정 내역 조회
     * @param 
     * @return com.lezhin.clone.backend.dto.Response
     */
    @GetMapping("/coin/expiring")
    public Response getExpiringCoins(@RequestAttribute @NonNull Long memberId, @RequestParam CoinType type, @PageableDefault(size = 5) Pageable pageable) {
        Response response = new Response();
        try {
            response.setData(memberService.getExpiringCoins(memberId, type, pageable));
            response.setCode("S");
            response.setMessage("내 코인 소멸 예정 내역입니다.");
        }
        catch(Exception e){
            response.setCode("F");
            response.setMessage("내 코인 소멸 예정 내역을 가져오는 도중 오류가 발생했습니다.");
            response.setData(e.getMessage());
        }
        
        return response;
    }

    /**
     * 코인 충전 내역 조회
     * @param 
     * @return com.lezhin.clone.backend.dto.Response
     */
    @GetMapping("/coin/charge")
    public Response getChargeHistory(@RequestAttribute @NonNull Long memberId, @PageableDefault(size = 5) Pageable pageable) {
        Response response = new Response();
        try {
            response.setData(memberService.getChargeHistory(memberId, pageable));
            response.setCode("S");
            response.setMessage("내 코인 충전 내역입니다.");
        }
        catch(Exception e){
            response.setCode("F");
            response.setMessage("내 코인 충전 내역을 가져오는 도중 오류가 발생했습니다.");
            response.setData(e.getMessage());
        }
        
        return response;
    }

    /**
     * 코인 사용 내역 조회
     * @param 
     * @return com.lezhin.clone.backend.dto.Response
     */
    @GetMapping("/coin/usage")
    public Response getUsageHistory(@RequestAttribute @NonNull Long memberId, @RequestParam CoinType type, @PageableDefault(size = 5) Pageable pageable) {
        Response response = new Response();
        try {
            response.setData(memberService.getUsageHistory(memberId, type, pageable));
            response.setCode("S");
            response.setMessage("내 코인 사용 내역입니다.");
        }
        catch(Exception e){
            response.setCode("F");
            response.setMessage("내 코인 사용 내역을 가져오는 도중 오류가 발생했습니다.");
            response.setData(e.getMessage());
        }
        
        return response;
    }
}