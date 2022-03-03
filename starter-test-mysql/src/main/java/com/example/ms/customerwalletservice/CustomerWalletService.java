package com.example.ms.customerwalletservice;

import com.example.ms.customerwalletservice.entity.CustomerWalletEntity;
import com.example.ms.customerwalletservice.repository.CustomerWalletRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CustomerWalletService {
    @Autowired
    private CustomerWalletRepository repository;

    public void addWallet(CustomerWalletEntity entity) {
        repository.save(entity);
    }

    public void delete(CustomerWalletEntity customerWalletEntity) {
        repository.delete(customerWalletEntity);
    }
}
