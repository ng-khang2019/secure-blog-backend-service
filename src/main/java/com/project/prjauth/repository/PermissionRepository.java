package com.project.prjauth.repository;

import com.project.prjauth.entity.Permission;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface PermissionRepository extends JpaRepository<Permission, Long> {
    void deletePermissionByName(String name);
    List<Permission> findByNameIn(Set<String> names);
    Optional<Permission> findByName(String name);
}
