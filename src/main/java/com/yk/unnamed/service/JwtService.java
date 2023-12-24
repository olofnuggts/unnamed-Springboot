package com.yk.unnamed.service;

import com.yk.unnamed.model.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtService {
    private static final String SECRETKEY = "JrpMOzObn9FWkhZFqngB7qn1NshwGAwx";
    public  String extractUsername(String token) {
        return extractClaims(token,Claims::getSubject);
    }

    public <T> T extractClaims(String token, Function <Claims,T> claimsResolver){
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    public  String gernateToken (Map<String, Object> extractCalims, User user){
        return Jwts.builder().claims(extractCalims).subject(user.getUsername())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis()+ 1000*60*24)).signWith(getPublicSigningKey(), SignatureAlgorithm.HS256)
                .compact();

    }
    public boolean valid(String token, User user){
    final String username = extractUsername(token);
    return (username.equals(user.getUsername())) && !isTokenExpired(token);
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaims(token, Claims::getExpiration);
    }

    public String genrateToken (User user){
        return  gernateToken(new HashMap<>(),user);
    }
    private Claims extractAllClaims(String token){
        return Jwts.parser().verifyWith(getPublicSigningKey()).build().parseSignedClaims(token).getPayload();
    }

    private SecretKey getPublicSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRETKEY);
        return Keys.hmacShaKeyFor(keyBytes);


    }
}
