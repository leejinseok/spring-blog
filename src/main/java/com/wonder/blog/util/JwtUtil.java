package com.wonder.blog.util;

import com.wonder.blog.security.UserContext;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Component;

import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import java.security.Key;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.stream.Collectors;

@Component
public class JwtUtil {
  private static String secretKey = "thisisblogapp";

  public String generateToken(UserContext userContext) {
    SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

    Claims claims = Jwts.claims().setSubject(userContext.getEmail());
    claims.put("scopes", userContext.getAuthorities().stream().map(s -> s.toString()).collect(Collectors.toList()));
    LocalDateTime currentTime = LocalDateTime.now();
    LocalDateTime expireTime = currentTime.plusDays(1);

    byte[] secretByte = DatatypeConverter.parseBase64Binary(secretKey);
    Key signingKey = new SecretKeySpec(secretByte, signatureAlgorithm.getJcaName());


    String token = Jwts.builder()
      .setIssuer("blog")
      .setIssuedAt(Date.from(currentTime.atZone(ZoneId.systemDefault()).toInstant()))
      .setExpiration(Date.from(expireTime.atZone(ZoneId.systemDefault()).toInstant()))
      .signWith(signatureAlgorithm, signingKey)
      .compact();


    return token;
  }
}
