package com.example.demo.infrastructure.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.infrastructure.repositories.tables.pojos.Scope;

@Repository
public interface ScopeRepository extends JpaRepository<Scope, java.lang.String> {

    List<Scope> findByIdIn(List<String> scopeIds);

    Optional<Scope> findByScope(String scope);

}