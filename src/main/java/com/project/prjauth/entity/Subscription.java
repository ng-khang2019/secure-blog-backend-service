package com.project.prjauth.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;

@Entity @Table(name = "subscriptions")
@Getter @Setter @Builder
@NoArgsConstructor @AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Subscription extends BaseEntity<Long> {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id", nullable = false)
    User author;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "subcriber_id",nullable = false)
    User subscriber;

    // Use big decimal for precise calculation
    @Column(nullable = false)
    BigDecimal totalAmount = BigDecimal.ZERO;

    @Column(nullable = false)
    boolean isActive = true;

    @Column(nullable = false)
    boolean allowDonate = true;

}
