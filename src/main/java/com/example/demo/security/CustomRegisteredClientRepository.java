package com.example.demo.security;

import java.time.Duration;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.jose.jws.SignatureAlgorithm;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.settings.ClientSettings;
import org.springframework.security.oauth2.server.authorization.settings.TokenSettings;

import com.example.demo.config.OAuthSettingNames;
import com.example.demo.infrastructure.repositories.Oauth2ClientAuthenticationMethodRepository;
import com.example.demo.infrastructure.repositories.Oauth2ClientGrantTypeRepository;
import com.example.demo.infrastructure.repositories.Oauth2ClientRedirectUriRepository;
import com.example.demo.infrastructure.repositories.Oauth2ClientRepository;
import com.example.demo.infrastructure.repositories.Oauth2ClientScopeRepository;
import com.example.demo.infrastructure.repositories.Oauth2ClientSettingRepository;
import com.example.demo.infrastructure.repositories.ScopeRepository;
import com.example.demo.infrastructure.repositories.tables.pojos.Oauth2Client;
import com.example.demo.infrastructure.repositories.tables.pojos.Oauth2ClientAuthenticationMethod;
import com.example.demo.infrastructure.repositories.tables.pojos.Oauth2ClientGrantType;
import com.example.demo.infrastructure.repositories.tables.pojos.Oauth2ClientRedirectUri;
import com.example.demo.infrastructure.repositories.tables.pojos.Oauth2ClientScope;
import com.example.demo.infrastructure.repositories.tables.pojos.Oauth2ClientSetting;
import com.example.demo.infrastructure.repositories.tables.pojos.Scope;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@NoArgsConstructor
@AllArgsConstructor
public class CustomRegisteredClientRepository implements RegisteredClientRepository {

