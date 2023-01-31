package com.example.demo.config;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.authentication.dao.AbstractUserDetailsAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;

import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.server.authorization.JdbcOAuth2AuthorizationConsentService;

import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationConsentService;

import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configurers.OAuth2AuthorizationServerConfigurer;
import org.springframework.security.oauth2.server.authorization.settings.AuthorizationServerSettings;
import org.springframework.security.oauth2.server.authorization.token.JwtEncodingContext;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenCustomizer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.RequestMatcher;

import com.example.demo.infrastructure.repositories.Oauth2ClientAuthenticationMethodRepository;
import com.example.demo.infrastructure.repositories.Oauth2ClientGrantTypeRepository;
import com.example.demo.infrastructure.repositories.Oauth2ClientRedirectUriRepository;
import com.example.demo.infrastructure.repositories.Oauth2ClientRepository;
import com.example.demo.infrastructure.repositories.Oauth2ClientScopeRepository;
import com.example.demo.infrastructure.repositories.Oauth2ClientSettingRepository;
import com.example.demo.infrastructure.repositories.ScopeRepository;
import com.example.demo.infrastructure.repositories.UserAccountRepository;
import com.example.demo.infrastructure.repositories.UserGroupMemberRepository;
import com.example.demo.infrastructure.repositories.UserGroupRepository;
import com.example.demo.security.CustomOAuth2Token;
import com.example.demo.security.CustomRegisteredClientRepository;
import com.example.demo.security.CustomUserDetailsAuthenticationProvider;
import com.example.demo.security.CustomUserDetailsService;
import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;

//TODO: 要改新寫法 https://github.com/spring-projects/spring-authorization-server/blob/main/samples/custom-consent-authorizationserver/src/main/java/sample/config/AuthorizationServerConfig.java

@Configuration(proxyBeanMethods = false)
public class AuthorizationServerConfig {

  @Value("${authserver.key-path}")
  private File pprivateKeyFile;

  @Value("${authserver.issuer}")
  private String issuer;

  private static final String CUSTOM_CONSENT_PAGE_URI = "/oauth2/consent";

  /**
   * Default Ebdpoints
   *
   * <p>
   * Authorization /oauth2/authorize Token Endpoint /oauth2/token Token Revocation
   * /oauth2/revoke Token Introspection /oauth2/introspect JWK Set Ecdpoint
   * /oauth2/jwks Authorization Server Metadata
   * /.well-known/oauth-authorization-server OIDC Provider Configuration
   * /.well-known/openid-configuration
   */
  @Bean
  @Order(Ordered.HIGHEST_PRECEDENCE)
  public SecurityFilterChain authorizationServerSecurityFilterChain(HttpSecurity http) throws Exception {
    // Original page
    // OAuth2AuthorizationServerConfiguration.applyDefaultSecurity(http);
    // return http.formLogin(Customizer.withDefaults()).build();
    OAuth2AuthorizationServerConfigurer authorizationServerConfigurer =
				new OAuth2AuthorizationServerConfigurer();

    // OAuth2AuthorizationServerConfigurer<HttpSecurity> authorizationServerConfigurer = new OAuth2AuthorizationServerConfigurer<>();
    authorizationServerConfigurer
        .authorizationEndpoint(authorizationEndpoint -> authorizationEndpoint.consentPage(CUSTOM_CONSENT_PAGE_URI));

    RequestMatcher endpointsMatcher = authorizationServerConfigurer.getEndpointsMatcher();

    http.securityMatcher(endpointsMatcher)
        .authorizeRequests(authorizeRequests -> authorizeRequests.anyRequest().authenticated())
        .csrf(csrf -> csrf.ignoringRequestMatchers(endpointsMatcher)).apply(authorizationServerConfigurer);
    return http.formLogin(Customizer.withDefaults()).build();
  }

