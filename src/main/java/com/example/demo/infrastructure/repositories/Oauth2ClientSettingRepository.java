package com.example.demo.infrastructure.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.infrastructure.repositories.tables.pojos.Oauth2ClientSetting;

@Repository
public interface Oauth2ClientSettingRepository extends JpaRepository<Oauth2ClientSetting, java.lang.String> {


}