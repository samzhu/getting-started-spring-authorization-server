package com.example.demo.security;

import com.example.demo.config.OAuthSettingNames;
import com.example.demo.entites.Oauth2ClientAuthenticationMethodEntity;
import com.example.demo.entites.Oauth2ClientEntity;
import com.example.demo.entites.Oauth2ClientGrantTypeEntity;
import com.example.demo.entites.Oauth2ClientRedirectUriEntity;
import com.example.demo.entites.Oauth2ClientScopeEntity;
import com.example.demo.entites.Oauth2ClientSettingEntity;
import com.example.demo.infrastructure.repositories.ScopeRepository;
import com.example.demo.infrastructure.repositories.tables.pojos.Scope;
import com.example.demo.repositories.Oauth2ClientAuthenticationMethodEntityRepository;
import com.example.demo.repositories.Oauth2ClientEntityRepository;
import com.example.demo.repositories.Oauth2ClientGrantTypeEntityRepository;
import com.example.demo.repositories.Oauth2ClientRedirectUriEntityRepository;
import com.example.demo.repositories.Oauth2ClientScopeEntityRepository;
import com.example.demo.repositories.Oauth2ClientSettingEntityRepository;
import java.time.Duration;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.jose.jws.SignatureAlgorithm;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.config.ClientSettings;
import org.springframework.security.oauth2.server.authorization.config.TokenSettings;

@Slf4j
@NoArgsConstructor
@AllArgsConstructor
public class CustomRegisteredClientRepository implements RegisteredClientRepository {

  private Oauth2ClientEntityRepository oauth2ClientEntityRepository;
  private Oauth2ClientAuthenticationMethodEntityRepository
      oauth2ClientAuthenticationMethodEntityRepository;
  private Oauth2ClientGrantTypeEntityRepository oauth2ClientGrantTypeEntityRepository;
  private Oauth2ClientRedirectUriEntityRepository oauth2ClientRedirectUriEntityRepository;
  private Oauth2ClientScopeEntityRepository oauth2ClientScopeEntityRepository;
  private Oauth2ClientSettingEntityRepository oauth2ClientSettingEntityRepository;
  private ScopeRepository scopeRepository;

  @Override
  public void save(RegisteredClient registeredClient) {
    log.debug(">> CustomRegisteredClientRepository.save registeredClient={}", registeredClient);
    // TODO need implement
    log.debug("<< CustomRegisteredClientRepository.save ");
  }

  /** Take this part of the Token process */
  @Override
  public RegisteredClient findById(String id) {
    log.debug(">> CustomRegisteredClientRepository.findById id={}", id);
    Optional<Oauth2ClientEntity> oauth2ClientOptional = oauth2ClientEntityRepository.findById(id);
    RegisteredClient registeredClient = this.getRegisteredClient(oauth2ClientOptional);
    log.debug("<< CustomRegisteredClientRepository.findById registeredClient={}", registeredClient);
    return registeredClient;
  }

  /** The authorization process will go through this */
  @Override
  public RegisteredClient findByClientId(String clientId) {
    log.debug(">> CustomRegisteredClientRepository.findByClientId id={}", clientId);
    Optional<Oauth2ClientEntity> oauth2ClientOptional =
        oauth2ClientEntityRepository.findByClientId(clientId);
    RegisteredClient registeredClient = this.getRegisteredClient(oauth2ClientOptional);
    log.debug(
        "<< CustomRegisteredClientRepository.findByClientId registeredClient={}", registeredClient);
    return registeredClient;
  }

