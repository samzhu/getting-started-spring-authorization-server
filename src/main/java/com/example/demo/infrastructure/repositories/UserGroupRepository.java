package com.example.demo.infrastructure.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.infrastructure.repositories.tables.pojos.UserGroup;

@Repository
public interface UserGroupRepository extends JpaRepository<UserGroup, java.lang.String> {
    List<UserGroup> findByIdIn(List<String> userGroupIds);
}