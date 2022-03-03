package com.example.ms.customerwalletservice.entity;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CustomerWalletEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long customerWalletId;
    private double availableAmount;
    private Long customerId;
}
