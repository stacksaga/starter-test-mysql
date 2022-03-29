package com.example.service;

import com.example.aggregator.OrderAggregator;
import com.example.executors.ReserveOrder;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mono.stacksaga.TransactionResponse;
import org.mono.stacksaga.executor.utils.ProcessStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.StopWatch;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class PlaceOrderServiceTest {

    @Autowired
    private PlaceOrderService placeOrderService;

    @Test
    void placeOrderProcessComplete() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start("placeOrderProcessComplete");
        OrderAggregator orderAggregator = new OrderAggregator();
        orderAggregator.setUpdatedStatus("INIT_STEP>");
        orderAggregator.setType(OrderAggregator.Type.process_complete);
        orderAggregator.setTime(new Date());
        TransactionResponse<OrderAggregator> response = placeOrderService.placeOrder(
                orderAggregator
        );
        stopWatch.stop();
        System.out.println("PlaceOrderProcessComplete duration : " + stopWatch.getLastTaskInfo().getTimeMillis());
        Assertions.assertEquals(ProcessStatus.PROCESS_COMPLETED, response.getFinalProcessStatus());
    }
}