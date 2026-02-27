package com.project.prjauth.dto.response;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Getter @Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CommentResponse {

    Long id;
    String content;
    LocalDateTime createdAt;

    Long userId;
    String firstName;
    String lastName;

    Long repliesCount;
}
