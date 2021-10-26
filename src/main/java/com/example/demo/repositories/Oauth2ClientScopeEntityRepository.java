package com.example.demo.repositories;


import com.example.demo.entites.Oauth2ClientScopeEntity;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface Oauth2ClientScopeEntityRepository
    extends JpaRepository<Oauth2ClientScopeEntity, String> {
  List<Oauth2ClientScopeEntity> findByOauth2ClientId(String oauth2ClientId);
}
