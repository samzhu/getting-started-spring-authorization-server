package com.example.demo.security;


import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.server.authorization.JwtEncodingContext;
import org.springframework.security.oauth2.server.authorization.OAuth2TokenCustomizer;

@Slf4j
public class CustomOAuth2Token implements OAuth2TokenCustomizer<JwtEncodingContext> {

  @Override
  public void customize(JwtEncodingContext context) {
    context.getClaims().id(UUID.randomUUID().toString()); // jti
    log.debug("<< Customizer.customize JwtEncodingContext={}", context);
  }
}

/**
 * 沒任何處理的 jwt { "sub": "admin", "aud": "democlient", "nbf": 1633279621, "scope": [ "openid" ],
 * "iss": "http://auth-server:8080", "exp": 1633279921, "iat": 1633279621 }
 */
