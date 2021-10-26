package com.example.demo.interfaces.rest;


import com.example.demo.interfaces.rest.dto.ScopeWithDescription;
import com.example.demo.interfaces.transform.ScopeAssembler;
import java.security.Principal;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationConsent;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationConsentService;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/** @author Daniel Garnier-Moiroux */
@Controller
@RequiredArgsConstructor
public class AuthorizationConsentController {
  private final RegisteredClientRepository registeredClientRepository;
  private final OAuth2AuthorizationConsentService authorizationConsentService;
  private final ScopeAssembler scopeAssembler;

  @GetMapping(value = "/oauth2/consent")
  public String consent(
      Principal principal,
      Model model,
      @RequestParam(OAuth2ParameterNames.CLIENT_ID) String clientId,
      @RequestParam(OAuth2ParameterNames.SCOPE) String scope,
      @RequestParam(OAuth2ParameterNames.STATE) String state) {

    // Remove scopes that were already approved
    Set<String> scopesToApprove = new HashSet<>();
    Set<String> previouslyApprovedScopes = new HashSet<>();
    RegisteredClient registeredClient = this.registeredClientRepository.findByClientId(clientId);
    OAuth2AuthorizationConsent currentAuthorizationConsent =
        this.authorizationConsentService.findById(registeredClient.getId(), principal.getName());
    Set<String> authorizedScopes;
    if (currentAuthorizationConsent != null) {
      authorizedScopes = currentAuthorizationConsent.getScopes();
    } else {
      authorizedScopes = Collections.emptySet();
    }
    for (String requestedScope : StringUtils.delimitedListToStringArray(scope, " ")) {
      if (authorizedScopes.contains(requestedScope)) {
        previouslyApprovedScopes.add(requestedScope);
      } else {
        scopesToApprove.add(requestedScope);
      }
    }

    model.addAttribute("clientId", clientId);
    model.addAttribute("state", state);
    model.addAttribute("scopes", withDescription(scopesToApprove));
    model.addAttribute("previouslyApprovedScopes", withDescription(previouslyApprovedScopes));
    model.addAttribute("principalName", principal.getName());

    return "consent";
  }

  private Set<ScopeWithDescription> withDescription(Set<String> scopes) {
    Set<ScopeWithDescription> scopeWithDescriptions = new HashSet<>();
    for (String scope : scopes) {

      scopeWithDescriptions.add(scopeAssembler.scopeToScopeWithDescription(scope));
    }
    return scopeWithDescriptions;
  }
}
