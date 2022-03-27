package com.example.controller;

import com.example.aggregator.OrderAggregator;
import com.example.executors.ReserveOrder;
import com.example.service.PlaceOrderService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.mono.stacksaga.SagaTemplate;
import org.mono.stacksaga.TransactionResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StopWatch;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

@RestController
@Slf4j
public class PlaceOrderController {

    @Autowired
    private PlaceOrderService placeOrderService;


    @GetMapping("/test")
    public ResponseEntity<?> placeOrder() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start("placeOrderProcessComplete");
        OrderAggregator orderAggregator = new OrderAggregator();
        orderAggregator.setUpdatedStatus("INIT_STEP>");
        orderAggregator.setType(OrderAggregator.Type.process_complete);
        TransactionResponse<OrderAggregator> response = placeOrderService.placeOrder(
                orderAggregator
        );
        stopWatch.stop();
        System.out.println("stopWatch " + stopWatch.getLastTaskInfo().getTimeMillis());

        return ResponseEntity.ok(response);
    }
}

