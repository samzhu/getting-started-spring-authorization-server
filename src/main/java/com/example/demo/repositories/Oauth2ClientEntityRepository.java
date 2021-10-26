package com.example.demo.repositories;


import com.example.demo.entites.Oauth2ClientEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface Oauth2ClientEntityRepository extends JpaRepository<Oauth2ClientEntity, String> {

  public Optional<Oauth2ClientEntity> findByClientId(String clientId);
}
