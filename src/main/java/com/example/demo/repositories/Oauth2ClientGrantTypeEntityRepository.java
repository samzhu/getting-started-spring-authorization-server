package com.example.demo.repositories;


import com.example.demo.entites.Oauth2ClientGrantTypeEntity;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface Oauth2ClientGrantTypeEntityRepository
    extends JpaRepository<Oauth2ClientGrantTypeEntity, String> {
  List<Oauth2ClientGrantTypeEntity> findByOauth2ClientId(String oauth2ClientId);
}
