package com.example.demo.security;


import com.example.demo.entites.Oauth2AuthorizationEntity;
import com.example.demo.repositories.Oauth2AuthorizationEntityRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataRetrievalFailureException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.jackson2.SecurityJackson2Modules;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.OAuth2AuthorizationCode;
import org.springframework.security.oauth2.core.OAuth2RefreshToken;
import org.springframework.security.oauth2.core.OAuth2TokenType;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.server.authorization.OAuth2Authorization;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.jackson2.OAuth2AuthorizationServerJackson2Module;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

@Slf4j
public class CustomOAuth2AuthorizationService implements OAuth2AuthorizationService {
  private ObjectMapper objectMapper = new ObjectMapper();

  private Oauth2AuthorizationEntityRepository oauth2AuthorizationEntityRepository;
  private RegisteredClientRepository registeredClientRepository;

  public CustomOAuth2AuthorizationService(
      Oauth2AuthorizationEntityRepository oauth2AuthorizationEntityRepository,
      RegisteredClientRepository registeredClientRepository) {
    ClassLoader classLoader = CustomOAuth2AuthorizationService.class.getClassLoader();
    List<com.fasterxml.jackson.databind.Module> securityModules =
        SecurityJackson2Modules.getModules(classLoader);
    this.objectMapper.registerModules(securityModules);
    this.objectMapper.registerModule(new OAuth2AuthorizationServerJackson2Module());
    this.oauth2AuthorizationEntityRepository = oauth2AuthorizationEntityRepository;
    this.registeredClientRepository = registeredClientRepository;
  }

  /** The client will call storage when authorized */
  @Override
  public void save(OAuth2Authorization authorization) {
    log.debug(">> CustomOAuth2AuthorizationService.save authorization={}", authorization);

    Oauth2AuthorizationEntity oauth2AuthorizationEntity = new Oauth2AuthorizationEntity();
    oauth2AuthorizationEntity.setId(authorization.getId());
    oauth2AuthorizationEntity.setRegisteredClientId(authorization.getRegisteredClientId());
    oauth2AuthorizationEntity.setPrincipalName(authorization.getPrincipalName());
    oauth2AuthorizationEntity.setAuthorizationGrantType(
        authorization.getAuthorizationGrantType().getValue());

    authorization
        .getAttributes()
        .keySet()
        .forEach(
            key -> log.debug("key={}, object={}", key, authorization.getAttributes().get(key)));

    Map<String, Object> attributeMap = authorization.getAttributes();
    log.debug("attributeMap={}", attributeMap);
    log.debug(
        "get java.security.Principal={}",
        authorization.getAttributes().get("java.security.Principal"));
    // object=UsernamePasswordAuthenticationToken
    log.debug(
        "get org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest={}",
        authorization
            .getAttributes()
            .get("org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest"));
    // object=org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest
    log.debug(
        "get org.springframework.security.oauth2.server.authorization.OAuth2Authorization.AUTHORIZED_SCOPE={}",
        authorization
            .getAttributes()
            .get(
                "org.springframework.security.oauth2.server.authorization.OAuth2Authorization.AUTHORIZED_SCOPE"));
    // object=[openid]

    String attributes = writeMap(authorization.getAttributes());
    log.debug("attributes={}", attributes);

    oauth2AuthorizationEntity.setAttributes(attributes);

    String state = null;
    String authorizationState = authorization.getAttribute(OAuth2ParameterNames.STATE);
    if (StringUtils.hasText(authorizationState)) {
      state = authorizationState;
    }
    oauth2AuthorizationEntity.setState(state);
    //
    OAuth2Authorization.Token<OAuth2AuthorizationCode> authorizationCode =
        authorization.getToken(OAuth2AuthorizationCode.class);
    TokenValueObj tokenValueObj = toValue(authorizationCode);

    oauth2AuthorizationEntity.setAuthorizationCodeValue(tokenValueObj.getTokenValue());
    oauth2AuthorizationEntity.setAuthorizationCodeIssuedAt(tokenValueObj.getTokenIssuedAt());
    oauth2AuthorizationEntity.setAuthorizationCodeExpiresAt(tokenValueObj.getTokenExpiresAt());
    oauth2AuthorizationEntity.setAuthorizationCodeMetadata(tokenValueObj.getMetadata());
    //
    OAuth2Authorization.Token<OAuth2AccessToken> accessToken =
        authorization.getToken(OAuth2AccessToken.class);
    tokenValueObj = toValue(accessToken);
    oauth2AuthorizationEntity.setAccessTokenValue(tokenValueObj.getTokenValue());
    oauth2AuthorizationEntity.setAccessTokenIssuedAt(tokenValueObj.getTokenIssuedAt());
    oauth2AuthorizationEntity.setAccessTokenExpiresAt(tokenValueObj.getTokenExpiresAt());
    oauth2AuthorizationEntity.setAccessTokenMetadata(tokenValueObj.getMetadata());
    //
    String accessTokenType = null;
    String accessTokenScopes = null;
    if (accessToken != null) {
      accessTokenType = accessToken.getToken().getTokenType().getValue();
      if (!CollectionUtils.isEmpty(accessToken.getToken().getScopes())) {
        accessTokenScopes =
            StringUtils.collectionToDelimitedString(accessToken.getToken().getScopes(), ",");
      }
    }
    oauth2AuthorizationEntity.setAccessTokenType(accessTokenType);
    oauth2AuthorizationEntity.setAccessTokenScopes(accessTokenScopes);
    //
    OAuth2Authorization.Token<OidcIdToken> oidcIdToken = authorization.getToken(OidcIdToken.class);
    tokenValueObj = toValue(oidcIdToken);
    oauth2AuthorizationEntity.setOidcIdTokenValue(tokenValueObj.getTokenValue());
    oauth2AuthorizationEntity.setOidcIdTokenIssuedAt(tokenValueObj.getTokenIssuedAt());
    oauth2AuthorizationEntity.setAccessTokenExpiresAt(tokenValueObj.getTokenExpiresAt());
    oauth2AuthorizationEntity.setOidcIdTokenMetadata(tokenValueObj.getMetadata());
    //
    OAuth2Authorization.Token<OAuth2RefreshToken> refreshToken = authorization.getRefreshToken();
    tokenValueObj = toValue(refreshToken);
    oauth2AuthorizationEntity.setRefreshTokenValue(tokenValueObj.getTokenValue());
    oauth2AuthorizationEntity.setRefreshTokenIssuedAt(tokenValueObj.getTokenIssuedAt());
    oauth2AuthorizationEntity.setRefreshTokenExpiresAt(tokenValueObj.getTokenExpiresAt());
    oauth2AuthorizationEntity.setRefreshTokenMetadata(tokenValueObj.getMetadata());

    oauth2AuthorizationEntityRepository.save(oauth2AuthorizationEntity);
    log.debug("<< CustomOAuth2AuthorizationService.save");
  }

