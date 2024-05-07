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

    private OAuth2UserInfo userInfo;

    public UserDetailsImpl(OAuth2UserInfo userInfo, Long memberId, MemberType memberType) {
        this.userInfo = userInfo;
        this.memberId = memberId;
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
        return userInfo.getEmail();
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
        return userInfo.getAttributes();
    }

    @Override
    public String getName() {
        return userInfo.getEmail();
    }

    public OAuth2UserInfo getUserInfo() {
        return userInfo;
    }
}
