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
import org.mono.stacksaga.exception.EventStoreConnectionException;
import org.mono.stacksaga.exception.execution.RevertException;
import org.mono.stacksaga.executor.utils.ProcessStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.StopWatch;

import java.util.Date;
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
        try {
            TransactionResponse<OrderAggregator> response = orderAggregatorSagaTemplate.doProcess(
                    orderAggregator,
                    ReserveOrder.class
            );
        } catch (EventStoreConnectionException e) {
            e.printStackTrace();
        } catch (RevertException e) {
            e.printStackTrace();
        }

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
        try {
            TransactionResponse<OrderAggregator> response = orderAggregatorSagaTemplate.doProcess(
                    orderAggregator,
                    ReserveOrder.class
            );
        } catch (EventStoreConnectionException e) {
            e.printStackTrace();
        } catch (RevertException e) {
            e.printStackTrace();
        }

        Optional<TransactionTranceResponse> transactionTrace = transactionService.getTransactionTrace(orderAggregator.getAggregateTransactionId());
        if (!transactionTrace.isPresent()) {

            Assertions.fail();
        } else {
            transactionTrace.get().getExecutorOrderAsDetailsMap().forEach(System.out::println);
        }
    }


    @Test
    void placeOrderProcessComplete() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start("placeOrderProcessComplete");
        OrderAggregator orderAggregator = new OrderAggregator();
        orderAggregator.setUpdatedStatus("INIT_STEP>");
        orderAggregator.setType(OrderAggregator.Type.process_complete);
        orderAggregator.setTime(new Date());
        try {
            TransactionResponse<OrderAggregator> response = orderAggregatorSagaTemplate.doProcess(
                    orderAggregator,
                    ReserveOrder.class
            );
            stopWatch.stop();
            System.out.println("stopWatch " + stopWatch.getLastTaskInfo().getTimeMillis());
            Assertions.assertEquals(ProcessStatus.PROCESS_COMPLETED, response.getFinalProcessStatus());
        } catch (EventStoreConnectionException e) {
            e.printStackTrace();
        } catch (RevertException revertException) {
            System.out.println("revertException = " + revertException);
            revertException.printStackTrace();
        }

    }

    @Test
    void placeOrderRevertComplete() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start("placeOrderProcessComplete");
        OrderAggregator orderAggregator = new OrderAggregator();
        orderAggregator.setUpdatedStatus("INIT_STEP>");
        orderAggregator.setType(OrderAggregator.Type.revert_complete);
        orderAggregator.setTime(new Date());
        try {
            TransactionResponse<OrderAggregator> response = orderAggregatorSagaTemplate.doProcess(
                    orderAggregator,
                    ReserveOrder.class
            );
            stopWatch.stop();
            System.out.println("stopWatch " + stopWatch.getLastTaskInfo().getTimeMillis());
            Assertions.assertEquals(ProcessStatus.REVERT_COMPLETED, response.getFinalProcessStatus());
        } catch (EventStoreConnectionException e) {
            e.printStackTrace();
            OrderAggregator orderAggregatorWithOrder = (OrderAggregator) e.getFinalAggregateState();
        } catch (RevertException revertException) {
            System.out.println("revertException = " + revertException);
            revertException.printStackTrace();
        }

    }


}
