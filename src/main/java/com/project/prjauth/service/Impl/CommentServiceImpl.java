package com.project.prjauth.service.Impl;

import com.project.prjauth.constant.PredefinedRole;
import com.project.prjauth.dto.request.CommentRequest;
import com.project.prjauth.dto.response.CommentResponse;
import com.project.prjauth.entity.Comment;
import com.project.prjauth.entity.Post;
import com.project.prjauth.entity.User;
import com.project.prjauth.exception.ResourceNotFoundException;
import com.project.prjauth.mapper.CommentMapper;
import com.project.prjauth.repository.CommentRepository;
import com.project.prjauth.repository.PostRepository;
import com.project.prjauth.repository.RoleRepository;
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
    private final RoleRepository roleRepository;
    private final CommentMapper commentMapper;

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

        return commentMapper.toCommentResponse(commentRepository.save(comment));
    }

    @Override
    @Transactional
    public CommentResponse replyToComment(Long postId, Long parentId, CommentRequest request, User currentUser) {
        Post post = postRepository.findById(postId).orElseThrow(
                () -> new ResourceNotFoundException("Post not found"));

        Comment parentComment = commentRepository.findById(parentId).orElseThrow(
                () -> new ResourceNotFoundException("Parent comment not found"));

        if (!parentComment.getPost().getId().equals(postId)) {
            throw new IllegalArgumentException("Parent comment does not belong to the post");}

        Comment reply = Comment.builder()
                .content(request.getContent())
                .user(currentUser)
                .post(post)
                .parent(parentComment)
                .build();
        return commentMapper.toCommentResponse(commentRepository.save(reply));
    }

    @Override
    @Transactional
    public CommentResponse editComment(Long commentId, CommentRequest request, User currentUser) {
        Comment comment = commentRepository.findById(commentId).orElseThrow(
                () -> new ResourceNotFoundException("Comment not found"));

        if (!comment.getUser().getEmail().equals(currentUser.getEmail())) {
            throw new IllegalStateException("You are not authorized to edit this comment");}

        comment.setContent(request.getContent());
        return commentMapper.toCommentResponse(commentRepository.save(comment));
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
    @Transactional
    public void deleteComment(Long commentId, User currentUser) {
        Comment comment = commentRepository.findById(commentId).orElseThrow(
                () -> new ResourceNotFoundException("Comment not found"));

        boolean isOwner = comment.getUser().getEmail().equals(currentUser.getEmail());
        boolean isAdmin = currentUser.getRoles()
                .stream()
                .anyMatch(role -> role.getName().equals(PredefinedRole.ADMIN_ROLE));

        if (!isOwner && !isAdmin) {
            throw new IllegalStateException("You are not authorized to delete this comment");
        }
        commentRepository.delete(comment);
    }
}
