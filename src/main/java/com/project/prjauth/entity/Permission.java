package com.project.prjauth.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Entity @Table(name = "permissions")
@Getter @Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Permission extends BaseEntity<Long> {
    String name;
    String description;
}