  @Override
  public void remove(OAuth2Authorization authorization) {
    Assert.notNull(authorization, "authorization cannot be null");
    log.debug(">> CustomOAuth2AuthorizationService.remove authorization={}", authorization);
    Optional<Oauth2AuthorizationEntity> oauth2AuthorizationEntityOptional =
        oauth2AuthorizationEntityRepository.findById(authorization.getId());
    if (oauth2AuthorizationEntityOptional.isPresent()) {
      oauth2AuthorizationEntityRepository.delete(oauth2AuthorizationEntityOptional.get());
    }
    log.debug("<< CustomOAuth2AuthorizationService.remove");
  }

  @Override
  public OAuth2Authorization findById(String id) {
    Assert.hasText(id, "id cannot be empty");
    log.debug(">> CustomOAuth2AuthorizationService.findById id={}", id);
    Optional<Oauth2AuthorizationEntity> oauth2AuthorizationEntityOptional =
        oauth2AuthorizationEntityRepository.findById(id);
    oauth2AuthorizationEntityOptional.orElseThrow(
        () -> new BadCredentialsException("Invalid token"));
    OAuth2Authorization oAuth2Authorization =
        this.convertEntityToModel(oauth2AuthorizationEntityOptional.get());
    log.debug(
        "<< CustomOAuth2AuthorizationService.findById oAuth2Authorization={}", oAuth2Authorization);
    return oAuth2Authorization;
  }