  @Bean
  public RegisteredClientRepository registeredClientRepository(
      Oauth2ClientRepository oauth2ClientRepository,
      Oauth2ClientAuthenticationMethodRepository oauth2ClientAuthenticationMethodRepository,
      Oauth2ClientGrantTypeRepository oauth2ClientGrantTypeRepository,
      Oauth2ClientRedirectUriRepository oauth2ClientRedirectUriRepository,
      Oauth2ClientScopeRepository oauth2ClientScopeRepository,
      Oauth2ClientSettingRepository oauth2ClientSettingRepository,
      ScopeRepository scopeRepository) {
    RegisteredClientRepository registeredClientRepository = new CustomRegisteredClientRepository(
      oauth2ClientRepository, oauth2ClientAuthenticationMethodRepository, oauth2ClientGrantTypeRepository,
        oauth2ClientRedirectUriRepository, oauth2ClientScopeRepository, oauth2ClientSettingRepository,
        scopeRepository);
    return registeredClientRepository;
  }

  @Bean
  public OAuth2TokenCustomizer<JwtEncodingContext> oauth2TokenCustomizer() {
    return new CustomOAuth2Token();
  }

  @Bean
  public UserDetailsService userDetailsService(UserAccountRepository userAccountRepository,
      UserGroupMemberRepository userGroupMemberRepository, UserGroupRepository userGroupRepository) {
    return new CustomUserDetailsService(userAccountRepository, userGroupMemberRepository, userGroupRepository);
  }

  @Bean
  public OAuth2AuthorizationConsentService authorizationConsentService(JdbcTemplate jdbcTemplate,
      RegisteredClientRepository registeredClientRepository) {
    return new JdbcOAuth2AuthorizationConsentService(jdbcTemplate, registeredClientRepository);
  }

  // @Bean
  // public OAuth2AuthorizationService authorizationService(JdbcTemplate
  // jdbcTemplate,
  // RegisteredClientRepository registeredClientRepository) {
  // // DefaultLobHandler lobHandler = new DefaultLobHandler();
  // // lobHandler.setWrapAsLob(true);
  // return new JdbcOAuth2AuthorizationService(jdbcTemplate,
  // registeredClientRepository);
  // }

  @Bean
  public AbstractUserDetailsAuthenticationProvider abstractUserDetailsAuthenticationProvider(
      UserDetailsService userDetailsService, PasswordEncoder passwordEncoder) {
    return new CustomUserDetailsAuthenticationProvider(userDetailsService, passwordEncoder);
  }

  // @Bean
  // public OAuth2AuthorizationService authorizationService(
  // Oauth2AuthorizationEntityRepository oauth2AuthorizationEntityRepository,
  // RegisteredClientRepository registeredClientRepository) {
  // OAuth2AuthorizationService OAuth2AuthorizationService = new
  // CustomOAuth2AuthorizationService(
  // oauth2AuthorizationEntityRepository, registeredClientRepository);
  // return OAuth2AuthorizationService;
  // }

  // @Bean
  // public OAuth2AuthorizationConsentService
  // authorizationConsentService(JdbcTemplate jdbcTemplate,
  // RegisteredClientRepository registeredClientRepository) {
  // return new JdbcOAuth2AuthorizationConsentService(jdbcTemplate,
  // registeredClientRepository);
  // }

  @Bean
  public JWKSource<SecurityContext> jwkSource() throws Exception {
    String pemEncodedRSAPrivateKey = new String(Files.readAllBytes(pprivateKeyFile.toPath()), StandardCharsets.UTF_8);
    JWK jwk = JWK.parseFromPEMEncodedObjects(pemEncodedRSAPrivateKey);
    JWKSet jwkSet = new JWKSet(jwk);
    return (jwkSelector, securityContext) -> jwkSelector.select(jwkSet);
  }

  @Bean
  public AuthorizationServerSettings providerSettings() {
    return AuthorizationServerSettings.builder().issuer(issuer).build();
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }
}
