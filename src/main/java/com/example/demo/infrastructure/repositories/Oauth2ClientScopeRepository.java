package com.example.demo.infrastructure.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.infrastructure.repositories.tables.pojos.Oauth2ClientScope;

@Repository
public interface Oauth2ClientScopeRepository extends JpaRepository<Oauth2ClientScope, java.lang.String> {


}