  /** I will run this section when I get the Token */
  @Override
  public OAuth2Authorization findByToken(String token, OAuth2TokenType tokenType) {
    Assert.hasText(token, "token cannot be empty");
    log.debug(
        ">> CustomOAuth2AuthorizationService.findByToken token={}, tokenType={}", token, tokenType);
    OAuth2Authorization oAuth2Authorization = null;
    Oauth2AuthorizationEntity oauth2AuthorizationEntity = null;
    if (tokenType == null) {
      oauth2AuthorizationEntity =
          oauth2AuthorizationEntityRepository
              .findByStateOrAuthorizationCodeValueOrAccessTokenValueOrRefreshTokenValue(
                  token, token, token, token);
    } else if (OAuth2ParameterNames.STATE.equals(tokenType.getValue())) {
      oauth2AuthorizationEntity = oauth2AuthorizationEntityRepository.findByState(token);
    } else if (OAuth2ParameterNames.CODE.equals(tokenType.getValue())) {
      oauth2AuthorizationEntity =
          oauth2AuthorizationEntityRepository.findByAuthorizationCodeValue(token);
    } else if (OAuth2TokenType.ACCESS_TOKEN.equals(tokenType)) {
      oauth2AuthorizationEntity = oauth2AuthorizationEntityRepository.findByAccessTokenValue(token);
    } else if (OAuth2TokenType.REFRESH_TOKEN.equals(tokenType)) {
      oauth2AuthorizationEntity =
          oauth2AuthorizationEntityRepository.findByRefreshTokenValue(token);
    }
    if (oauth2AuthorizationEntity != null) {
      oAuth2Authorization = this.convertEntityToModel(oauth2AuthorizationEntity);
    }
    log.debug(
        "<< CustomOAuth2AuthorizationService.findByToken oAuth2Authorization={}",
        oAuth2Authorization);
    return oAuth2Authorization;
  }

  private String writeMap(Map<String, Object> data) {
    try {
      return this.objectMapper.writeValueAsString(data);
    } catch (Exception ex) {
      throw new IllegalArgumentException(ex.getMessage(), ex);
    }
  }

  private TokenValueObj toValue(OAuth2Authorization.Token token) {
    byte[] tokenValue = null;
    LocalDateTime tokenIssuedAt = null;
    LocalDateTime tokenExpiresAt = null;
    String metadata = null;
    TokenValueObj tokenValueObj = new TokenValueObj();
    if (token != null) {
      tokenValue = token.getToken().getTokenValue().getBytes(StandardCharsets.UTF_8);
      tokenValueObj.setTokenValue(new String(tokenValue));
      if (token.getToken().getIssuedAt() != null) {
        tokenIssuedAt = LocalDateTime.ofInstant(token.getToken().getIssuedAt(), ZoneOffset.UTC);
        tokenValueObj.setTokenIssuedAt(tokenIssuedAt);
      }
      if (token.getToken().getExpiresAt() != null) {
        tokenExpiresAt = LocalDateTime.ofInstant(token.getToken().getExpiresAt(), ZoneOffset.UTC);
        tokenValueObj.setTokenExpiresAt(tokenExpiresAt);
      }
      metadata = writeMap(token.getMetadata());
      tokenValueObj.setMetadata(metadata);
    }
    return tokenValueObj;
  }

  @Data
  class TokenValueObj {
    private String tokenValue;
    private LocalDateTime tokenIssuedAt;
    private LocalDateTime tokenExpiresAt;
    private String metadata;
  }

