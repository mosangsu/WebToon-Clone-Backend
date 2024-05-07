package com.lezhin.clone.backend.user;
import java.util.Map;

import com.lezhin.clone.backend.enums.OAuth2Provider;

public interface OAuth2UserInfo {

    OAuth2Provider getProvider();

    String getAccessToken();

    Map<String, Object> getAttributes();

    String getId();

    String getEmail();

    String getName();

    String getFirstName();

    String getLastName();

    String getNickname();

    String getProfileImageUrl();

    void setIsExist(boolean isExist);
    boolean isExist();
}