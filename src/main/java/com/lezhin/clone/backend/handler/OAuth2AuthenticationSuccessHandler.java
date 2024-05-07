package com.lezhin.clone.backend.handler;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lezhin.clone.backend.enums.OAuth2Provider;
import com.lezhin.clone.backend.user.HttpCookieOAuth2AuthorizationRequestRepository;
import com.lezhin.clone.backend.user.UserDetailsImpl;
import com.lezhin.clone.backend.util.CookieUtil;
import com.lezhin.clone.backend.util.JwtUtil;
import com.lezhin.clone.backend.util.RedisUtil;

import java.io.IOException;
import java.util.Optional;

import static com.lezhin.clone.backend.user.HttpCookieOAuth2AuthorizationRequestRepository.MODE_PARAM_COOKIE_NAME;
import static com.lezhin.clone.backend.user.HttpCookieOAuth2AuthorizationRequestRepository.REDIRECT_URI_PARAM_COOKIE_NAME;

@Slf4j
@RequiredArgsConstructor
@Component
public class OAuth2AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final HttpCookieOAuth2AuthorizationRequestRepository httpCookieOAuth2AuthorizationRequestRepository;
    // private final OAuth2UserUnlinkManager oAuth2UserUnlinkManager;

    private final JwtUtil jwtUtil;
    private final RedisUtil redisUtil;
    private final ObjectMapper customObjectMapper;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
            Authentication authentication) throws IOException {

        String targetUrl;

        targetUrl = determineTargetUrl(request, response, authentication);

        if (response.isCommitted()) {
            logger.debug("Response has already been committed. Unable to redirect to " + targetUrl);
            return;
        }

        clearAuthenticationAttributes(request, response);
        getRedirectStrategy().sendRedirect(request, response, targetUrl);
    }

    protected String determineTargetUrl(HttpServletRequest request, HttpServletResponse response,
            Authentication authentication) {

        Optional<String> redirectUri = CookieUtil.getCookie(request, REDIRECT_URI_PARAM_COOKIE_NAME)
                .map(Cookie::getValue);

        String targetUrl = redirectUri.orElse(getDefaultTargetUrl());

        String mode = CookieUtil.getCookie(request, MODE_PARAM_COOKIE_NAME)
                .map(Cookie::getValue)
                .orElse("");

        UserDetailsImpl principal = getOAuth2UserPrincipal(authentication);

        if (principal == null) {
            return UriComponentsBuilder.fromUriString(targetUrl)
                    .queryParam("error", "Login failed")
                    .build().toUriString();
        }

        if ("login".equalsIgnoreCase(mode)) {
            // TODO: DB 저장
            // TODO: 액세스 토큰, 리프레시 토큰 발급
            // TODO: 리프레시 토큰 DB 저장
            log.info("email={}, name={}, nickname={}, accessToken={}", principal.getUserInfo().getEmail(),
                principal.getUserInfo().getName(),
                principal.getUserInfo().getNickname(),
                principal.getUserInfo().getAccessToken());
            // 기존에 가입된 계정이 있으면 로그인 처리
            if (principal.getUserInfo().isExist()) {
                String token = jwtUtil.generateToken(principal.getUsername(), principal.getMemberId(), principal.getMemberType());
                String refreshJwt = jwtUtil.generateRefreshToken(principal.getUsername(), principal.getMemberId(), principal.getMemberType());

                CookieUtil.addCookie(response, JwtUtil.REFRESH_TOKEN_NAME, refreshJwt, (int) JwtUtil.REFRESH_TOKEN_VALIDATION_MILLISECOND);
                redisUtil.setDataExpire("refreshToken", refreshJwt, principal.getUsername(), JwtUtil.REFRESH_TOKEN_VALIDATION_MILLISECOND);
    
                // 리소스 서버에서 발급받은 Access 토큰은 추후 관련 API 호출이 필요할때 관리하도록 한다.
                // String accessToken = principal.getUserInfo().getAccessToken();

                // 일반 로그인의 경우와 동일한 데이터를 쿼리스트링 형태로 반환한다.
                try {
                    return UriComponentsBuilder.fromUriString(targetUrl)
                            .queryParam("access_token", token)
                            .queryParam("id", principal.getMemberId())
                            .queryParam("username", principal.getUsername())
                            .queryParam("type", customObjectMapper.writeValueAsString(principal.getMemberType()) )
                            .build().toUriString();
                } catch (JsonProcessingException e) {
                    log.error(e.getMessage());
                }
            }
            // 없을 경우 회원가입 진행
            else {
                return UriComponentsBuilder.fromUriString(targetUrl)
                        .queryParam("email", principal.getUserInfo().getEmail())
                        .queryParam("provider", principal.getUserInfo().getProvider())
                        .build().toUriString();
            }

        } else if ("unlink".equalsIgnoreCase(mode)) {

            String accessToken = principal.getUserInfo().getAccessToken();
            OAuth2Provider provider = principal.getUserInfo().getProvider();

            // TODO: DB 삭제
            // TODO: 리프레시 토큰 삭제
            // oAuth2UserUnlinkManager.unlink(provider, accessToken);

            return UriComponentsBuilder.fromUriString(targetUrl)
                    .build().toUriString();
        }

        return UriComponentsBuilder.fromUriString(targetUrl)
                .queryParam("error", "Login failed")
                .build().toUriString();
    }

    private UserDetailsImpl getOAuth2UserPrincipal(Authentication authentication) {
        Object principal = authentication.getPrincipal();

        if (principal instanceof UserDetailsImpl) {
            return (UserDetailsImpl) principal;
        }
        return null;
    }

    protected void clearAuthenticationAttributes(HttpServletRequest request, HttpServletResponse response) {
        super.clearAuthenticationAttributes(request);
        httpCookieOAuth2AuthorizationRequestRepository.removeAuthorizationRequestCookies(request, response);
    }
}