  private OAuth2Authorization convertEntityToModel(
      Oauth2AuthorizationEntity oauth2AuthorizationEntity) {
    String registeredClientId = oauth2AuthorizationEntity.getRegisteredClientId();
    RegisteredClient registeredClient =
        this.registeredClientRepository.findById(registeredClientId);
    if (registeredClient == null) {
      throw new DataRetrievalFailureException(
          "The RegisteredClient with id '"
              + registeredClientId
              + "' was not found in the RegisteredClientRepository.");
    }
    OAuth2Authorization.Builder builder =
        OAuth2Authorization.withRegisteredClient(registeredClient);
    String id = oauth2AuthorizationEntity.getId();
    String principalName = oauth2AuthorizationEntity.getPrincipalName();
    String authorizationGrantType = oauth2AuthorizationEntity.getAuthorizationGrantType();
    // {"@class":"java.util.Collections$UnmodifiableMap","java.security.Principal":{"@class":"org.springframework.security.authentication.UsernamePasswordAuthenticationToken","authorities":["java.util.Collections$UnmodifiableRandomAccessList",[{"@class":"org.springframework.security.core.authority.SimpleGrantedAuthority","authority":"ADMIN"}]],"details":{"@class":"org.springframework.security.web.authentication.WebAuthenticationDetails","remoteAddress":"0:0:0:0:0:0:0:1","sessionId":"192605B8F2F684B77F25B96B55D48C05"},"authenticated":true,"principal":{"@class":"org.springframework.security.core.userdetails.User","password":null,"username":"admin","authorities":["java.util.Collections$UnmodifiableSet",[{"@class":"org.springframework.security.core.authority.SimpleGrantedAuthority","authority":"ADMIN"}]],"accountNonExpired":true,"accountNonLocked":true,"credentialsNonExpired":true,"enabled":true},"credentials":null},"org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest":{"@class":"org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest","authorizationUri":"http://localhost:8080/oauth2/authorize","authorizationGrantType":{"value":"authorization_code"},"responseType":{"value":"code"},"clientId":"democlient","redirectUri":"https://oidcdebugger.com/debug","scopes":["java.util.Collections$UnmodifiableSet",["openid"]],"state":null,"additionalParameters":{"@class":"java.util.Collections$UnmodifiableMap","nonce":"o2f5944hqu","response_mode":"form_post"},"authorizationRequestUri":"http://localhost:8080/oauth2/authorize?response_type=code&client_id=democlient&scope=openid&redirect_uri=https://oidcdebugger.com/debug&nonce=o2f5944hqu&response_mode=form_post","attributes":{"@class":"java.util.Collections$UnmodifiableMap"}},"org.springframework.security.oauth2.server.authorization.OAuth2Authorization.AUTHORIZED_SCOPE":["java.util.Collections$UnmodifiableSet",["openid"]]}
    Map<String, Object> attributes = parseMap(oauth2AuthorizationEntity.getAttributes());
    log.debug("attributes={}", attributes);
    // key=metadata.token.invalidated, object=false
    attributes.keySet().forEach(key -> log.debug("key={}, object={}", key, attributes.get(key)));

    builder
        .id(id)
        .principalName(principalName)
        .authorizationGrantType(new AuthorizationGrantType(authorizationGrantType))
        .attributes((attrs) -> attrs.putAll(attributes));

    String state = oauth2AuthorizationEntity.getState();
    if (StringUtils.hasText(state)) {
      builder.attribute(OAuth2ParameterNames.STATE, state);
    }

    String tokenValue;
    Instant tokenIssuedAt;
    Instant tokenExpiresAt;
    // 授權時只有這段
    if (oauth2AuthorizationEntity.getAuthorizationCodeValue() != null) {
      // PMsfvnv0B4ioXcDjRHcCV2jT1Xe2UTmmAf6oQsG3ubukcGvoJaQhNAOrNZ_jlK_1EvNLval-MVnHXIypOOGNXAlA-9qm8KyXMUcc-m458DJioJM61DYHOxaITUaAUCPC
      tokenValue = oauth2AuthorizationEntity.getAuthorizationCodeValue();
      tokenIssuedAt =
          oauth2AuthorizationEntity.getAuthorizationCodeIssuedAt().toInstant(ZoneOffset.UTC);
      tokenExpiresAt =
          oauth2AuthorizationEntity.getAuthorizationCodeExpiresAt().toInstant(ZoneOffset.UTC);
      // {"@class":"java.util.Collections$UnmodifiableMap","metadata.token.invalidated":false}
      Map<String, Object> authorizationCodeMetadata =
          parseMap(oauth2AuthorizationEntity.getAuthorizationCodeMetadata());
      log.debug("authorizationCodeMetadata={}", authorizationCodeMetadata);
      // key=metadata.token.invalidated, object=false
      authorizationCodeMetadata
          .keySet()
          .forEach(key -> log.debug("key={}, object={}", key, authorizationCodeMetadata.get(key)));

      OAuth2AuthorizationCode authorizationCode =
          new OAuth2AuthorizationCode(tokenValue, tokenIssuedAt, tokenExpiresAt);
      builder.token(authorizationCode, (metadata) -> metadata.putAll(authorizationCodeMetadata));
    }

    if (oauth2AuthorizationEntity.getAccessTokenValue() != null) {
      tokenValue = oauth2AuthorizationEntity.getAccessTokenValue();
      tokenIssuedAt = oauth2AuthorizationEntity.getAccessTokenIssuedAt().toInstant(ZoneOffset.UTC);
      tokenExpiresAt =
          oauth2AuthorizationEntity.getAccessTokenExpiresAt().toInstant(ZoneOffset.UTC);
      // {"@class":"java.util.Collections$UnmodifiableMap","metadata.token.claims":{"@class":"java.util.Collections$UnmodifiableMap","sub":"admin","aud":["java.util.Collections$SingletonList",["democlient"]],"nbf":["java.time.Instant",1634269643.115794000],"scope":["java.util.Collections$UnmodifiableSet",["openid"]],"iss":["java.net.URL","http://auth-server:8080"],"exp":["java.time.Instant",1634269943.115794000],"iat":["java.time.Instant",1634269643.115794000]},"metadata.token.invalidated":false}
      Map<String, Object> accessTokenMetadata =
          parseMap(oauth2AuthorizationEntity.getAccessTokenMetadata());
      log.debug("accessTokenMetadata={}", accessTokenMetadata);
      // key=metadata.token.invalidated, object=false
      accessTokenMetadata
          .keySet()
          .forEach(key -> log.debug("key={}, object={}", key, accessTokenMetadata.get(key)));

      OAuth2AccessToken.TokenType tokenType = null;
      if (OAuth2AccessToken.TokenType.BEARER
          .getValue()
          .equalsIgnoreCase(oauth2AuthorizationEntity.getAccessTokenType())) {
        tokenType = OAuth2AccessToken.TokenType.BEARER;
      }

      Set<String> scopes = Collections.emptySet();
      String accessTokenScopes = oauth2AuthorizationEntity.getAccessTokenScopes();
      if (accessTokenScopes != null) {
        scopes = StringUtils.commaDelimitedListToSet(accessTokenScopes);
      }
      OAuth2AccessToken accessToken =
          new OAuth2AccessToken(tokenType, tokenValue, tokenIssuedAt, tokenExpiresAt, scopes);
      builder.token(accessToken, (metadata) -> metadata.putAll(accessTokenMetadata));
    }

    if (oauth2AuthorizationEntity.getOidcIdTokenValue() != null) {
      tokenValue = oauth2AuthorizationEntity.getOidcIdTokenValue();
      tokenIssuedAt = oauth2AuthorizationEntity.getOidcIdTokenIssuedAt().toInstant(ZoneOffset.UTC);
      tokenExpiresAt =
          oauth2AuthorizationEntity.getOidcIdTokenExpiresAt().toInstant(ZoneOffset.UTC);
      // {"@class":"java.util.Collections$UnmodifiableMap","metadata.token.claims":{"@class":"java.util.Collections$UnmodifiableMap","sub":"admin","aud":["java.util.Collections$SingletonList",["democlient"]],"azp":"democlient","iss":["java.net.URL","http://auth-server:8080"],"exp":["java.time.Instant",1634271443.169382000],"iat":["java.time.Instant",1634269643.169382000],"nonce":"o2f5944hqu"},"metadata.token.invalidated":false}
      Map<String, Object> oidcTokenMetadata =
          parseMap(oauth2AuthorizationEntity.getOidcIdTokenMetadata());
      log.debug("oidcTokenMetadata={}", oidcTokenMetadata);
      // key=metadata.token.invalidated, object=false
      oidcTokenMetadata
          .keySet()
          .forEach(key -> log.debug("key={}, object={}", key, oidcTokenMetadata.get(key)));
      //
      OidcIdToken oidcToken =
          new OidcIdToken(
              tokenValue,
              tokenIssuedAt,
              tokenExpiresAt,
              (Map<String, Object>)
                  oidcTokenMetadata.get(OAuth2Authorization.Token.CLAIMS_METADATA_NAME));
      builder.token(oidcToken, (metadata) -> metadata.putAll(oidcTokenMetadata));
    }

    if (oauth2AuthorizationEntity.getRefreshTokenValue() != null) {
      tokenValue = oauth2AuthorizationEntity.getRefreshTokenValue();
      tokenIssuedAt = oauth2AuthorizationEntity.getRefreshTokenIssuedAt().toInstant(ZoneOffset.UTC);
      tokenExpiresAt = null;
      if (oauth2AuthorizationEntity.getRefreshTokenExpiresAt() != null) {
        tokenExpiresAt =
            oauth2AuthorizationEntity.getRefreshTokenExpiresAt().toInstant(ZoneOffset.UTC);
      }
      // {"@class":"java.util.Collections$UnmodifiableMap","metadata.token.invalidated":false}
      Map<String, Object> refreshTokenMetadata =
          parseMap(oauth2AuthorizationEntity.getRefreshTokenMetadata());

      OAuth2RefreshToken refreshToken =
          new OAuth2RefreshToken(tokenValue, tokenIssuedAt, tokenExpiresAt);
      builder.token(refreshToken, (metadata) -> metadata.putAll(refreshTokenMetadata));
    }
    return builder.build();
  }

  private Map<String, Object> parseMap(String data) {
    try {
      return this.objectMapper.readValue(data, new TypeReference<Map<String, Object>>() {});
    } catch (Exception ex) {
      throw new IllegalArgumentException(ex.getMessage(), ex);
    }
  }
}
