package com.example.demo.repositories;


import com.example.demo.entites.Oauth2ClientAuthenticationMethodEntity;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface Oauth2ClientAuthenticationMethodEntityRepository
    extends JpaRepository<Oauth2ClientAuthenticationMethodEntity, String> {

  List<Oauth2ClientAuthenticationMethodEntity> findByOauth2ClientId(String oauth2ClientId);
}
