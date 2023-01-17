package com.example.demo.infrastructure.repositories;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.infrastructure.repositories.tables.pojos.Scope;

@Repository
public interface ScopeRepository extends JpaRepository<Scope, java.lang.String> {


}