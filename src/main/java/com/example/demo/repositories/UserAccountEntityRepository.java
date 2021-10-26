package com.example.demo.repositories;


import com.example.demo.entites.UserAccountEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserAccountEntityRepository extends JpaRepository<UserAccountEntity, String> {

  Optional<UserAccountEntity> findByUserName(String userName);
}
