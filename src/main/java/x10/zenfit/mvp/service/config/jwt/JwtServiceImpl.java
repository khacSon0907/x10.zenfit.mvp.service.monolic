package x10.zenfit.mvp.service.config.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import x10.zenfit.mvp.service.common.domain.dto.request.TokenPayload;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class JwtServiceImpl implements IJwtService {

    private final JwtProperties jwtProperties;

    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(jwtProperties.getSecret().getBytes());
    }

    @Override
    public String generateAccessToken(TokenPayload payload) {

        Date now = new Date();
        Date expiryDate = new Date(
                now.getTime() + jwtProperties.getAccessTokenExpiration()
        );

        return Jwts.builder()
                .setSubject(payload.getUserId())
                .claim("username", payload.getUsername())
                .claim("email", payload.getEmail())
                .claim("roleIds", payload.getRoleIds())
                .claim("roles", payload.getRoles())
                .claim("type", "ACCESS")
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    @Override
    public String generateRefreshToken(TokenPayload payload) {

        Date now = new Date();
        Date expiryDate = new Date(
                now.getTime() + jwtProperties.getRefreshTokenExpiration()
        );

        return Jwts.builder()
                .setSubject(payload.getUserId())
                .claim("username", payload.getUsername())
                .claim("email", payload.getEmail())
                .claim("roleIds", payload.getRoleIds())
                .claim("roles", payload.getRoles())
                .claim("type", "REFRESH")
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    @Override
    public TokenPayload validateAccessToken(String token) {
        return validateToken(token, "ACCESS");
    }

    @Override
    public TokenPayload validateRefreshToken(String token) {
        return validateToken(token, "REFRESH");
    }

    private TokenPayload validateToken(String token, String expectedType) {

        try {

            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(getSigningKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();

            String tokenType = claims.get("type", String.class);

            if (!expectedType.equals(tokenType)) {
                throw new RuntimeException("Token type không hợp lệ");
            }

            return TokenPayload.builder()
                    .userId(claims.getSubject())
                    .username(claims.get("username", String.class))
                    .email(claims.get("email", String.class))
                    .roleIds(extractRoleIds(claims))
                    .roles(extractRoles(claims))
                    .build();

        } catch (ExpiredJwtException e) {

            log.error("JWT token hết hạn", e);
            throw new RuntimeException("Token đã hết hạn");

        } catch (JwtException | IllegalArgumentException e) {

            log.error("JWT token không hợp lệ", e);
            throw new RuntimeException("Token không hợp lệ");
        }
    }

    private List<String> extractRoleIds(Claims claims) {
        Object roleIdsClaim = claims.get("roleIds");

        if (roleIdsClaim instanceof List<?> roleIds) {
            return roleIds.stream()
                    .filter(String.class::isInstance)
                    .map(String.class::cast)
                    .map(String::trim)
                    .filter(roleId -> !roleId.isEmpty())
                    .toList();
        }

        return List.of();
    }

    private List<String> extractRoles(Claims claims) {
        Object rolesClaim = claims.get("roles");

        if (rolesClaim instanceof List<?> roles) {
            return roles.stream()
                    .filter(String.class::isInstance)
                    .map(String.class::cast)
                    .map(String::trim)
                    .filter(role -> !role.isEmpty())
                    .toList();
        }

        String legacyRoleClaim = claims.get("role", String.class);
        if (legacyRoleClaim == null || legacyRoleClaim.isBlank()) {
            return List.of();
        }

        return List.of(legacyRoleClaim.split(",")).stream()
                .map(String::trim)
                .filter(role -> !role.isEmpty())
                .toList();
    }



    @Override
    public boolean isTokenExpired(String token) {

        try {

            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(getSigningKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();

            return claims.getExpiration().before(new Date());

        } catch (Exception e) {

            return true;
        }
    }
    @Override
    public String getUserIdFromToken(String token) {

        try {

            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(getSigningKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();

            return claims.getSubject();

        } catch (JwtException | IllegalArgumentException e) {

            log.error("Không thể lấy userId từ token", e);
            throw new RuntimeException("Token không hợp lệ");
        }
    }



    @Override
    public long getRemainingExpiration(String token) {

        Date expiration = Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getExpiration();

        return expiration.getTime() - System.currentTimeMillis();
    }

    // Deprecated - giữ lại để tương thích
    @Deprecated
    public String generateToken(TokenPayload payload) {
        return generateAccessToken(payload);
    }

    // Deprecated - giữ lại để tương thích
    @Deprecated
    public TokenPayload validateToken(String token) {
        return validateAccessToken(token);
    }
}
