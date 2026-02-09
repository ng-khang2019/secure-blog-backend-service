package com.project.prjauth.service;

import com.project.prjauth.dto.response.CommentResponse;
import org.springframework.data.domain.Page;

public interface CommentService {

    Page<CommentResponse> getRootComments(Long postId, int page, int size);
    Page<CommentResponse> getChildrenComments(Long parentId, int page, int size);
}
