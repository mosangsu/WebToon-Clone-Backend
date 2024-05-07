package com.lezhin.clone.backend.service;

import org.modelmapper.ModelMapper;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lezhin.clone.backend.dto.MemberDto;
import com.lezhin.clone.backend.repository.MemberRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
@Transactional
public class MemberService {

    private final MemberRepository memberRepository;

    private final ModelMapper modelMapper;

    public MemberDto.Account.Res getAccount(@NonNull Long memberId) throws Exception {
        return modelMapper.map(memberRepository.getReferenceById(memberId), MemberDto.Account.Res.class);
    }
}