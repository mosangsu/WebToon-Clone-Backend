// package com.lezhin.clone.backend.controller;

// import lombok.RequiredArgsConstructor;

// import org.springframework.web.bind.annotation.GetMapping;
// import org.springframework.web.bind.annotation.RequestMapping;
// import org.springframework.web.bind.annotation.RestController;
// import org.springframework.web.server.ResponseStatusException;

// import com.lezhin.clone.backend.dto.AuthDto;
// import com.lezhin.clone.backend.dto.ComicDto;
// import com.lezhin.clone.backend.dto.Response;
// import com.lezhin.clone.backend.service.AuthService;
// import com.lezhin.clone.backend.service.GmailService;

// import jakarta.mail.MessagingException;

// import org.springframework.web.bind.annotation.PostMapping;
// import org.springframework.web.bind.annotation.RequestBody;



// @RestController
// @RequiredArgsConstructor
// @RequestMapping("/auth")
// public class AuthController {

//     private final GmailService gmailService;

//     @PostMapping("/email-verification")
//     public Response postMethodName(@RequestBody AuthDto.EmailVerification.Req req) {
        
//         Response response = new Response();
//         Object result = null;

//         try {
//             result = gmailService.sendEmail("ahtkdtn@gmail.com", req.getEmail());
//             response.setCode("S");
//             response.setMessage("인증 메일을 전송하였습니다.");
//             response.setData(result);    
//         } catch (Exception e) {
//             response.setCode("F");
//             response.setMessage("인증 메일을 전송하는 도중 문제가 발생하였습니다.");
//             response.setData(e.getMessage());
//         }

//         return response;
//     }
    
// }