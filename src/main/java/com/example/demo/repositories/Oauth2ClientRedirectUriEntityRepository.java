package com.example.demo.repositories;


import com.example.demo.entites.Oauth2ClientRedirectUriEntity;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface Oauth2ClientRedirectUriEntityRepository
    extends JpaRepository<Oauth2ClientRedirectUriEntity, String> {
  List<Oauth2ClientRedirectUriEntity> findByOauth2ClientId(String oauth2ClientId);
}
