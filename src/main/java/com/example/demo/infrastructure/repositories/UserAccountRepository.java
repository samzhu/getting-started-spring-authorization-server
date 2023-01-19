package com.example.demo.infrastructure.repositories;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.stereotype.Repository;

import com.example.demo.infrastructure.repositories.tables.pojos.UserAccount;

@Repository
public interface UserAccountRepository extends JpaRepository<UserAccount, java.lang.String> {

    Optional<UserAccount> findByUserName(String userName);

}