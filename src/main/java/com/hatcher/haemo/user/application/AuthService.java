package com.hatcher.haemo.user.application;

import io.jsonwebtoken.*;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Date;

import static com.hatcher.haemo.common.enums.BaseResponseStatus.*;

@Service
@RequiredArgsConstructor
public class AuthService {

    @Value("${jwt.access-token-validity-in-millis}")
    private long accessTokenExpirationTime;

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

    // Token validation check
    public Boolean validateToken(String token) {
        try {
            Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);
            return true;
        } catch (SignatureException ex) {
            System.out.println("Invalid JWT signature");
            throw new JwtException(INVALID_JWT_SIGNATURE.getMessage());
        } catch (MalformedJwtException ex) {
            System.out.println("Invalid JWT token");
            throw new JwtException(INVALID_ACCESS_TOKEN.getMessage());
        } catch (ExpiredJwtException ex) {
            System.out.println("Expired JWT token");
            throw new JwtException(EXPIRED_ACCESS_TOKEN.getMessage());
        } catch (UnsupportedJwtException ex) {
            System.out.println("Unsupported JWT token");
            throw new JwtException(UNSUPPORTED_JWT_TOKEN.getMessage());
        } catch (IllegalArgumentException ex) {
            System.out.println("JWT claims string is empty.");
            throw new JwtException(EMPTY_JWT_CLAIM.getMessage());
        }
    }
}
