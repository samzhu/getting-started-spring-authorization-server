package com.example.demo.interfaces.transform;


import com.example.demo.entites.ResourceScopeEntity;
import com.example.demo.interfaces.rest.dto.ScopeWithDescription;
import com.example.demo.repositories.ResourceScopeEntityRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ScopeAssembler {
  private static final String DEFAULT_DESCRIPTION =
      "UNKNOWN SCOPE - We cannot provide information about this permission, use caution when granting this.";
  private final ResourceScopeEntityRepository resourceScopeRepository;

  public ScopeWithDescription scopeToScopeWithDescription(String scope) {
    ScopeWithDescription scopeWithDescription = null;
    Optional<ResourceScopeEntity> resourceScopeOptional =
        resourceScopeRepository.findByScope(scope);
    if (resourceScopeOptional.isPresent()) {
      ResourceScopeEntity resourceScope = resourceScopeOptional.get();
      scopeWithDescription =
          new ScopeWithDescription(resourceScope.getScope(), resourceScope.getDescription());
    } else {
      scopeWithDescription = new ScopeWithDescription(scope, DEFAULT_DESCRIPTION);
    }
    return scopeWithDescription;
  }
}
