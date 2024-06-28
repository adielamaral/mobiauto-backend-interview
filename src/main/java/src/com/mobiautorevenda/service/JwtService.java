package src.com.mobiautorevenda.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import src.com.mobiautorevenda.dto.user.UserAccountDto;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Slf4j
@Service
public class JwtService {

    @Value("${mobiauto.secret}")
    private String secret;
    @Value("${mobiauto.access-token-validity}")
    private long validity;

    public String extractUserName(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String userName = extractUserName(token);
        return (userName.equals(userDetails.getUsername())) && !isTokenExpired(token);
    }

    private <T> T extractClaim(String token, Function<Claims, T> claimsResolvers) {
        final Claims claims = extractAllClaims(token);
        return claimsResolvers.apply(claims);
    }

    public String generateToken(UserAccountDto userData) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("profile", userData.getProfile());

        Date currentDate = new Date(System.currentTimeMillis());
        Date expirationDate = new Date(System.currentTimeMillis() + validity);
        log.info("CurrentDate: {} / ExpirationDate: {}", currentDate, expirationDate);
        return Jwts.builder()
                .claims(claims)
                .subject(userData.getEmail())
                .issuedAt(currentDate)
                .expiration(expirationDate)
                .signWith(getSigningKey())
                .compact();
    }

    private boolean isTokenExpired(String token) {
        final var expired = extractExpiration(token).before(new Date());
        if (expired) {
            log.info("============================ INVALID TOKEN ============================");
        }
        return expired;
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private Key getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secret);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
