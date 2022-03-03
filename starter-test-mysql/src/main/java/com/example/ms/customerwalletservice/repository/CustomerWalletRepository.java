package com.example.ms.customerwalletservice.repository;

import com.example.ms.customerwalletservice.entity.CustomerWalletEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CustomerWalletRepository extends JpaRepository<CustomerWalletEntity, Long> {
    Optional<CustomerWalletEntity> findFirstByCustomerId(Long customerId);

}
