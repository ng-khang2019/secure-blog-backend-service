package com.project.prjauth.entity;

import com.project.prjauth.constant.PaymentProvider;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;

@Entity @Table(name = "donations")
@Getter @Setter@Builder
@NoArgsConstructor @AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Donation extends BaseEntity<Long>{

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "subscription_id", nullable = false)
    Subscription subscription;

    @Column(nullable = false)
    BigDecimal amount;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    PaymentProvider provider;

    @Column(nullable = false)
    String transactionRef;
}
