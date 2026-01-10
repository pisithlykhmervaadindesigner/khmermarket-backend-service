package com.khmermarket.khmermarket.domain.repository;

import com.khmermarket.khmermarket.domain.entity.Role;
import com.khmermarket.khmermarket.enumerate.RoleName;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface RoleRepository extends JpaRepository<Role, UUID> {
    Optional<Role> findByName(RoleName name);
    boolean existsByName(RoleName name);
}
