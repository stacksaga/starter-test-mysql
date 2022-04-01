package com.example.controller;

import com.example.aggregator.OrderAggregator;
import com.example.executors.ReserveOrder;
import com.example.service.PlaceOrderService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.mono.stacksaga.SagaTemplate;
import org.mono.stacksaga.TransactionResponse;
import org.mono.stacksaga.cb.CircuitBreakerBroker;
import org.mono.stacksaga.core.SagaRevertEngineInvoker;
import org.mono.stacksaga.db.entity.RelatedServiceEntity;
import org.mono.stacksaga.db.service.RelatedServiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StopWatch;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.Optional;

@RestController
@Slf4j
public class PlaceOrderController {

    @Autowired
    private PlaceOrderService placeOrderService;
    @Autowired
    private RelatedServiceService relatedServiceService;
    @Autowired
    private CircuitBreakerBroker circuitBreakerBroker;
    @Autowired
    private SagaRevertEngineInvoker sagaRevertEngineInvoker;


    @GetMapping("/test")
    public ResponseEntity<?> placeOrder() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start("placeOrderProcessComplete");
        OrderAggregator orderAggregator = new OrderAggregator();
        orderAggregator.setUpdatedStatus("INIT_STEP>");
        orderAggregator.setType(OrderAggregator.Type.revert_error);
        TransactionResponse<OrderAggregator> response = placeOrderService.placeOrder(
                orderAggregator
        );
        stopWatch.stop();
        System.out.println("stopWatch " + stopWatch.getLastTaskInfo().getTimeMillis());
        Optional<RelatedServiceEntity> transactionRevertReasonRelatedServiceUid =
                relatedServiceService.getTransactionRevertReasonRelatedServiceUid(
                        response.getAggregate().getAggregateTransactionId());
        transactionRevertReasonRelatedServiceUid.ifPresent(relatedServiceEntity -> {
            if (circuitBreakerBroker.updateListerServiceAvailability(relatedServiceEntity.getService_name(), true)) {
                sagaRevertEngineInvoker.invokeRevertEngine(relatedServiceEntity.getService_name(), 1);
            }
        });
        return ResponseEntity.ok(response);
    }
}

