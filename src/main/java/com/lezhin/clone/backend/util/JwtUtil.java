package com.lezhin.clone.backend.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import com.lezhin.clone.backend.entity.Member;
// import com.lezhin.clone.backend.entity.Member;
import com.lezhin.clone.backend.enums.MemberType;
import com.nimbusds.jose.util.StandardCharset;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Date;

import javax.crypto.SecretKey;

@Component
public class JwtUtil {

    public final static long TOKEN_VALIDATION_MILLISECOND = 1000L * 60 * 60; // 1시간
    public final static long REFRESH_TOKEN_VALIDATION_MILLISECOND = 1000L * 60 * 60 * 24 * 2; // 2일

    final static public String ACCESS_TOKEN_NAME = "accessToken";
    final static public String REFRESH_TOKEN_NAME = "refreshToken";

    @Value("${spring.jwt.secret}")
    private String SECRET_KEY;

    private SecretKey getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(this.SECRET_KEY);
        byte[] a = Jwts.SIG.HS256.key().build().getEncoded();
        System.out.println(Base64.getEncoder().encodeToString(a));
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public Claims extractAllClaims(String token) throws ExpiredJwtException {
        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public Date getExpiration(String token) {
        return extractAllClaims(token).getExpiration();
    }

    public String getUsername(String token) {
        return extractAllClaims(token).get("username", String.class);
    }

    public Long getMemberId(String token) {
        return extractAllClaims(token).get("id", Long.class);
    }

    public MemberType getMemberType(String token) {
        return extractAllClaims(token).get("memberType", MemberType.class);
    }

    public Boolean isTokenExpired(String token) {
        final Date expiration = extractAllClaims(token).getExpiration();
        return expiration.before(new Date());
    }
    
    public String generateToken(String username, Long memberId, MemberType memberType) {
        return doGenerateToken(username, memberId, TOKEN_VALIDATION_MILLISECOND, memberType);
    }
    
    public String generateToken(Member member) {
        return doGenerateToken(member.getUsername(), member.getMemberId(), TOKEN_VALIDATION_MILLISECOND, member.getType());
    }

    public String generateRefreshToken(String username, Long memberId, MemberType memberType) {
        return doGenerateToken(username, memberId, REFRESH_TOKEN_VALIDATION_MILLISECOND, memberType);
    }

    public String generateRefreshToken(Member member) {
        return doGenerateToken(member.getUsername(), member.getMemberId(), REFRESH_TOKEN_VALIDATION_MILLISECOND, member.getType());
    }

    public String doGenerateToken(String username, Long memberId, long expireTime, MemberType memberType) {

        // claims.issuedAt(null)
        String jwt = Jwts.builder()
            .claims()
            .add("username", username)
            .add("id", memberId)
            .add("memberType", memberType.getAuthority())
            .and()
            .signWith(getSigningKey())
            .issuedAt(new Date(System.currentTimeMillis()))
            .expiration(new Date(System.currentTimeMillis() + expireTime))
            .compact();

        return jwt;
    }

    public Boolean validateToken(String token, UserDetails userDetails) {
        final String username = getUsername(token);

        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

}