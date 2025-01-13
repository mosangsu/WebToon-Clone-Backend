package com.lezhin.clone.backend.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lezhin.clone.backend.dto.MemberDto;
import com.lezhin.clone.backend.entity.Member;
import com.lezhin.clone.backend.enums.MemberType;
import com.lezhin.clone.backend.repository.MemberRepository;
import com.lezhin.clone.backend.util.CookieUtil;
import com.lezhin.clone.backend.util.JwtUtil;
import com.lezhin.clone.backend.util.RedisUtil;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
@Transactional
public class AuthService {

    private final MemberRepository memberRepository;

    private final PasswordEncoder passwordEncoder;


    private final JwtUtil jwtUtil;

    private final RedisUtil redisUtil;

    public MemberDto.Auth.Res loginUser(MemberDto.Auth.Req request, HttpServletResponse response) throws Exception {
        Member member = memberRepository.findByUsername(request.getUsername());
        if (member == null)
            throw new Exception("아이디 또는 비밀번호가 일치하지 않습니다.");

        if (!passwordEncoder.matches(request.getPassword(), member.getPassword()))
            throw new Exception("아이디 또는 비밀번호가 일치하지 않습니다.");

        if (member.getType() == MemberType.WITHDRAWAL)
            throw new Exception("탈퇴된 계정 입니다.");

        final String token = jwtUtil.generateToken(member);
        final String refreshJwt = jwtUtil.generateRefreshToken(member);
        CookieUtil.addCookie(response, JwtUtil.REFRESH_TOKEN_NAME, refreshJwt,
                (int) JwtUtil.REFRESH_TOKEN_VALIDATION_MILLISECOND);
        redisUtil.setDataExpire("refreshToken", refreshJwt, member.getMemberId().toString(),
                JwtUtil.REFRESH_TOKEN_VALIDATION_MILLISECOND);

        member.updateLastLoggedinAt();
        
        return MemberDto.Auth.Res.builder()
                .user(MemberDto.Auth.Res.User.builder().id(member.getMemberId()).nickname(member.getNickname()).type(member.getType()).build())
                .token(token).build();
    }

    public Object sendVerificationMail (String email) throws Exception {

        // Optional<Member> findMember = memberRepository.findByIsOauth2AndOauth2EmailOrUsername(true, email, email);
        
        // if (findMember.isPresent()) throw new Exception("해당 유저는 이미 가입한 유저입니다.");

        // if(redisUtil.getData("mailSendCheck", email) != null) {
        //     throw new IllegalArgumentException("인증번호 메일 재발송은 발송 10초 이후 가능합니다.");
        // }

        // StringBuffer certificationNumberSb = new StringBuffer();
        // Random random = new Random(System.currentTimeMillis());
        // for(int i=0;i<6;i++) {
        //     certificationNumberSb.append(random.nextInt(10));
        // }
        // String certificationNumber = certificationNumberSb.toString();
        
        // redisUtil.setDataExpire("mailSendCheck", email, "true", 1000L * 10); // 10초
        // redisUtil.setDataExpire("certificationNumber", email, certificationNumber, 1000L * 60 * 10); // 10분
        // redisUtil.setDataExpire("failCount", email, "0", 1000L * 60 * 10); // 10분
        
        // // res.setHeader("Code", certificationNumber);
        // // res.setHeader("Access-Control-Expose-Headers" , "Code");    // 해당 응답헤더를 javascript가 접근할 수 있도록 설정

        // MimeMessage message = emailSender.createMimeMessage();
        // message.setFrom("ahtkdtn@gmail.com");
        // message.setRecipient(Message.RecipientType.TO, new InternetAddress(email));
        // message.setSubject("웹툰 클론 사이트 이메일 인증");
        // StringBuffer sb = new StringBuffer();
        // sb.append("<!doctype html>");
        // sb.append("<html lang='ko'>");
        // sb.append("<head>");
        // sb.append("<meta charset='utf-8'>");
        // sb.append("<title>웹툰 클론 사이트 이메일 인증</title>");
        // sb.append("</head>");
        // sb.append("<body>");
        // sb.append("<div style='margin:30px auto;width:600px;border:10px solid #f7f7f7'>");
        // sb.append("<div style='border:1px solid #dedede'>");
        // sb.append("<h1 style='padding:30px 30px 0;background:#f7f7f7;color:#555;font-size:1.4em'>");
        // sb.append("인증번호 안내");
        // sb.append("</h1>");
        // sb.append("<p style='margin:20px 0 0;padding:30px 30px 30px;border-bottom:1px solid #eee;line-height:1.7em'>");
        // sb.append("이 메일은 서비스를 원활히 이용하기 위한 인증메일 입니다.<br>");
        // sb.append("아래에서 인증번호를 확인하신 후 입력란에 입력하시고 다음 단계를 진행하십시오.");
        // sb.append("</p>");
        // sb.append("<p style='margin:0;padding:30px 30px 30px;border-bottom:1px solid #eee;line-height:1.7em'>");
        // sb.append("<span style='display:inline-block;width:130px'>인증번호</span> <strong style='color:#ff3061'>");
        // sb.append(certificationNumber);
        // sb.append("</strong>");
        // sb.append("</p>");
        // sb.append("</div>");
        // sb.append("</div>");
        // sb.append("</body>");
        // sb.append("</html>");
        
        // message.setContent(sb.toString(), "text/html;charset=euc-kr");

        // emailSender.send(message);
        return null;
    }
}