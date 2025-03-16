package github.eojinkim1.registrationapi.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;


import java.security.Key;
import java.util.Date;


@Component
public class JwtUtil {
    private static final String SECRET_KEY = "secretkeyisecretsecretkeyisecretsecretkeyisecret";
    private static final long EXPIRATION_TIME = 1000 * 60 * 60; // 1시간 3,600,000 (밀리초 단위)

    private final Key key = Keys.hmacShaKeyFor(SECRET_KEY.getBytes());

    public String generateToken(String email) { // JWT 생성
        return Jwts.builder()
                .setSubject(email) // 토큰에 이메일 저장
                .setIssuedAt(new Date()) // 발급 시간
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME)) // 만료 시간
                .signWith(key, SignatureAlgorithm.HS256) // 서명
                .compact();
    }

    public String validateToken(String token) { // 토큰 검증 후 이메일 반환
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody()
                    .getSubject(); // 이메일 반환
        } catch (Exception e) {
            throw new RuntimeException("유효하지 않은 JWT 토큰입니다.");
        }
    }


}
