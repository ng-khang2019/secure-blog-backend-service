package com.project.prjauth.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.HashSet;
import java.util.Set;

@Entity @Table(name = "tags")
@Getter @Setter @Builder
@AllArgsConstructor @NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Tag extends BaseEntity<Long> {

    @Column(nullable = false, unique = true)
    String name;

    @ManyToMany(mappedBy = "tags")
    Set<Post> posts = new HashSet<>();
}
