package com.lezhin.clone.backend.user;

import java.util.Map;

import com.lezhin.clone.backend.enums.OAuth2Provider;

public class GoogleOAuth2UserInfo extends OAuth2UserInfo {

    @SuppressWarnings("unchecked")
    public GoogleOAuth2UserInfo(String accessToken, Map<String, Object> attributes) {
        super(accessToken, (Map<String, Object>) attributes);
    }

    @Override
    public OAuth2Provider getProvider() {
        return OAuth2Provider.GOOGLE;
    }
    
    @Override
    public String getId() {
        return (String) attributes.get("sub");
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
        return (String) attributes.get("picture");
    }
}