  private RegisteredClient getRegisteredClient(Optional<Oauth2ClientEntity> oauth2ClientOptional) {
    oauth2ClientOptional.orElseThrow(() -> new BadCredentialsException("Invalid client"));
    Oauth2ClientEntity oauth2Client = oauth2ClientOptional.get();
    // This is the association ID of the table
    String oauth2ClientId = oauth2Client.getId();
    // Client account
    String clientId = oauth2Client.getClientId();
    // 取出有幾種驗證方式 ex: client_secret_post
    // 可參考 https://github.com/spring-projects/spring-security/blob/main/oauth2/oauth2-core/src/main/java/org/springframework/security/oauth2/core/ClientAuthenticationMethod.java
    List<Oauth2ClientAuthenticationMethodEntity> oauth2ClientAuthenticationMethods =
        oauth2ClientAuthenticationMethodEntityRepository.findByOauth2ClientId(oauth2Client.getId());
    List<Oauth2ClientGrantTypeEntity> oauth2ClientGrantTypes =
        oauth2ClientGrantTypeEntityRepository.findByOauth2ClientId(oauth2Client.getId());
    List<Oauth2ClientRedirectUriEntity> oauth2ClientRedirectUris =
        oauth2ClientRedirectUriEntityRepository.findByOauth2ClientId(oauth2Client.getId());
    List<String> redirectUris =
        oauth2ClientRedirectUris.stream()
            .map(Oauth2ClientRedirectUriEntity::getRedirectUri)
            .collect(Collectors.toList());
    List<Oauth2ClientScopeEntity> oauth2ClientScopes =
        oauth2ClientScopeEntityRepository.findByOauth2ClientId(oauth2Client.getId());
    List<String> authorityIds =
        oauth2ClientScopes.stream()
            .map(Oauth2ClientScopeEntity::getResourceScopeId)
            .collect(Collectors.toList());
    List<String> clientScopes =
        scopeRepository.findByIdIn(authorityIds).stream()
            .map(Scope::getScope)
            .collect(Collectors.toList());
    // @formatter:off
    RegisteredClient.Builder builder =
        RegisteredClient.withId(oauth2Client.getId())
            .clientId(clientId)
            .clientIdIssuedAt(
                oauth2Client.getClientIdIssuedAt() != null
                    ? oauth2Client.getClientIdIssuedAt().toInstant(ZoneOffset.UTC)
                    : null)
            .clientSecret(oauth2Client.getClientSecret())
            .clientSecretExpiresAt(
                oauth2Client.getClientSecretExpiresAt() != null
                    ? oauth2Client.getClientSecretExpiresAt().toInstant(ZoneOffset.UTC)
                    : null)
            .clientName(oauth2Client.getClientName())
            .clientAuthenticationMethods(
                (authenticationMethods) ->
                    oauth2ClientAuthenticationMethods.forEach(
                        authenticationMethod ->
                            authenticationMethods.add(
                                resolveClientAuthenticationMethod(
                                    authenticationMethod.getAuthenticationMethod()))))
            .authorizationGrantTypes(
                (grantTypes) ->
                    oauth2ClientGrantTypes.forEach(
                        grantType ->
                            grantTypes.add(
                                resolveAuthorizationGrantType(grantType.getGrantType()))))
            .redirectUris((uris) -> uris.addAll(redirectUris))
            .scopes((scopes) -> scopes.addAll(clientScopes));
    // @formatter:on
    Oauth2ClientSettingEntity requireProofKey =
        this.findClientSetting(
            oauth2ClientId,
            OAuthSettingNames.Client.PROPERTYTYPE,
            OAuthSettingNames.Client.PROPERTYNAME_REQUIRE_PROOF_KEY);
    //
    Oauth2ClientSettingEntity requireAuthorizationConsent =
        this.findClientSetting(
            oauth2ClientId,
            OAuthSettingNames.Client.PROPERTYTYPE,
            OAuthSettingNames.Client.PROPERTYNAME_REQUIRE_AUTHORIZATION_CONSENT);

    //
    ClientSettings clientSettings =
        ClientSettings.builder()
            .requireProofKey(Boolean.valueOf(requireProofKey.getPropertyValue()))
            .requireAuthorizationConsent(
                Boolean.valueOf(requireAuthorizationConsent.getPropertyValue()))
            .build();
    log.debug("clientSettings={}", clientSettings);
    builder.clientSettings(clientSettings);
    //
    Oauth2ClientSettingEntity idTokenSignatureAlgorithm =
        this.findClientSetting(
            oauth2ClientId,
            OAuthSettingNames.Token.PROPERTYTYPE,
            OAuthSettingNames.Token.PROPERTYNAME_ID_TOKEN_SIGNATURE_ALGORITHM);
    Oauth2ClientSettingEntity accessTokenTimeToLive =
        this.findClientSetting(
            oauth2ClientId,
            OAuthSettingNames.Token.PROPERTYTYPE,
            OAuthSettingNames.Token.PROPERTYNAME_ACCESS_TOKEN_TIME_TO_LIVE);
    Oauth2ClientSettingEntity refreshTokenTimeToLive =
        this.findClientSetting(
            oauth2ClientId,
            OAuthSettingNames.Token.PROPERTYTYPE,
            OAuthSettingNames.Token.PROPERTYNAME_REFRESH_TOKEN_TIME_TO_LIVE);
    Oauth2ClientSettingEntity reuseRefreshTokens =
        this.findClientSetting(
            oauth2ClientId,
            OAuthSettingNames.Token.PROPERTYTYPE,
            OAuthSettingNames.Token.PROPERTYNAME_REUSE_REFRESH_TOKENS);
    //

    TokenSettings tokenSettings =
        TokenSettings.builder()
            .accessTokenTimeToLive(
                Duration.ofSeconds(Integer.valueOf(accessTokenTimeToLive.getPropertyValue())))
            .reuseRefreshTokens(Boolean.valueOf(reuseRefreshTokens.getPropertyValue()))
            .refreshTokenTimeToLive(
                Duration.ofSeconds(Integer.valueOf(refreshTokenTimeToLive.getPropertyValue())))
            .idTokenSignatureAlgorithm(
                SignatureAlgorithm.from(idTokenSignatureAlgorithm.getPropertyValue()))
            .build();
    log.debug("tokenSettings={}", tokenSettings);
    builder.tokenSettings(tokenSettings);

    RegisteredClient registeredClient = builder.build();
    return registeredClient;
  }

