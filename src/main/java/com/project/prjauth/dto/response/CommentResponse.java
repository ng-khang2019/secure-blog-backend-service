package com.project.prjauth.dto.response;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter @Setter
public class CommentResponse {

    Long id;
    String content;
    LocalDateTime createdAt;

    Long userId;
    String firstName;
    String lastName;

    Long repliesCount;
}
