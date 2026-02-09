package com.project.prjauth.entity;

import com.project.prjauth.constant.PostStatus;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.DynamicUpdate;

import java.util.HashSet;
import java.util.Set;

@Entity @Table(name = "posts")
@Getter @Setter @Builder
@AllArgsConstructor @NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@DynamicUpdate
public class Post extends BaseEntity<Long> {

    @Column(nullable = false)
    String title;

    @Column(nullable = false, columnDefinition = "TEXT")
    String content;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    PostStatus status;

    @Column(nullable = false)
    Integer readingTime;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "thumbnail_id")
    Image thumbnail;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="author_id", nullable = false)
    User author;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    Category category;

    @Builder.Default
    @ManyToMany
    @JoinTable(
            name="post_tags",
            joinColumns = @JoinColumn(name="post_id"),
            inverseJoinColumns = @JoinColumn(name="tag_id")
    )
    Set<Tag> tags = new HashSet<>();

}