  private Oauth2ClientSettingEntity findClientSetting(
      String oauth2ClientId, String propertyType, String propertyName) {
    Optional<Oauth2ClientSettingEntity> oauth2ClientSettingOptional =
        oauth2ClientSettingEntityRepository.findByOauth2ClientIdAndPropertyTypeAndPropertyName(
            oauth2ClientId, propertyType, propertyName);
    oauth2ClientSettingOptional.orElseThrow(
        () ->
            new BadCredentialsException(
                String.format(
                    "Invalid client setting %s %s %s",
                    oauth2ClientId, propertyType, propertyName)));
    return oauth2ClientSettingOptional.get();
  }

  private static ClientAuthenticationMethod resolveClientAuthenticationMethod(
      String clientAuthenticationMethod) {
    if (ClientAuthenticationMethod.CLIENT_SECRET_BASIC
        .getValue()
        .equals(clientAuthenticationMethod)) {
      return ClientAuthenticationMethod.CLIENT_SECRET_BASIC;
    } else if (ClientAuthenticationMethod.CLIENT_SECRET_POST
        .getValue()
        .equals(clientAuthenticationMethod)) {
      return ClientAuthenticationMethod.CLIENT_SECRET_POST;
    } else if (ClientAuthenticationMethod.NONE.getValue().equals(clientAuthenticationMethod)) {
      return ClientAuthenticationMethod.NONE;
    }
    return new ClientAuthenticationMethod(
        clientAuthenticationMethod); // Custom client authentication method
  }

  private static AuthorizationGrantType resolveAuthorizationGrantType(
      String authorizationGrantType) {
    if (AuthorizationGrantType.AUTHORIZATION_CODE.getValue().equals(authorizationGrantType)) {
      return AuthorizationGrantType.AUTHORIZATION_CODE;
    } else if (AuthorizationGrantType.CLIENT_CREDENTIALS
        .getValue()
        .equals(authorizationGrantType)) {
      return AuthorizationGrantType.CLIENT_CREDENTIALS;
    } else if (AuthorizationGrantType.REFRESH_TOKEN.getValue().equals(authorizationGrantType)) {
      return AuthorizationGrantType.REFRESH_TOKEN;
    }
    return new AuthorizationGrantType(authorizationGrantType); // Custom authorization grant type
  }
}
