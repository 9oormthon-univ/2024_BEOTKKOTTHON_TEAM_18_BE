package com.hatcher.haemo.user.application;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Date;

@Service
@RequiredArgsConstructor
public class AuthService {

    @Value("${jwt.access-token-validity-in-millis}")
    private int accessTokenExpirationTime;

    @Value("${jwt.secret-key}")
    private String secretKey;

    // accessToken 발급
    public String generateAccessToken(Long userIdx) {
        Claims claims = Jwts.claims();
        claims.put("userIdx", userIdx);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + accessTokenExpirationTime))
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }

    // Request에서 userIdx 추출
    public Long getUserIdx() {
        String token = getTokenFromRequest();
        if (token == null || token.equals("")) return null;
        return getUserIdxFromToken(token);
    }

    // Token에서 userIdx 추출
    public Long getUserIdxFromToken(String token) {
        return Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(token)
                .getBody()
                .get("userIdx", Long.class);
    }

    // Request에서 Token 추출
    public String getTokenFromRequest() {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        else return null;
    }
}
