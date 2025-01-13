package com.lezhin.clone.backend.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lezhin.clone.backend.dto.CoinDto;
import com.lezhin.clone.backend.dto.GiftDto;
import com.lezhin.clone.backend.dto.PaymentDto;
import com.lezhin.clone.backend.entity.Coin;
import com.lezhin.clone.backend.entity.CoinDetail;
import com.lezhin.clone.backend.entity.Gift;
import com.lezhin.clone.backend.entity.Member;
import com.lezhin.clone.backend.enums.CoinStatus;
import com.lezhin.clone.backend.enums.CoinType;
import com.lezhin.clone.backend.enums.GiftStatus;
import com.lezhin.clone.backend.enums.PackageType;
import com.lezhin.clone.backend.repository.CoinRepository;
import com.lezhin.clone.backend.repository.GiftRepository;
import com.lezhin.clone.backend.repository.MemberRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Service
@Transactional
public class GiftService {

    private final ModelMapper modelMapper;

    private final GiftRepository giftRepository;
    private final MemberRepository memberRepository;
    private final CoinRepository coinRepository;
    
    public Page<GiftDto.List.Res> getGifts(Long memberId, Pageable pageable) {
        Page<Gift> g = giftRepository.findByMemberMemberId(memberId, pageable);
        return new PageImpl<>(g.getContent().stream().map(p -> modelMapper.map(p, GiftDto.List.Res.class)).collect(Collectors.toList()), g.getPageable(), g.getTotalElements());
    }

    @Transactional
    public CoinDto.Amounts.Res receiveGift(Long memberId, Long giftId) throws Exception {
        Member m = memberRepository.findById(memberId).orElse(new Member());
        Gift g = giftRepository.getByMemberMemberIdAndGiftId(memberId, giftId).orElse(new Gift());
        LocalDateTime now = LocalDateTime.now();

        if(g.getGiftId() == null) throw new Exception("해당 선물을 가지고 있지 않습니다.");
        
        // 코인 추가
        if (g.getCoins() > 0) {
            Coin c = new Coin(m, CoinStatus.ACCUMULATE, CoinType.COIN, g.getCoins(), "코인 선물");
            List<CoinDetail> cdList = new ArrayList<CoinDetail>();
            CoinDetail cd = new CoinDetail(c, CoinStatus.ACCUMULATE, CoinType.COIN, g.getCoins());
            cdList.add(cd);
            c.setCoinDetails(cdList);
            coinRepository.save(c);
            cd.setCoinAccumId(cd.getCoinDetailId());
        }

        // 보너스 코인 추가
        if (g.getBonusCoins() > 0) {
            Coin bc = new Coin(m, CoinStatus.ACCUMULATE, CoinType.BONUSCOIN, g.getBonusCoins(), "보너스 코인 선물", now.plusYears(1));
            List<CoinDetail> bcdList = new ArrayList<CoinDetail>();
            CoinDetail bcd = new CoinDetail(bc, CoinStatus.ACCUMULATE, CoinType.BONUSCOIN, g.getBonusCoins(), now.plusYears(1));
            bcdList.add(bcd);
            bc.setCoinDetails(bcdList);
            coinRepository.save(bc);
            bcd.setCoinAccumId(bcd.getCoinDetailId());
        }

        // 포인트 추가
        if (g.getPoints() > 0) {
            Coin p = new Coin(m, CoinStatus.ACCUMULATE, CoinType.POINT, g.getPoints(), "포인트 선물", now.plusYears(1));
            List<CoinDetail> pdList = new ArrayList<CoinDetail>();
            CoinDetail pd = new CoinDetail(p, CoinStatus.ACCUMULATE, CoinType.POINT, g.getPoints(), now.plusYears(1));
            pdList.add(pd);
            p.setCoinDetails(pdList);
            coinRepository.save(p);
            pd.setCoinAccumId(pd.getCoinDetailId());
        }

        // 선물 상태를 받음으로 변경
        g.setStatus(GiftStatus.CLAIMED);

        // 남은 코인을 결과값으로 리턴
        CoinDto.Amounts.Res result = new CoinDto.Amounts.Res();
        result.setCoinAmount(memberRepository.getMemberCoinAmount(memberId));
        result.setBonusCoinAmount(memberRepository.getMemberBonusCoinAmount(memberId));
        result.setPointAmount(memberRepository.getMemberPointAmount(memberId));

        return result;
    }
}