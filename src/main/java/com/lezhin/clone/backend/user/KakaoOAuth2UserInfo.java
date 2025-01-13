package com.lezhin.clone.backend.user;

import java.util.Map;

import com.lezhin.clone.backend.enums.OAuth2Provider;

public class KakaoOAuth2UserInfo extends OAuth2UserInfo {

    private Long id;

    @SuppressWarnings("unchecked")
    public KakaoOAuth2UserInfo(String accessToken, Map<String, Object> attributes) {
        super(accessToken, (Map<String, Object>) attributes.get("kakao_account"));
        this.id = (Long) attributes.get("id");
    }

    @Override
    public OAuth2Provider getProvider() {
        return OAuth2Provider.KAKAO;
    }
    
    @Override
    public String getId() {
        return this.id.toString();
    }
 
    @Override
    public String getName() {
        return (String) ((Map<String, Object>) attributes.get("profile")).get("nickname");
    }
 
    @Override
    public String getEmail() {
        return (String) attributes.get("email");
    }
 
    @Override
    public String getProfileImageUrl() {
        return (String) ((Map<String, Object>) attributes.get("profile")).get("thumbnail_image_url");
    }
}