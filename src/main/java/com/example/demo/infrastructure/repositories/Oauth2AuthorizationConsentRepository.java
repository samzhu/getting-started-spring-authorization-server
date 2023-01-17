package com.example.demo.infrastructure.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.infrastructure.repositories.tables.pojos.Oauth2AuthorizationConsent;

@Repository
public interface Oauth2AuthorizationConsentRepository extends JpaRepository<Oauth2AuthorizationConsent, java.lang.String> {

}