package com.lezhin.clone.backend.service;

import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.lezhin.clone.backend.entity.Member;
import com.lezhin.clone.backend.enums.MemberType;
import com.lezhin.clone.backend.enums.OAuth2Provider;
import com.lezhin.clone.backend.exception.OAuth2AuthenticationProcessingException;
import com.lezhin.clone.backend.repository.MemberRepository;
import com.lezhin.clone.backend.user.KakaoOAuth2UserInfo;
import com.lezhin.clone.backend.user.OAuth2UserInfo;
import com.lezhin.clone.backend.user.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import java.util.Map;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class OAuth2Service extends DefaultOAuth2UserService {

    final private MemberRepository memberRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest oAuth2UserRequest) throws OAuth2AuthenticationException {

        OAuth2User oAuth2User = super.loadUser(oAuth2UserRequest);

        try {
            return processOAuth2User(oAuth2UserRequest, oAuth2User);
        } catch (AuthenticationException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new InternalAuthenticationServiceException(ex.getMessage(), ex.getCause());
        }
    }

    private OAuth2User processOAuth2User(OAuth2UserRequest userRequest, OAuth2User oAuth2User) {

        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        String accessToken = userRequest.getAccessToken().getTokenValue();
        Map<String, Object> attributes = oAuth2User.getAttributes();
        OAuth2UserInfo oAuth2UserInfo = null;
        UserDetailsImpl userDetailsImpl = null;
        if (OAuth2Provider.KAKAO.getRegistrationId().equals(registrationId)) {
            oAuth2UserInfo = new KakaoOAuth2UserInfo(accessToken, attributes);
        } else {
            throw new OAuth2AuthenticationProcessingException(registrationId + " 로그인은 지원되지 않습니다.");
        }

        if (StringUtils.hasText(oAuth2UserInfo.getEmail())) {
            String email = oAuth2UserInfo.getEmail();
            // 해당 메일에 연동되어있거나, 직접 가입한 계정이 있는지 조회
            Optional<Member> findMember = memberRepository.findByIsOauth2AndOauth2EmailOrUsername(false, email, email);
            boolean isMember = findMember.isPresent();
            
            // Oauth2 인증 후 기존 계정이 있으면 해당 계정으로 로그인, 없으면 회원가입으로 분기처리하기 위해 사전 처리 
            oAuth2UserInfo.setIsExist(isMember);    // 기존 계정 유무
            if (isMember) {
                userDetailsImpl = new UserDetailsImpl(oAuth2UserInfo, findMember.get().getMemberId(), findMember.get().getType());
            } else {
                userDetailsImpl = new UserDetailsImpl(oAuth2UserInfo, MemberType.USER);
            }
        }
        else {
            throw new OAuth2AuthenticationProcessingException("Email not found from OAuth2 provider");
        }

        return userDetailsImpl;
    }
}