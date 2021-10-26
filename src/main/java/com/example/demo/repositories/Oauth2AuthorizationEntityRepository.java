package com.example.demo.repositories;


import com.example.demo.entites.Oauth2AuthorizationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface Oauth2AuthorizationEntityRepository
    extends JpaRepository<Oauth2AuthorizationEntity, String> {

  Oauth2AuthorizationEntity
      findByStateOrAuthorizationCodeValueOrAccessTokenValueOrRefreshTokenValue(
          String state,
          String authorizationCodeValue,
          String accessTokenValue,
          String refreshTokenValue);

  Oauth2AuthorizationEntity findByState(String state);

  Oauth2AuthorizationEntity findByAuthorizationCodeValue(String authorizationCodeValue);

  Oauth2AuthorizationEntity findByAccessTokenValue(String accessTokenValue);

  Oauth2AuthorizationEntity findByRefreshTokenValue(String refreshTokenValue);
}
