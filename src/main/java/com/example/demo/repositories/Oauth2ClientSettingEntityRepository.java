package com.example.demo.repositories;


import com.example.demo.entites.Oauth2ClientSettingEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface Oauth2ClientSettingEntityRepository
    extends JpaRepository<Oauth2ClientSettingEntity, String> {
  Optional<Oauth2ClientSettingEntity> findByOauth2ClientIdAndPropertyTypeAndPropertyName(
      String oauth2ClientId, String propertyType, String propertyName);
}