        private Oauth2ClientRepository oauth2ClientRepository;
        private Oauth2ClientAuthenticationMethodRepository oauth2ClientAuthenticationMethodRepository;
        private Oauth2ClientGrantTypeRepository oauth2ClientGrantTypeRepository;
        private Oauth2ClientRedirectUriRepository oauth2ClientRedirectUriRepository;
        private Oauth2ClientScopeRepository oauth2ClientScopeEntityRepository;
        private Oauth2ClientSettingRepository oauth2ClientSettingRepository;
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
                Optional<Oauth2Client> oauth2ClientOptional = oauth2ClientRepository.findById(id);
                RegisteredClient registeredClient = this.getRegisteredClient(oauth2ClientOptional);
                log.debug("<< CustomRegisteredClientRepository.findById registeredClient={}", registeredClient);
                return registeredClient;
        }

        /** The authorization process will go through this */
        @Override
        public RegisteredClient findByClientId(String clientId) {
                log.debug(">> CustomRegisteredClientRepository.findByClientId id={}", clientId);
                Optional<Oauth2Client> oauth2ClientOptional = oauth2ClientRepository.findByClientId(clientId);
                RegisteredClient registeredClient = this.getRegisteredClient(oauth2ClientOptional);
                log.debug(
                                "<< CustomRegisteredClientRepository.findByClientId registeredClient={}",
                                registeredClient);
                return registeredClient;
        }

        private RegisteredClient getRegisteredClient(Optional<Oauth2Client> oauth2ClientOptional) {
                oauth2ClientOptional.orElseThrow(() -> new BadCredentialsException("Invalid client"));
                Oauth2Client oauth2Client = oauth2ClientOptional.get();
                // This is the association ID of the table
                String oauth2ClientId = oauth2Client.getId();
                // Client account
                String clientId = oauth2Client.getClientId();
                // 取出有幾種驗證方式 ex: client_secret_post
                // 可參考
                // https://github.com/spring-projects/spring-security/blob/main/oauth2/oauth2-core/src/main/java/org/springframework/security/oauth2/core/ClientAuthenticationMethod.java
                List<Oauth2ClientAuthenticationMethod> oauth2ClientAuthenticationMethods = oauth2ClientAuthenticationMethodRepository
                                .findByOauth2ClientId(oauth2Client.getId());
                List<Oauth2ClientGrantType> oauth2ClientGrantTypes = oauth2ClientGrantTypeRepository
                                .findByOauth2ClientId(oauth2Client.getId());
                List<Oauth2ClientRedirectUri> oauth2ClientRedirectUris = oauth2ClientRedirectUriRepository
                                .findByOauth2ClientId(oauth2Client.getId());
                List<String> redirectUris = oauth2ClientRedirectUris.stream()
                                .map(Oauth2ClientRedirectUri::getRedirectUri)
                                .collect(Collectors.toList());
                List<Oauth2ClientScope> oauth2ClientScopes = oauth2ClientScopeEntityRepository
                                .findByOauth2ClientId(oauth2Client.getId());
                List<String> authorityIds = oauth2ClientScopes.stream()
                                .map(Oauth2ClientScope::getScopeId)
                                .collect(Collectors.toList());
                List<String> clientScopes = scopeRepository.findByIdIn(authorityIds).stream()
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
                Oauth2ClientSetting requireProofKey = this.findClientSetting(
                                oauth2ClientId,
                                OAuthSettingNames.Client.PROPERTYTYPE,
                                OAuthSettingNames.Client.PROPERTYNAME_REQUIRE_PROOF_KEY);
                //
                Oauth2ClientSetting requireAuthorizationConsent = this.findClientSetting(
                                oauth2ClientId,
                                OAuthSettingNames.Client.PROPERTYTYPE,
                                OAuthSettingNames.Client.PROPERTYNAME_REQUIRE_AUTHORIZATION_CONSENT);

                //
                ClientSettings clientSettings = ClientSettings.builder()
                                .requireProofKey(Boolean.valueOf(requireProofKey.getPropertyValue()))
                                .requireAuthorizationConsent(
                                                Boolean.valueOf(requireAuthorizationConsent.getPropertyValue()))
                                .build();
                log.debug("clientSettings={}", clientSettings);
                builder.clientSettings(clientSettings);
                //
                Oauth2ClientSetting idTokenSignatureAlgorithm = this.findClientSetting(
                                oauth2ClientId,
                                OAuthSettingNames.Token.PROPERTYTYPE,
                                OAuthSettingNames.Token.PROPERTYNAME_ID_TOKEN_SIGNATURE_ALGORITHM);
                Oauth2ClientSetting accessTokenTimeToLive = this.findClientSetting(
                                oauth2ClientId,
                                OAuthSettingNames.Token.PROPERTYTYPE,
                                OAuthSettingNames.Token.PROPERTYNAME_ACCESS_TOKEN_TIME_TO_LIVE);
                Oauth2ClientSetting refreshTokenTimeToLive = this.findClientSetting(
                                oauth2ClientId,
                                OAuthSettingNames.Token.PROPERTYTYPE,
                                OAuthSettingNames.Token.PROPERTYNAME_REFRESH_TOKEN_TIME_TO_LIVE);
                Oauth2ClientSetting reuseRefreshTokens = this.findClientSetting(
                                oauth2ClientId,
                                OAuthSettingNames.Token.PROPERTYTYPE,
                                OAuthSettingNames.Token.PROPERTYNAME_REUSE_REFRESH_TOKENS);
                //

                TokenSettings tokenSettings = TokenSettings.builder()
                                .accessTokenTimeToLive(
                                                Duration.ofSeconds(Integer
                                                                .valueOf(accessTokenTimeToLive.getPropertyValue())))
                                .reuseRefreshTokens(Boolean.valueOf(reuseRefreshTokens.getPropertyValue()))
                                .refreshTokenTimeToLive(
                                                Duration.ofSeconds(Integer
                                                                .valueOf(refreshTokenTimeToLive.getPropertyValue())))
                                .idTokenSignatureAlgorithm(
                                                SignatureAlgorithm.from(idTokenSignatureAlgorithm.getPropertyValue()))
                                .build();
                log.debug("tokenSettings={}", tokenSettings);
                builder.tokenSettings(tokenSettings);

                RegisteredClient registeredClient = builder.build();
                return registeredClient;
        }

        private Oauth2ClientSetting findClientSetting(
                        String oauth2ClientId, String propertyType, String propertyName) {
                Optional<Oauth2ClientSetting> oauth2ClientSettingOptional = oauth2ClientSettingRepository
                                .findByOauth2ClientIdAndPropertyTypeAndPropertyName(
                                                oauth2ClientId, propertyType, propertyName);
                oauth2ClientSettingOptional.orElseThrow(
                                () -> new BadCredentialsException(
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
                log.debug("authorizationGrantType={}", authorizationGrantType);
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
