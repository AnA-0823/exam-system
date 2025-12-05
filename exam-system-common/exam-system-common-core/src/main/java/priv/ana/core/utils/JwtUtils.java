package priv.ana.core.utils;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey;
import java.util.Map;

public class JwtUtils {
    private static final String SECRET = "d8e8fca2dc0f896fd7cb4cb0031ba249";
    private static final SecretKey secretKey = Keys.hmacShaKeyFor(SECRET.getBytes());

    public static String build(Map<String, String> payload) {
        JwtBuilder builder = Jwts.builder()
                .claims(payload)
                .signWith(secretKey);
        return builder.compact();
    }

    public static Jws<Claims> verify(String token) {
        JwtParser parser = Jwts.parser()
                .verifyWith(secretKey)
                .build();
        return parser.parseSignedClaims(token);
    }
}
