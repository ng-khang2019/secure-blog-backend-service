package com.project.prjauth.dto.request;

import lombok.Getter;

@Getter
public class LoginRequest {
    private String username; // Email
    private String password;
}
