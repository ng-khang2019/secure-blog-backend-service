package com.project.prjauth.service;

import com.project.prjauth.dto.request.CommentRequest;
import com.project.prjauth.dto.response.CommentResponse;
import com.project.prjauth.entity.User;
import org.springframework.data.domain.Page;

public interface CommentService {

    CommentResponse createComment(Long postId, CommentRequest request, User currentUser);
    CommentResponse replyToComment(Long postId, Long parentId, CommentRequest request, User currentUser);
    CommentResponse editComment(Long commentId, CommentRequest request, User currentUser);
    Page<CommentResponse> getRootComments(Long postId, int page, int size);
    Page<CommentResponse> getChildrenComments(Long parentId, int page, int size);
    void deleteComment(Long commentId, User currentUser);
}
