package com.example.demo.config;

/** 參數名 */
public final class OAuthSettingNames {

  public static final class Client {
    public static final String TYPE = "client";

    public static final class Property {
      public static final String REQUIRE_PROOF_KEY = "require-proof-key";
      public static final String REQUIRE_AUTHORIZATION_CONSENT = "require-authorization-consent";
    }
  }

  public static final class Token {
    public static final String TYPE = "token";

    public static final class Property {
      public static final String ACCESS_TOKEN_TIME_TO_LIVE = "access-token-time-to-live";
      public static final String REUSE_REFRESH_TOKENS = "reuse-refresh-tokens";
      public static final String PREFRESH_TOKEN_TIME_TO_LIVE = "refresh-token-time-to-live";
      public static final String ID_TOKEN_SIGNATURE_ALGORITHM = "id-token-signature-algorithm";
    }
  }
}
