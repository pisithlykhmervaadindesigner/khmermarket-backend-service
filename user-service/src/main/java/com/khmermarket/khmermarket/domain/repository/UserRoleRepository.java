package com.khmermarket.khmermarket.domain.repository;

import com.khmermarket.khmermarket.domain.entity.UserRole;
import com.khmermarket.khmermarket.enumerate.RoleName;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRoleRepository extends JpaRepository<UserRole, UUID> {
    Optional<UserRole> findByName(RoleName name);
    boolean existsByName(RoleName name);
    List<UserRole> findAll();
    List<UserRole> findAllByUserId(UUID userId);
}
