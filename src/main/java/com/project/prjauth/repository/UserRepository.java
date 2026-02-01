package com.project.prjauth.repository;

import com.project.prjauth.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);

    @Query("""
        Select u From User u
        Left Join Fetch u.roles r
        Left Join Fetch r.permissions
        Where u.email = :email
    """)
    Optional<User> findByEmailWithRoles(@Param("email")String email);
}
