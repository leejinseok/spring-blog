package com.wonder.blog.util;

import com.wonder.blog.security.UserContext;
import io.jsonwebtoken.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import java.io.UnsupportedEncodingException;
import java.security.Key;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class JwtUtil {
  private static String secretKey = "thisisblogapp";

  public String generateToken(UserContext userContext) {
    SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

    Claims claims = Jwts.claims().setSubject(userContext.getEmail());
    claims.put("scopes", userContext.getAuthorities().stream().map(s -> s.toString()).collect(Collectors.toList()));
    LocalDateTime currentTime = LocalDateTime.now();
//    LocalDateTime expireTime = currentTime.plusDays(1);
    LocalDateTime expireTime = currentTime.plusSeconds(1);

    byte[] secretByte = DatatypeConverter.parseBase64Binary(secretKey);
    Key signingKey = new SecretKeySpec(secretByte, signatureAlgorithm.getJcaName());

    return Jwts.builder()
      .setClaims(claims)
      .setIssuer("blog")
      .setIssuedAt(Date.from(currentTime.atZone(ZoneId.systemDefault()).toInstant()))
      .setExpiration(Date.from(expireTime.atZone(ZoneId.systemDefault()).toInstant()))
      .signWith(signatureAlgorithm, signingKey)
      .compact();
  }

  public UserContext decodeToken(String token) throws ExpiredJwtException , Exception {
    SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
    byte[] secretByte = DatatypeConverter.parseBase64Binary(secretKey);
    Key signingKey = new SecretKeySpec(secretByte, signatureAlgorithm.getJcaName());

    Jws<Claims> claims = Jwts.parser()
      .setSigningKey(signingKey)
      .parseClaimsJws(token);

    String subject = claims.getBody().getSubject();
    List<String> scopes = claims.getBody().get("scopes", List.class);
    List<GrantedAuthority> authorities = scopes.stream()
      .map(SimpleGrantedAuthority::new)
      .collect(Collectors.toList());

    return UserContext.create(subject, authorities);
  }
}
