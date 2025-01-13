package com.lezhin.clone.backend.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import javax.naming.AuthenticationException;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lezhin.clone.backend.dto.MemberDto;
import com.lezhin.clone.backend.entity.CoinDetail;
import com.lezhin.clone.backend.entity.Gift;
import com.lezhin.clone.backend.entity.Member;
import com.lezhin.clone.backend.enums.CoinType;
import com.lezhin.clone.backend.enums.GiftStatus;
import com.lezhin.clone.backend.enums.MemberType;
import com.lezhin.clone.backend.repository.CoinDetailRepository;
import com.lezhin.clone.backend.repository.CoinRepository;
import com.lezhin.clone.backend.repository.GiftRepository;
import com.lezhin.clone.backend.repository.MemberRepository;
import com.lezhin.clone.backend.util.CookieUtil;
import com.lezhin.clone.backend.util.JwtUtil;
import com.lezhin.clone.backend.util.RedisUtil;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Service
@Transactional
public class MemberService {

    private final MemberRepository memberRepository;

    private final GiftRepository giftRepository;

    private final CoinRepository coinRepository;
    private final CoinDetailRepository coinDetailRepository;

    private final ModelMapper modelMapper;

    @Autowired
    private final JwtUtil jwtUtil;

    @Autowired
    private final RedisUtil redisUtil;

    public MemberDto.Auth.Res signup(MemberDto.SignUp.Req req, HttpServletResponse res) {
        // Member newMember = null;
        // String token = null;
        // String refreshJwt = null;
        // try {
            
        //     // 2. 회원가입
        //     newMember = new Member();
        //     newMember.setNickname(req.getName());
        //     newMember.setProfileImageUrl(req.getProfileImageUrl());
        //     newMember.setType(MemberType.USER);
        //     newMember.setProfileImageUrl(req.getProfileImageUrl());
        //     newMember.setOauth2(true);
        //     newMember.setOauth2Email(email);
        //     newMember.setStatus(true);
        //     newMember.setOauth2Provider(req.getProvider());
        //     newMember.updateLastLoggedinAt();
        //     newMember.setFirstPurchase(true);
        //     memberRepository.save(newMember);

        //     // 3. 로그인
        //     token = jwtUtil.generateToken(newMember);
        //     refreshJwt = jwtUtil.generateRefreshToken(newMember);
        //     CookieUtil.addCookie(res, JwtUtil.REFRESH_TOKEN_NAME, refreshJwt,
        //         (int) JwtUtil.REFRESH_TOKEN_VALIDATION_MILLISECOND);
        //     redisUtil.setDataExpire("refreshToken", refreshJwt, newMember.getMemberId().toString(),
        //         JwtUtil.REFRESH_TOKEN_VALIDATION_MILLISECOND);

        //     // 4. 회원가입 선물
        //     List<Gift> gbList = new ArrayList<Gift>();
        //     gbList.add(new Gift(newMember, GiftStatus.UNCLAIMED, 12, 0, 0, "첫가입 특별 선물!", "지금 바로 보고싶은 웹툰을 보러가세요!", "https://s3.ap-northeast-2.amazonaws.com/cdn-webtoon-clone.mosworld.kr/banners/dummy_m_banner1.png", now.plusMonths(3)));
        //     gbList.add(new Gift(newMember, GiftStatus.UNCLAIMED, 0, 12, 0, "첫가입 특별 선물!", "지금 바로 보고싶은 웹툰을 보러가세요!", "https://s3.ap-northeast-2.amazonaws.com/cdn-webtoon-clone.mosworld.kr/banners/dummy_m_banner2.png", now.plusMonths(3)));
        //     gbList.add(new Gift(newMember, GiftStatus.UNCLAIMED, 0, 0, 1000, "첫가입 특별 선물!", "지금 바로 보고싶은 웹툰을 보러가세요!", "https://s3.ap-northeast-2.amazonaws.com/cdn-webtoon-clone.mosworld.kr/banners/dummy_m_banner3.png", now.plusMonths(3)));
        //     gbList.add(new Gift(newMember, GiftStatus.UNCLAIMED, 6, 6, 500, "첫가입 특별 선물!", "지금 바로 보고싶은 웹툰을 보러가세요!", "https://s3.ap-northeast-2.amazonaws.com/cdn-webtoon-clone.mosworld.kr/banners/dummy_m_banner4.png", now.plusMonths(3)));
        //     gbList.add(new Gift(newMember, GiftStatus.UNCLAIMED, 12, 12, 1000, "첫가입 특별 선물!", "지금 바로 보고싶은 웹툰을 보러가세요!", "https://s3.ap-northeast-2.amazonaws.com/cdn-webtoon-clone.mosworld.kr/banners/dummy_m_banner5.png", now.plusMonths(3)));

        //     giftRepository.saveAll(gbList);
        
        // } catch(AuthenticationException e) {
        //     log.error(e.getMessage());
        // }
        // return MemberDto.Auth.Res.builder()
        //         .user(MemberDto.Auth.Res.User.builder().id(newMember.getMemberId()).nickname(newMember.getNickname()).type(newMember.getType()).build())
        //         .token(token).build();
        return null;
    }
    
