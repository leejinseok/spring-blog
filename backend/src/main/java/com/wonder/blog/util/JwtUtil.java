package com.wonder.blog.util;

import com.wonder.blog.security.UserContext;
import io.jsonwebtoken.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
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
  private final String issuer = "blog";
  private final int plusDays = 1;

  public String generateToken(UserContext userContext) {
    Claims claims = Jwts.claims().setSubject(userContext.getEmail());
    claims.put("scopes", userContext.getAuthorities().stream().map(Object::toString).collect(Collectors.toList()));
    LocalDateTime currentTime = LocalDateTime.now();
    LocalDateTime expireTime = currentTime.plusDays(plusDays);

    return Jwts.builder()
      .setClaims(claims)
      .setIssuer(issuer)
      .setIssuedAt(localDateTimeToDate(currentTime))
      .setExpiration(localDateTimeToDate(expireTime))
      .signWith(signatureAlgorithm, generateSigingKey())
      .compact();
  }

  public UserContext decodeToken(String token) {
    Jws<Claims> claims = generateClaims(token);
    List<String> scopes = claims.getBody().get("scopes", List.class);
    List<GrantedAuthority> authorities = scopes.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList());
    return UserContext.create(claims.getBody().getSubject(), authorities);
  }

  private Key generateSigingKey() {
    byte[] secretByte = DatatypeConverter.parseBase64Binary(secretKey);
    return new SecretKeySpec(secretByte, signatureAlgorithm.getJcaName());
  }

  private Jws<Claims> generateClaims(String token) {
    return Jwts.parser()
      .setSigningKey(generateSigingKey())
      .parseClaimsJws(token);
  }

  private Date localDateTimeToDate(LocalDateTime time) {
    return Date.from(time.atZone(ZoneId.systemDefault()).toInstant());
  }
}
