package com.lezhin.clone.backend.user;
import java.util.Map;

import com.lezhin.clone.backend.enums.OAuth2Provider;

public abstract class OAuth2UserInfo {
    protected final Map<String, Object> attributes;
    private final String accessToken;
    private boolean isExist;
    
    public OAuth2UserInfo(String accessToken, Map<String, Object> attributes) {
        this.accessToken = accessToken;
        this.attributes = attributes;
    }

    public Map<String, Object> getAttributes() {
        return attributes;
    }

    public String getAccessToken() {
        return this.accessToken;
    }

    public void setIsExist(boolean isExist) {
        this.isExist = isExist;
    }
    public boolean isExist() {
        return this.isExist;
    }

    public abstract OAuth2Provider getProvider();

    public abstract String getId();

    public abstract String getEmail();

    public abstract String getName();

    public abstract String getProfileImageUrl();
}