package com.project.prjauth.repository;

import com.project.prjauth.entity.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    @Query("""
        Select c From Comment c
        Where c.post.id = :postId
        And c.parent Is Null
        Order By c.createdAt Asc
    """)
    Page<Comment> findRootCommentsByPostId(@Param("postId")Long postId, Pageable pageable);

    @Query("""
        Select c From Comment c
        Where c.parent.id = :parentId
        Order By c.createdAt Asc
    """)
    Page<Comment> findChildrenCommentsByParentId(@Param("parentId") Long parentId, Pageable pageable);

    @Query("Select Count(c) From Comment c Where c.parent.id = :parentId")
    Long countRepliesByParentId(@Param("parentId")Long parentId);

}
