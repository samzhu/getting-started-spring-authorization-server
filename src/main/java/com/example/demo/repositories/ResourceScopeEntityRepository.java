package com.example.demo.repositories;


import com.example.demo.entites.ResourceScopeEntity;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ResourceScopeEntityRepository extends JpaRepository<ResourceScopeEntity, String> {

  List<ResourceScopeEntity> findByIdIn(List<String> resourceScopeIds);

  Optional<ResourceScopeEntity> findByScope(String scope);
}
