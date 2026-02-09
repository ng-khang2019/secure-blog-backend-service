package com.project.prjauth.dto.response;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter @Setter
public class CommentResponse {

    Long id;
    String content;
    Long userId;
    String firstName;
    String lastName;
    List<CommentResponse> replies = new ArrayList<>();
}
