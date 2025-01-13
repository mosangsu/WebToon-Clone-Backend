package com.lezhin.clone.backend.filter;

import com.lezhin.clone.backend.enums.MemberType;
import com.lezhin.clone.backend.user.UserDetailsImpl;
import com.lezhin.clone.backend.util.JwtUtil;

import io.jsonwebtoken.ExpiredJwtException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@Component
public class JwtRequestFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        System.out.println("====================================================================================");

        String authHeader = request.getHeader("Authorization");
        String accessToken = authHeader != null ? (authHeader.substring("Bearer ".length())) : "";

        Long memberId = null;

        System.out.println("=====================" + accessToken);


        try{
            if(!accessToken.equals("")){
                memberId = jwtUtil.getMemberId(accessToken);
            }
            if(memberId != null){
                // UserDetailsImpl userDetails = (UserDetailsImpl) userPrincipalDetailService.loadUserByUsername(username);

                UserDetailsImpl userDetails = new UserDetailsImpl(MemberType.find(jwtUtil.getMemberType(accessToken)));

                UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(userDetails,null,userDetails.getAuthorities());
                usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
            
                request.setAttribute("nickname", jwtUtil.getNickname(accessToken));
                request.setAttribute("memberId", jwtUtil.getMemberId(accessToken));
                request.setAttribute("memberType", jwtUtil.getMemberType(accessToken));
                System.out.println("===================================================attribute=================");
            }
        }catch (ExpiredJwtException e){
            log.info("Access 토큰 만료");
            response.setStatus(401);
            // filterChain.doFilter(request, response);

            return;
        }catch(Exception e){
            log.error(e.getMessage()); 
        }

        filterChain.doFilter(request,response);
    }
}