package com.example.demo.infrastructure.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.infrastructure.repositories.tables.pojos.Oauth2ClientAuthenticationMethod;

@Repository
public interface Oauth2ClientAuthenticationMethodRepository
        extends JpaRepository<Oauth2ClientAuthenticationMethod, java.lang.String> {

    List<Oauth2ClientAuthenticationMethod> findByOauth2ClientId(String oauth2ClientId);

}