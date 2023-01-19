package com.example.demo.infrastructure.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.infrastructure.repositories.tables.pojos.Oauth2ClientRedirectUri;

@Repository
public interface Oauth2ClientRedirectUriRepository extends JpaRepository<Oauth2ClientRedirectUri, java.lang.String> {
    List<Oauth2ClientRedirectUri> findByOauth2ClientId(String oauth2ClientId);

}