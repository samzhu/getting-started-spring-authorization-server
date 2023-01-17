package com.example.demo.infrastructure.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.infrastructure.repositories.tables.pojos.Oauth2ClientGrantType;

@Repository
public interface Oauth2ClientGrantTypeRepository extends JpaRepository<Oauth2ClientGrantType, java.lang.String> {

}