package com.project.prjauth.repository;

import com.project.prjauth.dto.response.UserProfileResponse;
import com.project.prjauth.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserDisplayRepository extends JpaRepository<User,Long> {
    @Query("""
            Select new com.project.prjauth.dto.response.UserProfileResponse(
                u.id, u.firstName, u.lastName, u.username, i.url)
            From User u
            Left Join u.avatarImage i
            Where u.id = :id
    """)
    UserProfileResponse findUserProfileById(Long id);

    @Query("""
            Select new com.project.prjauth.dto.response.UserProfileResponse(
                u.id, u.firstName, u.lastName, u.username, i.url)
            From User u
            Left Join u.avatarImage i
            Where u.email = :email
    """)
    UserProfileResponse findUserProfileByEmail(@Param("email") String email);
}
