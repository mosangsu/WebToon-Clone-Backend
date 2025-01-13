package com.lezhin.clone.backend.user;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import com.lezhin.clone.backend.enums.MemberType;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserDetailsImpl implements OAuth2User, UserDetails {

    private Long memberId;

    private MemberType memberType;

    private String nickname;

    private OAuth2UserInfo userInfo;

    public UserDetailsImpl(OAuth2UserInfo userInfo, Long memberId, String nickname, MemberType memberType) {
        this.userInfo = userInfo;
        this.memberId = memberId;
        this.nickname = nickname;
        this.memberType = memberType;
    }

    public UserDetailsImpl(OAuth2UserInfo userInfo, MemberType memberType) {
        this.userInfo = userInfo;
        this.memberType = memberType;
    }

    public UserDetailsImpl(MemberType memberType) {
        this.memberType = memberType;
    }

    @Override
    public String getUsername() {
        if (userInfo != null) return userInfo.getEmail();
        else return null;
    }

    public String getNickname() {
        return this.nickname;
    }

    @Override
    public String getPassword() {
        return null;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        SimpleGrantedAuthority simpleGrantedAuthority = new SimpleGrantedAuthority(memberType.getName());
        Collection<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(simpleGrantedAuthority);
        return authorities;
    }

    @Override
    public Map<String, Object> getAttributes() {
        if (userInfo != null) return userInfo.getAttributes();
        else return null;
    }

    @Override
    public String getName() {
        if (userInfo != null) return userInfo.getEmail();
        else return null;
    }

    public OAuth2UserInfo getUserInfo() {
        return userInfo;
    }
}
