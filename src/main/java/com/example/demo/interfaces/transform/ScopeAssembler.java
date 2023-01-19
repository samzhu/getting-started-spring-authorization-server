package com.example.demo.interfaces.transform;

import java.util.Optional;
import org.springframework.stereotype.Component;
import com.example.demo.infrastructure.repositories.ScopeRepository;
import com.example.demo.infrastructure.repositories.tables.pojos.Scope;
import com.example.demo.interfaces.rest.dto.ScopeWithDescription;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ScopeAssembler {
  private static final String DEFAULT_DESCRIPTION = "UNKNOWN SCOPE - We cannot provide information about this permission, use caution when granting this.";
  private final ScopeRepository scopeRepository;

  public ScopeWithDescription scopeToScopeWithDescription(String scopeCode) {
    ScopeWithDescription scopeWithDescription = null;
    Optional<Scope> resourceScopeOptional = scopeRepository.findByScope(scopeCode);
    if (resourceScopeOptional.isPresent()) {
      Scope scope = resourceScopeOptional.get();
      scopeWithDescription = new ScopeWithDescription(scope.getScope(), scope.getDescription());
    } else {
      scopeWithDescription = new ScopeWithDescription(scopeCode, DEFAULT_DESCRIPTION);
    }
    return scopeWithDescription;
  }
}
