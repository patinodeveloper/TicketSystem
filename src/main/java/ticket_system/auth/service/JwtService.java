package ticket_system.auth.service;

import ticket_system.entities.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtService {

    @Value("${jwt.secret}")
    private String secretKey;

    @Value("${jwt.access-expiration}")
    private long accessTokenExpiration;

    @Value("${jwt.refresh-expiration}")
    private long refreshTokenExpiration;

    // Generar el access token
    public String getAccessToken(User user) {
        return getToken(new HashMap<>(), user, accessTokenExpiration, "access");
    }

    // Generar el refresh token
    public String getRefreshToken(User user) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("id", user.getId());
        claims.put("email", user.getEmail());
        return getToken(claims, user, refreshTokenExpiration, "refresh");
    }

    private String getToken(Map<String, Object> extraClaims, User user, long expiration, String tokenType) {
        extraClaims.put("tokenType", tokenType);

        // Si es accessToken incluimos los datos
        if ("access".equals(tokenType)) {
            String role = "ROLE_" + user.getRole().name();

            extraClaims.put("id", user.getId());
            extraClaims.put("firstName", user.getFirstName());
            extraClaims.put("lastName", user.getLastName());
            extraClaims.put("secondLastName", user.getSecondLastName());
            extraClaims.put("username", user.getUsername());
            extraClaims.put("email", user.getEmail());
            extraClaims.put("role", role);
        }

        return Jwts
                .builder()
                .setClaims(extraClaims)
                .setSubject(user.getEmail())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(getKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    private Key getKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String getUserNameFromToken(String token) {
        return getClaim(token, Claims::getSubject);
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = getUserNameFromToken(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    public boolean isAccessToken(String token) {
        String tokenType = getClaim(token, claims -> claims.get("tokenType", String.class));
        return "access".equals(tokenType);
    }

    public boolean isRefreshToken(String token) {
        String tokenType = getClaim(token, claims -> claims.get("tokenType", String.class));
        return "refresh".equals(tokenType);
    }

    private Claims getAllClaims(String token) {
        return Jwts
                .parserBuilder()
                .setSigningKey(getKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public <T> T getClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Date getExpiration(String token) {
        return getClaim(token, Claims::getExpiration);
    }

    private boolean isTokenExpired(String token) {
        return getExpiration(token).before(new Date());
    }
}