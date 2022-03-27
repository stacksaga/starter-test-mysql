package com.example.service;

import com.example.aggregator.OrderAggregator;
import com.example.executors.ReserveOrder;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.mono.stacksaga.SagaTemplate;
import org.mono.stacksaga.TransactionResponse;
import org.mono.stacksaga.annotation.Coordinator;
import org.mono.stacksaga.core.annotation.AsyncListener;
import org.mono.stacksaga.core.lsitener.AggregatorListener;
import org.mono.stacksaga.executor.utils.ProcessStack;
import org.springframework.beans.factory.annotation.Autowired;

@Slf4j
@Coordinator
public class PlaceOrderService implements AggregatorListener<OrderAggregator> {
    @Autowired
    private SagaTemplate<OrderAggregator> orderAggregatorSagaTemplate;

    public TransactionResponse<OrderAggregator> placeOrder(OrderAggregator orderAggregator) {
        return orderAggregatorSagaTemplate.doProcess(orderAggregator,
                ReserveOrder.class);
    }

    @SneakyThrows
    @Override
    @AsyncListener
    public void onEachProcessPerformed(ProcessStack<OrderAggregator> previousProcessStack, OrderAggregator currentAggregate) {
        log.info("PlaceOrderController.onProcessPerformed");
    }

    @Override
    @AsyncListener
    public void onTransactionRevertSuccess(TransactionResponse<OrderAggregator> response) {
        System.out.println("PlaceOrderController.onTransactionSuccess");
    }

}
