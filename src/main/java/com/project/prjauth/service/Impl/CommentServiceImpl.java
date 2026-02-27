package com.project.prjauth.service.Impl;

import com.project.prjauth.dto.request.CommentRequest;
import com.project.prjauth.dto.response.CommentResponse;
import com.project.prjauth.entity.Comment;
import com.project.prjauth.entity.Post;
import com.project.prjauth.entity.User;
import com.project.prjauth.exception.ResourceNotFoundException;
import com.project.prjauth.mapper.CommentMapper;
import com.project.prjauth.repository.CommentRepository;
import com.project.prjauth.repository.PostRepository;
import com.project.prjauth.service.CommentService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {
    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final CommentMapper CommentMapper;

    @Override
    @Transactional
    public CommentResponse createComment(Long postId, CommentRequest request, User currentUser) {
        Post post = postRepository.findById(postId).orElseThrow(
                () -> new ResourceNotFoundException("Post not found"));
        Comment comment = Comment.builder()
                .content(request.getContent())
                .user(currentUser)
                .post(post)
                .parent(null)
                .build();

        return CommentMapper.toCommentResponse(commentRepository.save(comment));
    }

    @Override
    public CommentResponse replyToComment(Long postId, Long parentId, CommentRequest request, User currentUser) {
        return null;
    }

    @Override
    public CommentResponse editComment(Long commentId, CommentRequest request, User currentUser) {
        return null;
    }

    @Override
    public Page<CommentResponse> getRootComments(Long postId, int page, int size) {
        return null;
    }

    @Override
    public Page<CommentResponse> getChildrenComments(Long parentId, int page, int size) {
        return null;
    }

    @Override
    public void deleteComment(Long commentId, User currentUser) {

    }
}
