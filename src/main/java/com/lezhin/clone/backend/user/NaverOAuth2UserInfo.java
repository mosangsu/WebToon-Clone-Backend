package com.lezhin.clone.backend.user;

import java.util.Map;

import com.lezhin.clone.backend.enums.OAuth2Provider;

public class NaverOAuth2UserInfo extends OAuth2UserInfo {

    @SuppressWarnings("unchecked")
    public NaverOAuth2UserInfo(String accessToken, Map<String, Object> attributes) {
        super(accessToken, (Map<String, Object>) attributes.get("response"));
    }

    @Override
    public OAuth2Provider getProvider() {
        return OAuth2Provider.NAVER;
    }
    
    @Override
    public String getId() {
        return (String) attributes.get("id");
    }
 
    @Override
    public String getName() {
        return (String) attributes.get("name");
    }
 
    @Override
    public String getEmail() {
        return (String) attributes.get("email");
    }
 
    @Override
    public String getProfileImageUrl() {
        return (String) attributes.get("profile_image");
    }
}