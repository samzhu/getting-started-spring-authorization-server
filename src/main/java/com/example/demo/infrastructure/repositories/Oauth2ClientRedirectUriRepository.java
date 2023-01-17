package com.example.demo.infrastructure.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.infrastructure.repositories.tables.pojos.Oauth2ClientRedirectUri;

@Repository
public interface Oauth2ClientRedirectUriRepository extends JpaRepository<Oauth2ClientRedirectUri, java.lang.String> {


}