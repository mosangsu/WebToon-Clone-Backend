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

import jakarta.servlet.http.Cookie;
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
        redisUtil.setDataExpire("refreshToken", refreshJwt, member.getUsername(),
                JwtUtil.REFRESH_TOKEN_VALIDATION_MILLISECOND);

        member.updateLastLoggedinAt();
        
        return MemberDto.Auth.Res.builder()
                .user(MemberDto.Auth.Res.User.builder().id(member.getMemberId()).username(member.getUsername()).type(member.getType()).build())
                .token(token).build();
    }

    

    // public MemberDto.Auth.Res refreshAccessToken(HttpServletRequest request) {
    //     Member member = null;
    //     String partnerUri = null;
    //     ChainStoreLevel level = null;

    //     Cookie refreshToken = cookieUtil.getCookie(request, JwtUtil.REFRESH_TOKEN_NAME);
    //     if (refreshToken == null)
    //         throw new BadCredentialsException("리프레시 토큰이 존재하지 않습니다.");

    //     String refreshJwt = refreshToken.getValue();
    //     String username = redisUtil.getData(JwtUtil.REFRESH_TOKEN_NAME, refreshJwt);

    //     if (username.equals(jwtUtil.getUsername(refreshJwt))) {
    //         member = memberRepository.findByUsername(username);
    //         ChainStore chainStore = chainStoreRepository.findByDefaultYnAndMember(true, member);
    //         partnerUri = chainStore.getPartner().getUri();
    //         List<ChainStore> chainStoreList = chainStoreRepository.findAllByMember(member);
    //         level = chainStore.getLevel();
    //         boolean check = chainStoreList.stream().anyMatch(c -> c.getLevel().name().equals(ChainStoreLevel.FRANCHISOR.name()));
    //         if (check) {
    //             level = ChainStoreLevel.FRANCHISOR;
    //         }
    //     }

    //     return MemberDto.Auth.Res.builder()
    //             .user(MemberDto.Auth.Res.User.builder().id(member.getMemberId()).name(member.getNickname()).type(member.getType()).chainStoreLevel(level).build())
    //             .token(jwtUtil.generateToken(member,  level, partnerUri)).build();
    // }
    // public MemberDto.Auth.Res getTokenByChainStore(Long memberId, Long partnerId) throws NoSuchElementException {
    //     Member member = memberRepository.findById(memberId).get();
    //     ChainStore chainStore = chainStoreRepository.findByPartnerPartnerIdAndMemberMemberId(partnerId, memberId).orElseThrow();
    //     List<ChainStore> chainStoreList = chainStoreRepository.findAllByMember(member);
    //     ChainStoreLevel level = chainStore.getLevel();
    //     boolean check = chainStoreList.stream().anyMatch(c -> c.getLevel().name().equals(ChainStoreLevel.FRANCHISOR.name()));
    //     if (check) {
    //         level = ChainStoreLevel.FRANCHISOR;
    //     }
    //     String partnerUri = chainStore.getPartner().getUri();
        
    //     return MemberDto.Auth.Res.builder()
    //             .user(MemberDto.Auth.Res.User.builder().id(member.getMemberId()).name(member.getNickname()).type(member.getType()).chainStoreLevel(level).build())
    //             .token(jwtUtil.generateToken(member, level, partnerUri)).build();
    // }

    // // 인증번호 확인
    // public void checkCertificationNumber(String username, String name, String email, String code, HttpServletResponse res) {
    //     String certificationNumber = redisUtil.getData("certificationNumber", username);
    //     if(certificationNumber == null) {
    //         throw new IllegalArgumentException("CNN : 인증번호 유효기간이 경과하였습니다.");
    //     }

    //     if(redisUtil.getData("failCount", username) == null) {
    //         throw new IllegalArgumentException("FCN : 인증번호 유효기간이 경과하였습니다.");
    //     }
    //     int failCount = Integer.valueOf(redisUtil.getData("failCount", username));

    //     if(failCount > 5) {
    //         throw new IllegalArgumentException("6회 이상 오류로 인증이 제한됩니다. 인증번호 재발급 요청하여 진행해주세요.");
    //     }
    //     if(!code.equals(certificationNumber)) {
    //         failCount++;
    //         redisUtil.setDataExpire("failCount", username, Integer.toString((failCount)),
    //             1000L * 60 * 10); // 10분
    //         throw new IllegalArgumentException("인증번호가 일치하지 않습니다. : "+Integer.toString(failCount)+"회, 6회 이상 실패 시 인증번호를 재발급 해야합니다.");
    //     }

    //     Member member = memberRepository.findByUsernameAndNameAndEmail(username, name, email);
    //     if(member == null) {
    //         throw new IllegalArgumentException("해당하는 Member가 없습니다.");
    //     }
    //     generateRedisPasswordValidationCode(username, res);
    // }
}