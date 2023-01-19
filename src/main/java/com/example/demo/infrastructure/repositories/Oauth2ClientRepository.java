package com.example.demo.infrastructure.repositories;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.example.demo.infrastructure.repositories.tables.pojos.Oauth2Client;

@Repository
public interface Oauth2ClientRepository extends JpaRepository<Oauth2Client, java.lang.String> {
    Optional<Oauth2Client> findByClientId(String clientId);
}