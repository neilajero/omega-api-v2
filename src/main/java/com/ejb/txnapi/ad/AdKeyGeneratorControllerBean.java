package com.ejb.txnapi.ad;

import com.ejb.ConfigurationClass;
import com.util.EJBContextClass;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

import jakarta.ejb.Stateless;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Stateless(name = "AdKeyGeneratorControllerEJB")
public class AdKeyGeneratorControllerBean extends EJBContextClass implements AdKeyGeneratorController {

    private static final String SECRET_KEY = ConfigurationClass.SECRETKEY;
    private static final long ACCESS_TOKEN_EXPIRATION_TIME = 1800000; // 30 minutes
    private static final long REFRESH_TOKEN_EXPIRATION_TIME = 604800000; // 7 days

    byte[] secretKeyBytes = SECRET_KEY.getBytes(StandardCharsets.UTF_8);
    Key key = Keys.hmacShaKeyFor(secretKeyBytes);

    @Override
    public String generateKey(String username) {
        long nowMillis = System.currentTimeMillis();
        Date now = new Date(nowMillis);
        String token = Jwts.builder()
                .setSubject(username)
                .setIssuedAt(now)
                .setIssuer("omegabci")
                .setExpiration(toDate(LocalDateTime.now().plusMinutes(15L)))
                .signWith(key)
                .compact();
        return token;
    }

    @Override
    public Map<String, String> generateTokens(String username) {
        Date now = new Date();
        Date accessTokenExpirationTime = new Date(now.getTime() + ACCESS_TOKEN_EXPIRATION_TIME);
        Date refreshTokenExpirationTime = new Date(now.getTime() + REFRESH_TOKEN_EXPIRATION_TIME);

        String jwt = Jwts.builder()
                .setSubject(username)
                .setIssuedAt(now)
                .setExpiration(accessTokenExpirationTime)
                .signWith(key)
                .compact();

        String refreshToken = Jwts.builder()
                .setSubject(username)
                .setIssuedAt(now)
                .setExpiration(refreshTokenExpirationTime)
                .signWith(key)
                .compact();

        Map<String, String> tokens = new HashMap<>();
        tokens.put("jwt", jwt);
        tokens.put("refreshToken", refreshToken);

        return tokens;
    }

    @Override
    public String refreshToken(String refreshToken) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(refreshToken)
                .getBody();
        String username = claims.getSubject();
        return generateTokens(username).get("jwt");
    }

    private Date toDate(LocalDateTime localDateTime) {

        return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
    }

}