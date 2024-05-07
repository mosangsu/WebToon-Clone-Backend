package com.lezhin.clone.backend.dto;

import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;

import com.lezhin.clone.backend.entity.Member;

public class UserPrincipal extends User {
    public UserPrincipal(Member member){
        super(member.getUsername(), member.getPassword(), AuthorityUtils.createAuthorityList());
    }
}