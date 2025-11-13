package com.storix.storix_api.global.security;

import com.storix.storix_api.domains.user.domain.RefreshToken;
import com.storix.storix_api.domains.user.repository.RefreshTokenRepository;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class TokenProvider implements InitializingBean {

    private final UserDetailsService userDetailsService;
    private final RefreshTokenRepository refreshTokenRepository;

    @Value("${JWT_SECRET_KEY}") private String secretKey;
    @Value("${JWT_ACCESS_TOKEN_VALIDITY_MS}") private long accessTokenValidityMs;
    @Value(("${JWT_REFRESH_TOKEN_VALIDITY_MS}")) private long refreshTokenValidityMs;


    private Key key;

    @Override
    public void afterPropertiesSet() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        this.key = Keys.hmacShaKeyFor(keyBytes); // HMAC-SHA
    }

    /** 요청 헤더에서 Bearer 토큰만 추출 */
    public String getAccessToken(HttpServletRequest request) {
        String header = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (header != null && header.startsWith("Bearer ")) {
            return header.substring(7);
        }
        return null;
    }

    /** 액세스 토큰 생성: subject = userId, roles = 권한 CSV */
    public String createAccessToken(Long id, Authentication authentication) {
        String authorities = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        Date now = new Date();
        Date expiry = new Date(now.getTime() + accessTokenValidityMs);

        return Jwts.builder()
                .setSubject(String.valueOf(id))      // user 식별자
                .claim("roles", authorities)          // 권한
                .setIssuedAt(now)
                .setExpiration(expiry)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    public String createRefreshToken(Long userId) {
        Date now = new Date();
        Date expiry = new Date(now.getTime() + refreshTokenValidityMs);

        String token = Jwts.builder()
                .setSubject(String.valueOf(userId))
                .claim("type", "refresh")
                .setIssuedAt(now)
                .setExpiration(expiry)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();

        // @TimeToLive 단위 변환
        long ttlSeconds = refreshTokenValidityMs / 1000L;

        RefreshToken refreshToken = RefreshToken.builder()
                .id(String.valueOf(userId))   // key: userId 기준
                .refreshToken(token)
                .ttl(ttlSeconds)
                .build();

        refreshTokenRepository.save(refreshToken);

        return token;
    }

    /** 토큰에서 사용자 ID(subject) 추출 */
    public String getTokenUserId(String token) {
        return Jwts.parserBuilder().setSigningKey(key).build()
                .parseClaimsJws(token)
                .getBody().getSubject();
    }

    /** 토큰을 기반으로 Authentication 생성 (SecurityContext에 넣을 용도) */
    public Authentication getAuthentication(String token) {
        String userId = getTokenUserId(token);
        UserDetails userDetails = userDetailsService.loadUserByUsername(userId);
        return new UsernamePasswordAuthenticationToken(
                userDetails, token, userDetails.getAuthorities());
    }

    /** 유효성 검증 (서명/만료/포맷) */
    public boolean validateAccessToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (ExpiredJwtException e) {
            return false; // 만료
        } catch (JwtException | IllegalArgumentException e) {
            return false; // 서명 불일치/변조/형식 오류
        }
    }

    /** 리프레시 토큰 유효성 검증 + Redis에 존재 여부 검증 */
    public boolean validateRefreshToken(String token) {
        try {
            // 1) JWT 형식/서명/만료 검증
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();

            // type 이 refresh인지 체크
            Object type = claims.get("type");
            if (type == null || !"refresh".equals(type.toString())) {
                return false;
            }

            String userId = claims.getSubject();

            // 2) Redis 에서 해당 사용자 id로 저장된 refresh token 가져오기
            return refreshTokenRepository.findById(userId)
                    .map(saved -> saved.getRefreshToken().equals(token))
                    .orElse(false);

        } catch (ExpiredJwtException e) {
            return false; // 만료
        } catch (JwtException | IllegalArgumentException e) {
            return false; // 서명 불일치/변조/형식 오류
        }
    }
}
