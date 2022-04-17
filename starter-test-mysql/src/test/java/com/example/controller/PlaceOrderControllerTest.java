package com.example.controller;

import com.example.aggregator.OrderAggregator;
import com.example.executors.ReserveOrder;
import com.example.ms.customerwalletservice.CustomerWalletService;
import com.example.ms.customerwalletservice.entity.CustomerWalletEntity;
import com.example.ms.orderservice.CustomerOrderService;
import com.example.ms.userservice.UserService;
import com.example.ms.userservice.entity.UserEntity;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mono.stacksaga.SagaTemplate;
import org.mono.stacksaga.TransactionResponse;
import org.mono.stacksaga.exception.EventStoreConnectionException;
import org.mono.stacksaga.exception.execution.RevertException;
import org.mono.stacksaga.executor.utils.ProcessStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;

import java.util.Objects;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class PlaceOrderControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private UserService userService;

    @Autowired
    private CustomerWalletService customerWalletService;

    @Autowired
    private CustomerOrderService customerOrderService;

    private final UserEntity userEntity = UserEntity.builder()
            .username("mafei")
            .isActive(true)
            .userId(1L)
            .build();

    private final CustomerWalletEntity customerWalletEntity = CustomerWalletEntity.builder()
            .customerWalletId(22L)
            .availableAmount(200.00)
            .customerId(1L)
            .build();

    @BeforeEach
    void setUp() {
        customerWalletService.addWallet(customerWalletEntity);
        userService.saveUser(userEntity);
        System.out.println("PlaceOrderControllerTest.setUp");
    }

    @AfterEach
    void tearDown() {
//        userService.delete(userEntity);
//        customerWalletService.delete(customerWalletEntity);

    }

    @Autowired
    private SagaTemplate<OrderAggregator> orderAggregatorSagaTemplate;

    @Autowired
    private ObjectMapper objectMapper;


    @Test
    void placeOrderProcessRevertCompleteWithoutCommandRevert() {
        OrderAggregator orderAggregator = new OrderAggregator();
        orderAggregator.setUpdatedStatus("INIT_STEP>");
        orderAggregator.setType(OrderAggregator.Type.revert_complete);
        TransactionResponse<OrderAggregator> response = null;
        try {
            response = orderAggregatorSagaTemplate.process(
                    orderAggregator,
                    ReserveOrder.class
            );
        } catch (EventStoreConnectionException e) {
            e.printStackTrace();
        } catch (RevertException e) {
            e.printStackTrace();
        }
        Assertions.assertEquals(ProcessStatus.REVERT_COMPLETED, Objects.requireNonNull(response).getFinalProcessStatus());
    }

    @Test
    void placeOrderProcessRevertCompleteWithCommandRevert() {

        OrderAggregator orderAggregator = new OrderAggregator();
        orderAggregator.setUpdatedStatus("INIT_STEP>");
        orderAggregator.setType(OrderAggregator.Type.revert_error);
        TransactionResponse<OrderAggregator> response = null;
        try {
            response = orderAggregatorSagaTemplate.process(
                    orderAggregator,
                    ReserveOrder.class
            );
        } catch (EventStoreConnectionException e) {
            e.printStackTrace();
        } catch (RevertException e) {
            e.printStackTrace();
        }
        Assertions.assertEquals(ProcessStatus.REVERT_FAILED, Objects.requireNonNull(response).getFinalProcessStatus());
    }


}