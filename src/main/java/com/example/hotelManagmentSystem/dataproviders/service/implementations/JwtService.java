package com.example.hotelManagmentSystem.dataproviders.service.implementations;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.*;
import java.util.function.Function;

@Service
public class JwtService {

    private static final String SECRET_KEY = "4BEBE5AC983B166E60E4F5223F3A07109777FEB885B6E71B123C6A4164BF147FAB55499EBA12420F4D0AA39287D82CDBB14E4A5194EBA0E7357938225FCE13C04FF56394C5D87E351B7C2402A7381EB105A1DF3EA0C93F13DC8369FD766E7A0D49D9CAEE40956370E3248AAC4B1F99FEBF9E5233F00BC12568B3F19269988191";

    public String extractUsername(String jwtToken) {
        return extractClaim(jwtToken,Claims::getSubject);
    }

    private <T> T extractClaim(String token, Function<Claims, T> claimsResolver){
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token){
        return Jwts.parserBuilder()
                .setSigningKey(getSignInKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private Key getSignInKey(){
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }

//    public String generateToken(UserDetails userDetails){
//        return generateToken(new ArrayList<String>(),userDetails);
//    }

    public String generateToken(UserDetails userDetails,List<String> roles){
        return Jwts.builder()
                .setSubject(userDetails.getUsername())
                .claim("role",roles)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 3600000*2))
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public boolean isTokenValid(String token, UserDetails userDetails){
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername())) && !isTokenExpired(token);
    }

    public boolean isTokenExpired(String token){
        return extractExpiration(token).before(new Date());
    }

    public Date extractExpiration(String token){
        return extractClaim(token,Claims::getExpiration);
    }

}
