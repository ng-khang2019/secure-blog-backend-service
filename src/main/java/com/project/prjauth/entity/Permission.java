package com.project.prjauth.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.util.Set;

@Entity @Table(name = "permissions")
@Getter @Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Permission extends BaseEntity<Long> {
    String name;
    String description;
}
