package com.project.prjauth.repository;

import com.project.prjauth.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {

    @Query("""
    Select r From Role r Left Join Fetch r.permissions
    """)
    List<Role> findAllWithPermissions();

    void deleteRoleByName(String name);

    Optional<Role> findByName(String name);
}
