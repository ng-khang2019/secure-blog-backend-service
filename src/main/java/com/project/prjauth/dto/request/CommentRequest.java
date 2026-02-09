package com.project.prjauth.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class CommentRequest {

    @NotBlank( message = "Comment cannot be blank")
    String content;
}
