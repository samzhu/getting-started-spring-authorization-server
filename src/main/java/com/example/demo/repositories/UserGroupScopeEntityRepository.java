package com.example.demo.repositories;


import com.example.demo.entites.UserGroupScopeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserGroupScopeEntityRepository
    extends JpaRepository<UserGroupScopeEntity, String> {}
