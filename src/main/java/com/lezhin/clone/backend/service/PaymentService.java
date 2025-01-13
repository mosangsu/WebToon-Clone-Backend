package com.lezhin.clone.backend.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.naming.AuthenticationException;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;

import com.lezhin.clone.backend.dto.CoinDto;
import com.lezhin.clone.backend.dto.MemberDto;
import com.lezhin.clone.backend.dto.PaymentDto;
import com.lezhin.clone.backend.dto.SubscribedComicDto;
import com.lezhin.clone.backend.entity.Coin;
import com.lezhin.clone.backend.entity.CoinDetail;
import com.lezhin.clone.backend.entity.CoinPackage;
import com.lezhin.clone.backend.entity.Member;
import com.lezhin.clone.backend.enums.CoinStatus;
import com.lezhin.clone.backend.enums.CoinType;
import com.lezhin.clone.backend.enums.MemberType;
import com.lezhin.clone.backend.enums.OAuth2Provider;
import com.lezhin.clone.backend.enums.PackageType;
import com.lezhin.clone.backend.repository.CoinPackageRepository;
import com.lezhin.clone.backend.repository.CoinRepository;
import com.lezhin.clone.backend.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Service
@Transactional
public class PaymentService {
    private final CoinPackageRepository coinPackageRepository;
    private final MemberRepository memberRepository;
    private final CoinRepository coinRepository;

    private final ModelMapper modelMapper;
    
    public List<PaymentDto.List.Res> getPayment(Long memberId) {
        Member m = memberRepository.findById(memberId).orElse(new Member());
        List<PaymentDto.List.Res> result = coinPackageRepository.getCoinPackages(memberId, m.isFirstPurchase());
        return result;
    }

    @Transactional
    public CoinDto.Amounts.Res purchaseCoin(Long memberId, Long coinPackageId) throws Exception {
        Member m = memberRepository.findById(memberId).orElse(new Member());
        CoinPackage cp = coinPackageRepository.findById(coinPackageId).orElse(new CoinPackage());
        LocalDateTime now = LocalDateTime.now();

        // 첫결제 상품을 구입할 경우
        if (cp.getType().equals(PackageType.FIRST_PURCHASE)) {
            if (m.isFirstPurchase()) {  // 첫결제 유저가 맞으면 false로 전환후 결제 진행
                m.setFirstPurchase(false);
            } else {    // 아니면 예외 처리
                throw new Exception("이미 첫결제 상품을 구입한 유저입니다.");
            }
        }

        // 코인 추가
        Coin c = new Coin(m, CoinStatus.ACCUMULATE, CoinType.COIN, cp.getCoins(), "코인 충전");
        List<CoinDetail> cdList = new ArrayList<CoinDetail>();
        CoinDetail cd = new CoinDetail(c, CoinStatus.ACCUMULATE, CoinType.COIN, cp.getCoins());
        cdList.add(cd);
        c.setCoinDetails(cdList);
        coinRepository.save(c);
        cd.setCoinAccumId(cd.getCoinDetailId());

        // 보너스 코인 추가
        if (cp.getBonusCoins() > 0) {
            Coin bc = new Coin(m, CoinStatus.ACCUMULATE, CoinType.BONUSCOIN, cp.getBonusCoins(), "보너스 코인 충전", now.plusYears(1));
            List<CoinDetail> bcdList = new ArrayList<CoinDetail>();
            CoinDetail bcd = new CoinDetail(bc, CoinStatus.ACCUMULATE, CoinType.BONUSCOIN, cp.getBonusCoins(), now.plusYears(1));
            bcdList.add(bcd);
            bc.setCoinDetails(bcdList);
            coinRepository.save(bc);
            bcd.setCoinAccumId(bcd.getCoinDetailId());
        }

        // 포인트 추가
        // 포인트를 소모하는 상품일 경우와 아닌 경우 분기
        if (cp.getType().equals(PackageType.POINT)) {
            Coin p = new Coin(m, CoinStatus.USE, CoinType.POINT, -cp.getPointPrice(), "포인트 상품 구매", null);
            List<CoinDetail> pdList = new ArrayList<CoinDetail>();
            List<CoinDto.CoinDetails.Res> pointDetails = coinRepository.getCoinDetails(memberId, CoinType.POINT);
            
            // 누적된 포인트보다 결제하려는 포인트가 많을 경우 에러 발생
            if (pointDetails.stream().map(pd -> pd.getAmount()).reduce(0, Integer::sum) < cp.getPointPrice()) throw new Exception("결제에 필요한 포인트가 부족합니다!");
            
            int nowPointPrice = cp.getPointPrice();
            // 누적된 포인트들의 상세 이력을 조회하여 만료되거나 소진되지 않은 포인트만 찾아서 감소
            for (CoinDto.CoinDetails.Res detail : pointDetails) {
                if (nowPointPrice == 0) break;
                int recordedAmount;
                if (nowPointPrice > detail.getAmount()) recordedAmount = detail.getAmount();
                else recordedAmount = nowPointPrice;
                nowPointPrice -= recordedAmount;
                
                CoinDetail pd = new CoinDetail(p, CoinStatus.USE, CoinType.POINT, -recordedAmount, null);
                pd.setCoinAccumId(detail.getCoinAccumId());
                pdList.add(pd);
            }
            p.setCoinDetails(pdList);
            coinRepository.save(p);
        } else {
            if (cp.getPoints() > 0) {
                Coin p = new Coin(m, CoinStatus.ACCUMULATE, CoinType.POINT, cp.getPoints(), "포인트 충전", now.plusYears(1));
                List<CoinDetail> pdList = new ArrayList<CoinDetail>();
                CoinDetail pd = new CoinDetail(p, CoinStatus.ACCUMULATE, CoinType.POINT, cp.getPoints(), now.plusYears(1));
                pdList.add(pd);
                p.setCoinDetails(pdList);
                coinRepository.save(p);
                pd.setCoinAccumId(pd.getCoinDetailId());
            }
        }

        // 남은 코인을 결과값으로 리턴
        CoinDto.Amounts.Res result = new CoinDto.Amounts.Res();
        result.setCoinAmount(memberRepository.getMemberCoinAmount(memberId));
        result.setBonusCoinAmount(memberRepository.getMemberBonusCoinAmount(memberId));
        result.setPointAmount(memberRepository.getMemberPointAmount(memberId));

        return result;
    }
}