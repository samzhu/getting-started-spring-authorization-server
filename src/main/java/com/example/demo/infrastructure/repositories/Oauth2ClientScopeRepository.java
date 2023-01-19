package com.example.demo.infrastructure.repositories;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.example.demo.infrastructure.repositories.tables.pojos.Oauth2ClientScope;

@Repository
public interface Oauth2ClientScopeRepository extends JpaRepository<Oauth2ClientScope, java.lang.String> {
    List<Oauth2ClientScope> findByOauth2ClientId(String oauth2ClientId);
}