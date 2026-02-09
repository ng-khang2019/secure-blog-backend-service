package com.project.prjauth.dto.response;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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