    public MemberDto.Auth.Res signupWithOauth2(String validationToken, MemberDto.OAuth2.Req req, HttpServletResponse res) {
        Member newMember = null;
        String token = null;
        String refreshJwt = null;
        try {
            String email = jwtUtil.get(validationToken, "email");
            String code = jwtUtil.get(validationToken, "code");
            String state = jwtUtil.get(validationToken, "state");
            LocalDateTime now = LocalDateTime.now();

            // 1. 유효성 인증 토큰 검증
            if (!(code + state).equals(redisUtil.getData("validationCode", email))) {
                throw new AuthenticationException();
            }
            
            // 2. 회원가입
            newMember = new Member();
            newMember.setNickname(req.getName());
            newMember.setProfileImageUrl(req.getProfileImageUrl());
            newMember.setType(MemberType.USER);
            newMember.setProfileImageUrl(req.getProfileImageUrl());
            newMember.setOauth2(true);
            newMember.setOauth2Email(email);
            newMember.setStatus(true);
            newMember.setOauth2Provider(req.getProvider());
            newMember.updateLastLoggedinAt();
            newMember.setFirstPurchase(true);
            memberRepository.save(newMember);

            // 3. 로그인
            token = jwtUtil.generateToken(newMember);
            refreshJwt = jwtUtil.generateRefreshToken(newMember);
            CookieUtil.addCookie(res, JwtUtil.REFRESH_TOKEN_NAME, refreshJwt,
                (int) JwtUtil.REFRESH_TOKEN_VALIDATION_MILLISECOND);
            redisUtil.setDataExpire("refreshToken", refreshJwt, newMember.getMemberId().toString(),
                JwtUtil.REFRESH_TOKEN_VALIDATION_MILLISECOND);

            // 4. 회원가입 선물
            List<Gift> gbList = new ArrayList<Gift>();
            gbList.add(new Gift(newMember, GiftStatus.UNCLAIMED, 12, 0, 0, "첫가입 특별 선물!", "지금 바로 보고싶은 웹툰을 보러가세요!", "https://s3.ap-northeast-2.amazonaws.com/cdn-webtoon-clone.mosworld.kr/banners/dummy_m_banner1.png", now.plusMonths(3)));
            gbList.add(new Gift(newMember, GiftStatus.UNCLAIMED, 0, 12, 0, "첫가입 특별 선물!", "지금 바로 보고싶은 웹툰을 보러가세요!", "https://s3.ap-northeast-2.amazonaws.com/cdn-webtoon-clone.mosworld.kr/banners/dummy_m_banner2.png", now.plusMonths(3)));
            gbList.add(new Gift(newMember, GiftStatus.UNCLAIMED, 0, 0, 1000, "첫가입 특별 선물!", "지금 바로 보고싶은 웹툰을 보러가세요!", "https://s3.ap-northeast-2.amazonaws.com/cdn-webtoon-clone.mosworld.kr/banners/dummy_m_banner3.png", now.plusMonths(3)));
            gbList.add(new Gift(newMember, GiftStatus.UNCLAIMED, 6, 6, 500, "첫가입 특별 선물!", "지금 바로 보고싶은 웹툰을 보러가세요!", "https://s3.ap-northeast-2.amazonaws.com/cdn-webtoon-clone.mosworld.kr/banners/dummy_m_banner4.png", now.plusMonths(3)));
            gbList.add(new Gift(newMember, GiftStatus.UNCLAIMED, 12, 12, 1000, "첫가입 특별 선물!", "지금 바로 보고싶은 웹툰을 보러가세요!", "https://s3.ap-northeast-2.amazonaws.com/cdn-webtoon-clone.mosworld.kr/banners/dummy_m_banner5.png", now.plusMonths(3)));

            giftRepository.saveAll(gbList);
        
        } catch(AuthenticationException e) {
            log.error(e.getMessage());
        }
        return MemberDto.Auth.Res.builder()
                .user(MemberDto.Auth.Res.User.builder().id(newMember.getMemberId()).nickname(newMember.getNickname()).type(newMember.getType()).build())
                .token(token).build();
    }

    @Transactional
    public Object deleteMember(Long memberId, Long reqMemberId, HttpServletResponse res) throws Exception {
        if (memberId != reqMemberId) throw new Exception("탈퇴하려는 유저와 현재 로그인 중인 유저가 다릅니다.");
        
        Member m = memberRepository.findById(memberId).orElse(new Member());

        coinDetailRepository.deleteAll(m.getCoins().stream().map(bc -> bc.getCoinDetails()).reduce(new ArrayList<CoinDetail>(), (prev, now) -> Stream.concat(prev.stream(), now.stream()).toList()));

        memberRepository.delete(m);

        return null;
    }

    public MemberDto.Account.Res getAccount(@NonNull Long memberId) throws Exception {
        MemberDto.Account.Res result = modelMapper.map(memberRepository.findById(memberId).orElse(new Member()), MemberDto.Account.Res.class);

        return result;
    }

    public MemberDto.Coin.Res getCoin(@NonNull Long memberId) throws Exception {
        Member m = memberRepository.findById(memberId).orElse(new Member());
        MemberDto.Coin.Res result = modelMapper.map(m, MemberDto.Coin.Res.class);
        result.setCoinAmount(memberRepository.getMemberCoinAmount(memberId));
        result.setBonusCoinAmount(memberRepository.getMemberBonusCoinAmount(memberId));
        result.setPointAmount(memberRepository.getMemberPointAmount(memberId));
        return result;
    }

    public Page<MemberDto.ExpiringCoins.Res> getExpiringCoins(Long memberId, CoinType type, Pageable pageable) {
        return coinRepository.getExpiringCoins(memberId, type, pageable);
    }

    public Page<MemberDto.ChargeHistory.Res> getChargeHistory(Long memberId, Pageable pageable) {
        return coinRepository.getChargeHistory(memberId, pageable);
    }

    public Page<MemberDto.UsageHistory.Res> getUsageHistory(Long memberId, CoinType type, Pageable pageable) {
        return coinRepository.getUsageHistory(memberId, type, pageable);
    }

    public Object changePassword(Long memberId) {
        if (memberId == null) throw new NullPointerException("회원정보가 없습니다.");
        Member member = memberRepository.findById(memberId).get();

        member.setPassword(null);

        return null;
    }
}