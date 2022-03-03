package com.example.ms.userservice.entity;

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
public class UserEntity {
    @Id
//    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long userId;
    private String username;
    private boolean isActive;
}
