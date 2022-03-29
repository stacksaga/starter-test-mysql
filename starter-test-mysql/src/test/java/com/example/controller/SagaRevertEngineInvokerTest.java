package com.example.controller;

import com.example.aggregator.OrderAggregator;
import com.example.executors.ReserveOrder;
import com.example.ms.customerwalletservice.CustomerWalletService;
import com.example.ms.userservice.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mono.stacksaga.SagaTemplate;
import org.mono.stacksaga.TransactionResponse;
import org.mono.stacksaga.cb.CircuitBreakerBroker;
import org.mono.stacksaga.core.SagaRevertEngineInvoker;
import org.mono.stacksaga.db.entity.RelatedServiceEntity;
import org.mono.stacksaga.db.service.RelatedServiceService;
import org.mono.stacksaga.db.service.TransactionService;
import org.mono.stacksaga.init.Store;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class SagaRevertEngineInvokerTest {

    @Autowired
    private SagaTemplate<OrderAggregator> orderAggregatorSagaTemplate;
    @Autowired
    private SagaRevertEngineInvoker sagaRevertEngineInvoker;

    @Autowired
    private RelatedServiceService relatedServiceService;

    @Autowired
    private CircuitBreakerBroker circuitBreakerBroker;


    @BeforeEach
    void setUp() {


    }

    @AfterEach
    void tearDown() {
    }

    @SneakyThrows
    @Test
    void invokeRevertEngine() throws InterruptedException {
        OrderAggregator orderAggregator = new OrderAggregator();
        orderAggregator.setUpdatedStatus("INIT_STEP>");
        orderAggregator.setType(OrderAggregator.Type.revert_error);
        TransactionResponse<OrderAggregator> response = orderAggregatorSagaTemplate.doProcess(
                orderAggregator,
                ReserveOrder.class
        );
        System.out.println("Response : " + new ObjectMapper().writeValueAsString(response));

        Optional<RelatedServiceEntity> transactionRevertReasonRelatedServiceUid =
                relatedServiceService.getTransactionRevertReasonRelatedServiceUid(
                        response.getAggregate().getAggregateTransactionId());
        transactionRevertReasonRelatedServiceUid.ifPresent(relatedServiceEntity -> {
            if (circuitBreakerBroker.updateListerServiceAvailability(relatedServiceEntity.getService_name(), true)) {
                sagaRevertEngineInvoker.invokeRevertEngine(relatedServiceEntity.getService_name(), 1);
            }
        });
        Thread.sleep(5000);
    }
}