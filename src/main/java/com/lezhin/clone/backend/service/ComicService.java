package com.lezhin.clone.backend.service;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.stream.Collectors;
import java.util.List;

import com.lezhin.clone.backend.dto.ComicDto;
import com.lezhin.clone.backend.dto.SubscribedComicDto;
import com.lezhin.clone.backend.entity.Comic;
import com.lezhin.clone.backend.entity.Episode;
import com.lezhin.clone.backend.entity.LikedComic;
import com.lezhin.clone.backend.entity.Member;
import com.lezhin.clone.backend.entity.MemberEpisode;
import com.lezhin.clone.backend.entity.SubscribedComic;
import com.lezhin.clone.backend.enums.EpisodeSchedule;
import com.lezhin.clone.backend.enums.RankingType;
import com.lezhin.clone.backend.repository.ComicRepository;
import com.lezhin.clone.backend.repository.EpisodeRepository;
import com.lezhin.clone.backend.repository.LikedComicRepository;
import com.lezhin.clone.backend.repository.MemberEpisodeRepository;
import com.lezhin.clone.backend.repository.MemberRepository;
import com.lezhin.clone.backend.repository.SubscribedComicRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
@Transactional
public class ComicService {

    private final MemberRepository memberRepository;

    private final ComicRepository comicRepository;

    private final EpisodeRepository episodeRepository;

    private final SubscribedComicRepository subscribedComicRepository;

    private final MemberEpisodeRepository memberEpisodeRepository;

    private final LikedComicRepository likedComicRepository;

    private final ModelMapper modelMapper;

    public List<ComicDto.Scheduled.Res> getScheduled(@NonNull EpisodeSchedule episodeSchedule) {
        List<Comic> e = comicRepository.findByEpisodeSchedule(episodeSchedule);
        return e.stream().map(p -> modelMapper.map(p, ComicDto.Scheduled.Res.class)).collect(Collectors.toList());
    }

    public Page<ComicDto.Summary.List.Res> getAdultComicList(Pageable pageable) {
        Page<Comic> c = comicRepository.findByRating("19", pageable);
        return new PageImpl<>(c.getContent().stream().map(p -> modelMapper.map(p, ComicDto.Summary.List.Res.class)).collect(Collectors.toList()), c.getPageable(), c.getTotalElements());
    }

    public Page<ComicDto.Summary.List.Res> getNewComicList(Pageable pageable) {
        Page<Comic> c = comicRepository.findByIsNew(true, pageable);
        return new PageImpl<>(c.getContent().stream().map(p -> modelMapper.map(p, ComicDto.Summary.List.Res.class)).collect(Collectors.toList()), c.getPageable(), c.getTotalElements());
    }

    public Page<ComicDto.Summary.List.Res> getCompletedComicList(Pageable pageable) {
        Page<Comic> c = comicRepository.findByIsCompleted(true, pageable);
        return new PageImpl<>(c.getContent().stream().map(p -> modelMapper.map(p, ComicDto.Summary.List.Res.class)).collect(Collectors.toList()), c.getPageable(), c.getTotalElements());
    }

    public Page<ComicDto.Summary.List.Res> getFreeComicList(Pageable pageable) {
        Page<Comic> c = comicRepository.findByIsFree(true, pageable);
        return new PageImpl<>(c.getContent().stream().map(p -> modelMapper.map(p, ComicDto.Summary.List.Res.class)).collect(Collectors.toList()), c.getPageable(), c.getTotalElements());
    }

    public Page<ComicDto.Summary.List.Res> getPresubComicList(Pageable pageable) {
        Page<Comic> c = comicRepository.findByIsPresub(true, pageable);
        return new PageImpl<>(c.getContent().stream().map(p -> modelMapper.map(p, ComicDto.Summary.List.Res.class)).collect(Collectors.toList()), c.getPageable(), c.getTotalElements());
    }

    public Page<ComicDto.Summary.List.Res> getUpdatedComicList(Pageable pageable) {
        Page<Comic> c = comicRepository.findByIsUpdated(true, pageable);
        return new PageImpl<>(c.getContent().stream().map(p -> modelMapper.map(p, ComicDto.Summary.List.Res.class)).collect(Collectors.toList()), c.getPageable(), c.getTotalElements());
    }

    public Page<ComicDto.Summary.List.Res> getEventComicList(Pageable pageable) {
        Page<Comic> c = comicRepository.findByIsEvent(true, pageable);
        return new PageImpl<>(c.getContent().stream().map(p -> modelMapper.map(p, ComicDto.Summary.List.Res.class)).collect(Collectors.toList()), c.getPageable(), c.getTotalElements());
    }

    public Page<ComicDto.Summary.List.Res> getAwardedComicList(Pageable pageable) {
        Page<Comic> c = comicRepository.findByIsAwarded(true, pageable);
        return new PageImpl<>(c.getContent().stream().map(p -> modelMapper.map(p, ComicDto.Summary.List.Res.class)).collect(Collectors.toList()), c.getPageable(), c.getTotalElements());
    }

