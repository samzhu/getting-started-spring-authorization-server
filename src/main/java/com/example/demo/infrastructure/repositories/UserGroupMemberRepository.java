package com.example.demo.infrastructure.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.infrastructure.repositories.tables.pojos.UserGroupMember;

@Repository
public interface UserGroupMemberRepository extends JpaRepository<UserGroupMember, java.lang.String> {
    List<UserGroupMember> findByUserId(String userId);
}