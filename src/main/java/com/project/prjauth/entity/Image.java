package com.project.prjauth.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity @Table(name = "images")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Image extends BaseEntity<Long>{

    @Column(name = "url", nullable = false)
    String url;

    @Column(name = "public_id", nullable = false, unique = true)
    String publicId;
}
