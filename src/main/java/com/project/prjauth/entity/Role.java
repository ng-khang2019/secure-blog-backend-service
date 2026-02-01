package com.project.prjauth.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Set;

@Entity @Table(name = "roles")
@Getter @Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Role extends BaseEntity<Long> {
    String name;
    String description;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "role_permission",
            joinColumns = @JoinColumn(name = "role_id"),
            inverseJoinColumns = @JoinColumn(name = "permission_id")
    )
    @JsonIgnoreProperties(value = {"roles"})
    Set<Permission> permissions;
}
