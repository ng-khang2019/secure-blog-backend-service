package com.project.prjauth.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Getter
@AllArgsConstructor @NoArgsConstructor
public class AuthResponse implements Serializable {
    private String accessToken;
}
