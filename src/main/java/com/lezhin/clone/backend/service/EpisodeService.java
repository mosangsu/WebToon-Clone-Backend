package com.lezhin.clone.backend.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lezhin.clone.backend.dto.CoinDto;
import com.lezhin.clone.backend.dto.ComicDto;
import com.lezhin.clone.backend.dto.EpisodeDto;
import com.lezhin.clone.backend.dto.MemberDto;
import com.lezhin.clone.backend.dto.PaymentDto;
import com.lezhin.clone.backend.entity.Coin;
import com.lezhin.clone.backend.entity.CoinDetail;
import com.lezhin.clone.backend.entity.Comic;
import com.lezhin.clone.backend.entity.Episode;
import com.lezhin.clone.backend.entity.Member;
import com.lezhin.clone.backend.entity.MemberEpisode;
import com.lezhin.clone.backend.entity.SubscribedComic;
import com.lezhin.clone.backend.enums.CoinStatus;
import com.lezhin.clone.backend.enums.CoinType;
import com.lezhin.clone.backend.exception.PaymentException;
import com.lezhin.clone.backend.repository.CoinRepository;
import com.lezhin.clone.backend.repository.EpisodeRepository;
import com.lezhin.clone.backend.repository.MemberEpisodeRepository;
import com.lezhin.clone.backend.repository.MemberRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
@Transactional
public class EpisodeService {

    private final CoinRepository coinRepository;
    private final MemberRepository memberRepository;
    private final EpisodeRepository episodeRepository;
    private final MemberEpisodeRepository memberEpisodeRepository;

    private final ModelMapper modelMapper;

    public EpisodeDto.Detail.Res getEpisodeByLinkName(Long memberId, String comicLinkName, String episodeLinkName) {
        Episode e = episodeRepository.findByComicLinkNameAndLinkName(comicLinkName, episodeLinkName).orElse(new Episode());
        EpisodeDto.Detail.Res result = modelMapper.map(e, EpisodeDto.Detail.Res.class);
        Episode prev = episodeRepository.findTop1ByComicLinkNameAndOrderLessThanOrderByOrderDesc(comicLinkName, result.getOrder()).orElse(new Episode());
        Episode next = episodeRepository.findTop1ByComicLinkNameAndOrderGreaterThanOrderByOrder(comicLinkName, result.getOrder()).orElse(new Episode());

        for (SubscribedComic sc : e.getComic().getSubscribedComics()) {
            if(memberId != null && memberId.equals(sc.getMember().getMemberId())){
                result.getComic().setSubscribed(true);
                break;
            }
        }
        
        result.setPrevLinkName(prev.getLinkName());
        result.setPrev(prev.getEpisodeId() != null);
        result.setNextLinkName(next.getLinkName());
        result.setNext(next.getEpisodeId() != null);

        return result;
    }

    public EpisodeDto.Detail2.Res getEpisode(Long episodeId) {
        Episode e = episodeRepository.findById(episodeId).orElse(new Episode());
        return modelMapper.map(e, EpisodeDto.Detail2.Res.class);
    }

    public CoinDto.Amounts.Res purchaseEpisode(Long memberId, Long episodeId) throws PaymentException {
        if (memberId == null) throw new NullPointerException("회원정보가 없습니다.");
        Member m = memberRepository.getReferenceById(memberId);
        Integer amount = memberRepository.getMemberCoinAmount(memberId);    // 유저가 가진 코인 수량 조회
        Episode e = episodeRepository.findById(episodeId).orElse(new Episode());

        // 유저가 가진 코인의 수량보다 에피소드의 가격이 비쌀 경우 예외 발생
        if (amount < e.getPrice()) throw new PaymentException("보유한 코인의 수량이 부족합니다.", new EpisodeDto.PaymentException.Res("/payment"));

        // 가격만큼 코인 수량을 감소
        Coin c = new Coin(m, CoinStatus.USE, CoinType.COIN, -e.getPrice(), e.getComic().getTitle() + " " + e.getTitle() + " 구매");
        List<CoinDetail> cdList = new ArrayList<CoinDetail>();
        List<CoinDto.CoinDetails.Res> coinDetails = coinRepository.getCoinDetails(memberId, CoinType.COIN);
        
        int nowCoinPrice = e.getPrice();
        // 누적된 포인트들의 상세 이력을 조회하여 만료되거나 소진되지 않은 포인트만 찾아서 감소
        for (CoinDto.CoinDetails.Res detail : coinDetails) {
            if (nowCoinPrice == 0) break;
            int recordedAmount; // 사용 이력에 남길 코인수량
            // 감소시킬 코인이 남아있는 코인보다 높으면
            if (nowCoinPrice > detail.getAmount()) recordedAmount = detail.getAmount(); // 남은 코인의 최대 수량을 기록
            // 낮으면
            else recordedAmount = nowCoinPrice; // 감소시킬 코인 전부 기록

            nowCoinPrice -= recordedAmount;
            CoinDetail cd = new CoinDetail(c, CoinStatus.USE, CoinType.COIN, -recordedAmount);
            cd.setCoinAccumId(detail.getCoinAccumId());
            cdList.add(cd);
        }
        c.setCoinDetails(cdList);
        coinRepository.save(c);

        // 에피소드를 소장목록에 저장
        MemberEpisode me = new MemberEpisode(m, e);
        memberEpisodeRepository.save(me);

        // 남은 코인을 결과값으로 리턴
        CoinDto.Amounts.Res result = new CoinDto.Amounts.Res();
        result.setCoinAmount(memberRepository.getMemberCoinAmount(memberId));
        result.setBonusCoinAmount(memberRepository.getMemberBonusCoinAmount(memberId));
        result.setPointAmount(memberRepository.getMemberPointAmount(memberId));

        return result;
    }
}