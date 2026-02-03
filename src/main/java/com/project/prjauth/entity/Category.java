package com.project.prjauth.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.ArrayList;
import java.util.List;

@Entity @Table(name = "categories")
@Getter @Setter @Builder
@AllArgsConstructor @NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Category extends BaseEntity<Long>{

    @Column(nullable = false, unique = true)
    String name;

    @Builder.Default
    @OneToMany(mappedBy = "category")
    List<Post> posts = new ArrayList<>();
}