    public Page<ComicDto.OwnedComic.Res> getOwnedComicList(Long memberId, String q, Pageable pageable) {
        return comicRepository.getOwnedComicList(memberId, q, pageable);
    }

    public Page<ComicDto.Summary.List.Res> getSubscribedComicList(Long memberId, String q, Pageable pageable) {
        return comicRepository.getSubscribedComicList(memberId, q, pageable);
    }

    public Page<ComicDto.Summary.List.Res> getReadComicList(Integer[] idList, String q, Pageable pageable) {
        return comicRepository.getReadComicList(idList, q, pageable);
    }

    public Object getComic(Long memberId, String title) {
        Comic c = comicRepository.findByLinkName(title).orElse(new Comic());
        ComicDto.Comic.Res result = modelMapper.map(c, ComicDto.Comic.Res.class);

        // 찜(구독)한 웹툰 여부 저장
        if(subscribedComicRepository.findByMemberMemberIdAndComicComicId(memberId, c.getComicId()).isPresent())
            result.setSubscribed(true);
        
        // 구매한 에피소드 여부 저장
        int epiIndex = 0;
        for (Episode e : c.getEpisodes()) {
            if(memberEpisodeRepository.findByMemberMemberIdAndEpisodeEpisodeId(memberId, e.getEpisodeId()).isPresent())
                result.getEpisodes().get(epiIndex).setOwned(true);
            epiIndex++;
        }
        return result;
    }

    public Object getTop3Comics(RankingType type) {
        List<Comic> c = null;
        if (type == RankingType.NEW)
            c = comicRepository.findTop3ByIsNewOrderByViewCountDesc(true);
        else if (type == RankingType.EVENT)
            c = comicRepository.findTop3ByIsEventOrderByViewCountDesc(true);
        else
            c = comicRepository.findTop3AllByOrderByViewCountDesc();
        return c.stream().map(p -> modelMapper.map(p, ComicDto.Ranking.Res.class)).collect(Collectors.toList());
    }

    public Object getComics(String tag, String genre, Boolean isCompleted, Boolean isFree, String order, String q, String t, Pageable pageable) {
        return comicRepository.findComicList(tag, genre, isCompleted, isFree, order, q, t, pageable);
    }

    public Object getTop100Comics(String genre, Integer year, Boolean isEvent, Boolean isNew) {
        return comicRepository.findTop100Comics(genre, year, isEvent, isNew);
    }

    public Object subscribeComic(Long memberId, Long comicId) {
        if (memberId == null) throw new NullPointerException("회원정보가 없습니다.");
        Member m = memberRepository.findById(memberId).get();
        Comic c = comicRepository.findById(comicId).get();
        SubscribedComic sc = new SubscribedComic(c, m);
        return modelMapper.map(subscribedComicRepository.save(sc), SubscribedComicDto.Subscribe.Res.class);
    }

    public Object unsubscribeComic(Long memberId, List<Long> comicIds) {
        if (memberId == null) throw new NullPointerException("회원정보가 없습니다.");

        List<SubscribedComic> sc = subscribedComicRepository.findByMemberMemberIdAndComicComicIdIn(memberId, comicIds);

        subscribedComicRepository.deleteAll(sc);

        return null;
    }

    public Object likeComic(Long memberId, Long comicId, boolean isLiked) {
        if (memberId == null) throw new NullPointerException();
        Member m = memberRepository.findById(memberId).get();
        Comic c = comicRepository.findById(comicId).get();
        LikedComic lc = new LikedComic(m, c, isLiked);
        return modelMapper.map(likedComicRepository.save(lc), SubscribedComicDto.Subscribe.Res.class);
    }

    public Object unlikeComic(Long memberId, Long comicId) {
        if (memberId == null) throw new NullPointerException("회원정보가 없습니다.");

        LikedComic lc = likedComicRepository.findByComicComicIdAndMemberMemberId(comicId, memberId);

        likedComicRepository.delete(lc);
        
        return null;
    }

    public Object ownComic(Long memberId, Long episodeID) {
        if (memberId == null) throw new NullPointerException("회원정보가 없습니다.");
        Member m = memberRepository.findById(memberId).get();
        Episode e = episodeRepository.findById(episodeID).get();
        MemberEpisode me = new MemberEpisode(m, e);
        return modelMapper.map(memberEpisodeRepository.save(me), SubscribedComicDto.Subscribe.Res.class);
    }

    public Object disownComic(Long memberId, List<Long> comicIds) {
        if (memberId == null) throw new NullPointerException("회원정보가 없습니다.");

        List<MemberEpisode> me = memberEpisodeRepository.findByMemberMemberIdAndEpisodeComicComicIdIn(memberId, comicIds);

        memberEpisodeRepository.deleteAll(me);

        return null;
    }
}