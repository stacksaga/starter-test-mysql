package com.example.controller;


import com.example.aggregator.OrderAggregator;
import com.example.executors.ReserveOrder;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mono.stacksaga.SagaTemplate;
import org.mono.stacksaga.TransactionResponse;
import org.mono.stacksaga.common.comiunication.TransactionTranceResponse;
import org.mono.stacksaga.db.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class TransactionServiceImplTest {


    @Autowired
    private TransactionService transactionService;
    @Autowired
    private SagaTemplate<OrderAggregator> orderAggregatorSagaTemplate;


    @BeforeEach
    void setUp() {

    }


    @Test
    void getTransactionTraceTestRevertComplete() {
        OrderAggregator orderAggregator = new OrderAggregator();
        orderAggregator.setUpdatedStatus("INIT_STEP>");
        orderAggregator.setType(OrderAggregator.Type.revert_complete);
        TransactionResponse<OrderAggregator> response = orderAggregatorSagaTemplate.doProcess(
                orderAggregator,
                ReserveOrder.class
        );

        Optional<TransactionTranceResponse> transactionTrace = transactionService.getTransactionTrace(orderAggregator.getAggregateTransactionId());
        if (!transactionTrace.isPresent()) {

            Assertions.fail();
        } else {
            transactionTrace.get().getExecutorOrderAsDetailsMap().forEach(System.out::println);
        }
    }

    @Test
    void getTransactionTraceTest() {
        OrderAggregator orderAggregator = new OrderAggregator();
        orderAggregator.setUpdatedStatus("INIT_STEP>");
        orderAggregator.setType(OrderAggregator.Type.revert_complete);
        TransactionResponse<OrderAggregator> response = orderAggregatorSagaTemplate.doProcess(
                orderAggregator,
                ReserveOrder.class
        );

        Optional<TransactionTranceResponse> transactionTrace = transactionService.getTransactionTrace(orderAggregator.getAggregateTransactionId());
        if (!transactionTrace.isPresent()) {

            Assertions.fail();
        } else {
            transactionTrace.get().getExecutorOrderAsDetailsMap().forEach(System.out::println);
        }
    }



}
