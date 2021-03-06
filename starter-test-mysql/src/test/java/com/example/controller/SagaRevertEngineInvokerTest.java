package com.example.controller;

import com.example.aggregator.OrderAggregator;
import com.example.executors.ReserveOrder;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mono.stacksaga.SagaTemplate;
import org.mono.stacksaga.TransactionResponse;
import org.mono.stacksaga.cb.CircuitBreakerBroker;
import org.mono.stacksaga.core.SagaRevertEngineInvoker;
import org.mono.stacksaga.db.entity.RelatedServiceEntity;
import org.mono.stacksaga.db.service.RelatedServiceService;
import org.mono.stacksaga.exception.ProcessStoppedWithGarbageException;
import org.mono.stacksaga.executor.utils.ProcessStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;
import java.util.UUID;

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
    void executeTransactionWithRevertError() throws InterruptedException {
        OrderAggregator orderAggregator = new OrderAggregator();
        orderAggregator.setUpdatedStatus("INIT_STEP>");
        orderAggregator.setType(OrderAggregator.Type.revert_error);
        TransactionResponse<OrderAggregator> response = orderAggregatorSagaTemplate.process(
                orderAggregator,
                ReserveOrder.class
        );
        System.out.println("Response : " + new ObjectMapper().writeValueAsString(response));
        Assertions.assertEquals(ProcessStatus.REVERT_FAILED, response.getFinalProcessStatus());
    }


    @SneakyThrows
    @Test
    void invokeRevertEngineWithTransaction() throws InterruptedException {
        OrderAggregator orderAggregator = new OrderAggregator();
        orderAggregator.setUpdatedStatus("INIT_STEP>");
        orderAggregator.setType(OrderAggregator.Type.revert_error);
        TransactionResponse<OrderAggregator> response = orderAggregatorSagaTemplate.process(
                orderAggregator,
                ReserveOrder.class
        );
        System.out.println("Response : " + new ObjectMapper().writeValueAsString(response));

        Optional<RelatedServiceEntity> transactionRevertReasonRelatedServiceUid =
                relatedServiceService.getTransactionRevertReasonRelatedServiceUid(
                        response.getAggregate().getAggregateTransactionId());

        transactionRevertReasonRelatedServiceUid.ifPresent(relatedServiceEntity -> {
            if (circuitBreakerBroker.updateListerServiceAvailability(relatedServiceEntity.getService_name(), true)) {
                String retryFootnote = UUID.randomUUID().toString();
                sagaRevertEngineInvoker.invokeRevertEngine(relatedServiceEntity.getService_name(), 1, retryFootnote);
            }
        });
        Thread.sleep(5000);
    }

    @SneakyThrows
    @Test
    void invokeRevertEngineMultipleTimesWithTransaction() {
        OrderAggregator orderAggregator = new OrderAggregator();
        orderAggregator.setUpdatedStatus("INIT_STEP>");
        orderAggregator.setType(OrderAggregator.Type.revert_error);
        TransactionResponse<OrderAggregator> response = orderAggregatorSagaTemplate.process(
                orderAggregator,
                ReserveOrder.class
        );

        Optional<RelatedServiceEntity> transactionRevertReasonRelatedServiceUid =
                relatedServiceService.getTransactionRevertReasonRelatedServiceUid(
                        response.getAggregate().getAggregateTransactionId());

        if (transactionRevertReasonRelatedServiceUid.isPresent()) {
            if (circuitBreakerBroker.updateListerServiceAvailability(transactionRevertReasonRelatedServiceUid.get().getService_name(), true)) {
                String retryFootnote = UUID.randomUUID().toString();
                sagaRevertEngineInvoker.invokeRevertEngine(transactionRevertReasonRelatedServiceUid.get().getService_name(), 1, retryFootnote);
                Thread.sleep(2_000);
                sagaRevertEngineInvoker.invokeRevertEngine(transactionRevertReasonRelatedServiceUid.get().getService_name(), 0, retryFootnote);
                sagaRevertEngineInvoker.invokeRevertEngine(transactionRevertReasonRelatedServiceUid.get().getService_name(), 1, retryFootnote);
                Thread.sleep(2_000);

                sagaRevertEngineInvoker.invokeRevertEngine(transactionRevertReasonRelatedServiceUid.get().getService_name(), 0, retryFootnote);
                sagaRevertEngineInvoker.invokeRevertEngine(transactionRevertReasonRelatedServiceUid.get().getService_name(), 1, retryFootnote);
                Thread.sleep(2_000);

                sagaRevertEngineInvoker.invokeRevertEngine(transactionRevertReasonRelatedServiceUid.get().getService_name(), 0, retryFootnote);
                sagaRevertEngineInvoker.invokeRevertEngine(transactionRevertReasonRelatedServiceUid.get().getService_name(), 1, retryFootnote);
                Thread.sleep(2_000);
            }
        }

        Thread.sleep(6000);
    }

    @SneakyThrows
    @Test
    void invokeRevertEngineWithoutTransactionWalletService() throws InterruptedException {

        String relatedService = "wallet-service";
        if (circuitBreakerBroker.updateListerServiceAvailability(relatedService, true)) {
            String retryFootnote = UUID.randomUUID().toString();
            sagaRevertEngineInvoker.invokeRevertEngine(relatedService, 1, retryFootnote);
        }
        Thread.sleep(5000);
    }

    @SneakyThrows
    @Test
    void invokeRevertEngineWithoutTransactionStockService() throws InterruptedException {

        String relatedService = "stock-service";
        if (circuitBreakerBroker.updateListerServiceAvailability(relatedService, true)) {
            String retryFootnote = UUID.randomUUID().toString();
            sagaRevertEngineInvoker.invokeRevertEngine(relatedService, 1, retryFootnote);
        }
        Thread.sleep(5000);
    }


}