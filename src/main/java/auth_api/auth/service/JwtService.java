package auth_api.auth.service;

import auth_api.entities.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.SignatureAlgorithm;
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

    private static final String SECRET_KEY = "cA3AAN6Rb+/0FaghSfB9Sj5Fp0cQHxi868lRH09GAgI=";

    public String getToken(User user) {
        return getToken(new HashMap<>(), user);
    }

    private String getToken(Map<String, Object> extraClaims, User user) {
        List<String> roles = user.getAuthorities().stream()
                .map(Object::toString)
                .filter(auth -> auth.startsWith("ROLE_"))
                .toList();

        List<String> permissions = user.getAuthorities().stream()
                .map(Object::toString)
                .filter(auth -> !auth.startsWith("ROLE_"))
                .toList();

        extraClaims.put("id", user.getId());
        extraClaims.put("firstName", user.getFirstName());
        extraClaims.put("lastName", user.getLastName());
        extraClaims.put("username", user.getUsername());
        extraClaims.put("email", user.getEmail());
        extraClaims.put("roles", roles);
        extraClaims.put("permissions", permissions);

        return Jwts
                .builder()
                .setClaims(extraClaims)
                .setSubject(user.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 24)) // 24 minutos
                .signWith(getKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    private Key getKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String getUserNameFromToken(String token) {
        return getClaim(token, Claims::getSubject);
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = getUserNameFromToken(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
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
