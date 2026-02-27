package com.project.prjauth.dto.request;

import com.project.prjauth.annotation.WordRange;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class CommentRequest {

    @NotBlank( message = "Comment cannot be blank")
    @Size(min = 2, message = "Comment must be at least 2 characters long")
    @WordRange(min = 1, max = 400, message = "Comment must be between 1 and 400 words")
    private String content;
}
