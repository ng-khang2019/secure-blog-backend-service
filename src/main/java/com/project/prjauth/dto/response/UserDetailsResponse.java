package com.project.prjauth.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Set;

@Data @Builder
@NoArgsConstructor @AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserDetailsResponse {
    Long id;
    String firstName;
    String lastName;
    String username;
    String email;
    String phone;
    String imageUrl;

}
