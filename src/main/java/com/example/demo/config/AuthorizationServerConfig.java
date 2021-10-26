package com.example.demo.config;

import com.example.demo.repositories.Oauth2AuthorizationEntityRepository;
import com.example.demo.repositories.Oauth2ClientAuthenticationMethodEntityRepository;
import com.example.demo.repositories.Oauth2ClientEntityRepository;
import com.example.demo.repositories.Oauth2ClientGrantTypeEntityRepository;
import com.example.demo.repositories.Oauth2ClientRedirectUriEntityRepository;
import com.example.demo.repositories.Oauth2ClientScopeEntityRepository;
import com.example.demo.repositories.Oauth2ClientSettingEntityRepository;
import com.example.demo.repositories.ResourceScopeEntityRepository;
import com.example.demo.repositories.UserAccountEntityRepository;
import com.example.demo.repositories.UserGroupEntityRepository;
import com.example.demo.repositories.UserGroupMemberEntityRepository;
import com.example.demo.security.CustomOAuth2AuthorizationService;
import com.example.demo.security.CustomOAuth2Token;
import com.example.demo.security.CustomRegisteredClientRepository;
import com.example.demo.security.CustomUserDetailsAuthenticationProvider;
import com.example.demo.security.CustomUserDetailsService;
import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
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
import org.springframework.security.config.annotation.web.configuration.OAuth2AuthorizationServerConfiguration;
import org.springframework.security.config.annotation.web.configurers.oauth2.server.authorization.OAuth2AuthorizationServerConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.server.authorization.JdbcOAuth2AuthorizationConsentService;
import org.springframework.security.oauth2.server.authorization.JwtEncodingContext;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationConsentService;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.OAuth2TokenCustomizer;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.config.ProviderSettings;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.RequestMatcher;

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

    OAuth2AuthorizationServerConfigurer<HttpSecurity> authorizationServerConfigurer = new OAuth2AuthorizationServerConfigurer<>();
    authorizationServerConfigurer
        .authorizationEndpoint(authorizationEndpoint -> authorizationEndpoint.consentPage(CUSTOM_CONSENT_PAGE_URI));

    RequestMatcher endpointsMatcher = authorizationServerConfigurer.getEndpointsMatcher();

    http.requestMatcher(endpointsMatcher)
        .authorizeRequests(authorizeRequests -> authorizeRequests.anyRequest().authenticated())
        .csrf(csrf -> csrf.ignoringRequestMatchers(endpointsMatcher)).apply(authorizationServerConfigurer);
    return http.formLogin(Customizer.withDefaults()).build();
  }

  @Bean
  public RegisteredClientRepository registeredClientRepository(
      Oauth2ClientEntityRepository oauth2ClientEntityRepository,
      Oauth2ClientAuthenticationMethodEntityRepository oauth2ClientAuthenticationMethodEntityRepository,
      Oauth2ClientGrantTypeEntityRepository oauth2ClientGrantTypeRepository,
      Oauth2ClientRedirectUriEntityRepository oauth2ClientRedirectUriRepository,
      Oauth2ClientScopeEntityRepository oauth2ClientScopeRepository,
      Oauth2ClientSettingEntityRepository oauth2ClientSettingRepository,
      ResourceScopeEntityRepository resourceScopeRepository) {
    RegisteredClientRepository registeredClientRepository = new CustomRegisteredClientRepository(
        oauth2ClientEntityRepository, oauth2ClientAuthenticationMethodEntityRepository, oauth2ClientGrantTypeRepository,
        oauth2ClientRedirectUriRepository, oauth2ClientScopeRepository, oauth2ClientSettingRepository,
        resourceScopeRepository);
    return registeredClientRepository;
  }

  @Bean
  public OAuth2TokenCustomizer<JwtEncodingContext> oauth2TokenCustomizer() {
    return new CustomOAuth2Token();
  }

  @Bean
  public UserDetailsService userDetailsService(UserAccountEntityRepository userAccountRepository,
      UserGroupMemberEntityRepository userGroupMemberRepository, UserGroupEntityRepository userGroupRepository) {
    return new CustomUserDetailsService(userAccountRepository, userGroupMemberRepository, userGroupRepository);
  }

  @Bean
  public AbstractUserDetailsAuthenticationProvider abstractUserDetailsAuthenticationProvider(
      UserDetailsService userDetailsService, PasswordEncoder passwordEncoder) {
    return new CustomUserDetailsAuthenticationProvider(userDetailsService, passwordEncoder);
  }

  @Bean
  public OAuth2AuthorizationService authorizationService(
      Oauth2AuthorizationEntityRepository oauth2AuthorizationEntityRepository,
      RegisteredClientRepository registeredClientRepository) {
    OAuth2AuthorizationService OAuth2AuthorizationService = new CustomOAuth2AuthorizationService(
        oauth2AuthorizationEntityRepository, registeredClientRepository);
    return OAuth2AuthorizationService;
  }

  @Bean
  public OAuth2AuthorizationConsentService authorizationConsentService(JdbcTemplate jdbcTemplate,
      RegisteredClientRepository registeredClientRepository) {
    return new JdbcOAuth2AuthorizationConsentService(jdbcTemplate, registeredClientRepository);
  }

  @Bean
  public JWKSource<SecurityContext> jwkSource() throws Exception {
    String pemEncodedRSAPrivateKey = new String(Files.readAllBytes(pprivateKeyFile.toPath()), StandardCharsets.UTF_8);
    JWK jwk = JWK.parseFromPEMEncodedObjects(pemEncodedRSAPrivateKey);
    JWKSet jwkSet = new JWKSet(jwk);
    return (jwkSelector, securityContext) -> jwkSelector.select(jwkSet);
  }

  @Bean
  public ProviderSettings providerSettings() {
    return ProviderSettings.builder().issuer(issuer).build();
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }
}
