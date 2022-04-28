package com.example.service;

import com.example.aggregator.OrderAggregator;
import com.example.executors.ReserveOrder;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.mono.stacksaga.RevertHintStore;
import org.mono.stacksaga.SagaTemplate;
import org.mono.stacksaga.TransactionResponse;
import org.mono.stacksaga.annotation.Coordinator;
import org.mono.stacksaga.core.annotation.AsyncListener;
import org.mono.stacksaga.core.lsitener.AggregatorListener;
import org.mono.stacksaga.exception.EventStoreConnectionException;
import org.mono.stacksaga.exception.NetworkException;
import org.mono.stacksaga.exception.execution.ExecutorException;
import org.mono.stacksaga.exception.execution.RevertException;
import org.mono.stacksaga.exception.execution.UnHandledException;
import org.mono.stacksaga.executor.utils.ProcessStack;

@Slf4j
@Coordinator
@AllArgsConstructor
public class PlaceOrderService implements AggregatorListener<OrderAggregator> {

    private final SagaTemplate<OrderAggregator> orderAggregatorSagaTemplate;


    public TransactionResponse<OrderAggregator> placeOrder(OrderAggregator orderAggregator) {
        try {
            return orderAggregatorSagaTemplate.process(orderAggregator,
                    ReserveOrder.class);
        } catch (UnHandledException e) {
            System.out.println("UnHandledException");
            e.printStackTrace();
            return null;
        } catch (NetworkException e) {
            throw new RuntimeException(e);
        }
    }

    @SneakyThrows
    @Override
    @AsyncListener
    public void onEachProcessPerformed(ProcessStack<OrderAggregator> previousProcessStack, OrderAggregator currentAggregate) {
        log.info("PlaceOrderService.onEachProcessPerformed");
    }

    @Override
    @AsyncListener
    public void onEachRevertPerformed(ProcessStack<OrderAggregator> previousProcessStack, ExecutorException executorException, RevertHintStore revertHintStore) throws RevertException {

        System.out.println("PlaceOrderService.onEachRevertPerformed");
    }

    @Override
    @AsyncListener
    public void onTransactionCompleted(TransactionResponse<OrderAggregator> response) {
        System.out.println("PlaceOrderService.onTransactionCompleted");
    }

    @Override
    @AsyncListener
    public void onTransactionTerminated(UnHandledException unHandledException) {
        System.out.println("PlaceOrderService.onTransactionTerminated");
    }
}
