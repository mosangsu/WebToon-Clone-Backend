package com.lezhin.clone.backend.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.lezhin.clone.backend.dto.MemberDto;
import com.lezhin.clone.backend.dto.Response;
import com.lezhin.clone.backend.service.AuthService;
import com.lezhin.clone.backend.service.MemberService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class MemberController {
    
    private final AuthService authService;

    private final MemberService memberService;

    // @PostMapping("/signup")
    // public Response insertChainStore(@RequestPart(value = "file") MultipartFile multipartFile,
    //                            @RequestPart(value = "data") SignUpDto.Req requestDto,
    //                            HttpServletResponse res) {
    //     Response response = new Response();

    //     // memberService.insertChainStore(multipartFile, requestDto, res);
    //     response.setCode("S");
    //     response.setMessage("회원가입을 성공적으로 완료하였습니다.");

    //     return response;
    // }

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

    @PostMapping("/token/refresh")
    @ResponseBody
    public Response refreshAccessToken(HttpServletRequest request) {
        Response response = new Response();

        response.setCode("S");
        response.setMessage("토큰을 성공적으로 갱신하였습니다.");
        // response.setData(authService.refreshAccessToken(request));
        return response;
    }

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
            // response.setData(e.getMessage());
        }
        
        return response;
    }

}