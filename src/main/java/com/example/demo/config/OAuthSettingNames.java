package com.example.demo.config;

/**
 * 參數名
 * Ref:
 * https://github.com/spring-projects/spring-authorization-server/blob/main/oauth2-authorization-server/src/main/java/org/springframework/security/oauth2/server/authorization/config/ConfigurationSettingNames.java
 */
public final class OAuthSettingNames {

  public static final class Client {
    public static final String PROPERTYTYPE = "client";
    public static final String PROPERTYNAME_REQUIRE_PROOF_KEY = "require-proof-key";
    public static final String PROPERTYNAME_REQUIRE_AUTHORIZATION_CONSENT = "require-authorization-consent";

  }

  public static final class Token {
    public static final String PROPERTYTYPE = "token";
    public static final String PROPERTYNAME_ACCESS_TOKEN_TIME_TO_LIVE = "access-token-time-to-live";
    public static final String PROPERTYNAME_REUSE_REFRESH_TOKENS = "reuse-refresh-tokens";
    public static final String PROPERTYNAME_REFRESH_TOKEN_TIME_TO_LIVE = "refresh-token-time-to-live";
    public static final String PROPERTYNAME_ID_TOKEN_SIGNATURE_ALGORITHM = "id-token-signature-algorithm";
  }

}
