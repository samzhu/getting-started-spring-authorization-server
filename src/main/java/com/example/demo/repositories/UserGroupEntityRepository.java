package com.example.demo.repositories;


import com.example.demo.entites.UserGroupEntity;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserGroupEntityRepository extends JpaRepository<UserGroupEntity, String> {

  List<UserGroupEntity> findByIdIn(List<String> userGroupIds);
}
