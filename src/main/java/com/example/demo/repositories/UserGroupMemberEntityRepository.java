package com.example.demo.repositories;


import com.example.demo.entites.UserGroupMemberEntity;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserGroupMemberEntityRepository
    extends JpaRepository<UserGroupMemberEntity, String> {

  List<UserGroupMemberEntity> findByUserId(String userId);
}
