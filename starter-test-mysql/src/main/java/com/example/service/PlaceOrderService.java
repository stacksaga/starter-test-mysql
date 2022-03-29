package com.example.service;

import com.example.aggregator.OrderAggregator;
import com.example.executors.ReserveOrder;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.mono.stacksaga.SagaTemplate;
import org.mono.stacksaga.TransactionResponse;
import org.mono.stacksaga.annotation.Coordinator;
import org.mono.stacksaga.core.annotation.AsyncListener;
import org.mono.stacksaga.core.lsitener.AggregatorListener;
import org.mono.stacksaga.executor.utils.ProcessStack;

@Slf4j
@Coordinator
@AllArgsConstructor
public class PlaceOrderService implements AggregatorListener<OrderAggregator> {

    private final SagaTemplate<OrderAggregator> orderAggregatorSagaTemplate;


    public TransactionResponse<OrderAggregator> placeOrder(OrderAggregator orderAggregator) {
        return orderAggregatorSagaTemplate.doProcess(orderAggregator,
                ReserveOrder.class);
    }

    @SneakyThrows
    @Override
    @AsyncListener
    public void onEachProcessPerformed(ProcessStack<OrderAggregator> previousProcessStack, OrderAggregator currentAggregate) {
        log.info("PlaceOrderService.onEachProcessPerformed");
    }

    @Override
    @AsyncListener
    public void onTransactionRevertSuccess(TransactionResponse<OrderAggregator> response) {
        System.out.println("PlaceOrderService.onTransactionRevertSuccess");
    }

    @Override
    @AsyncListener
    public void onTransactionRevertFailed(TransactionResponse<OrderAggregator> response) {
        log.info("PlaceOrderService.onTransactionRevertFailed");

    }
}
