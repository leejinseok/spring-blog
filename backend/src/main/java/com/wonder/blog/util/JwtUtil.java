package com.wonder.blog.util;

import com.wonder.blog.security.UserContext;
import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.context.annotation.Bean;
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
  private final SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
  private static final String secretKey = "thisisblogapp";
  public static final String JWT_TOKEN_NAME = "JWT-TOKEN";


  public String generateToken(UserContext userContext) {
    Claims claims = Jwts.claims().setSubject(userContext.getEmail());
    claims.put("scopes", userContext.getAuthorities().stream().map(s -> s.toString()).collect(Collectors.toList()));
    LocalDateTime currentTime = LocalDateTime.now();
    LocalDateTime expireTime = currentTime.plusDays(1);
//    LocalDateTime expireTime = currentTime.plusSeconds(1);

    return Jwts.builder()
      .setClaims(claims)
      .setIssuer("blog")
      .setIssuedAt(Date.from(currentTime.atZone(ZoneId.systemDefault()).toInstant()))
      .setExpiration(Date.from(expireTime.atZone(ZoneId.systemDefault()).toInstant()))
      .signWith(signatureAlgorithm, generateSigingKey())
      .compact();
  }

  public UserContext decodeToken(String token) {
    Jws<Claims> claims = Jwts.parser()
      .setSigningKey(generateSigingKey())
      .parseClaimsJws(token);

    String subject = claims.getBody().getSubject();
    List<String> scopes = claims.getBody().get("scopes", List.class);
    List<GrantedAuthority> authorities = scopes.stream()
      .map(SimpleGrantedAuthority::new)
      .collect(Collectors.toList());

    return UserContext.create(subject, authorities);
  }

  private Key generateSigingKey() {
    byte[] secretByte = DatatypeConverter.parseBase64Binary(secretKey);
    return new SecretKeySpec(secretByte, signatureAlgorithm.getJcaName());
  }
}
