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
import org.mono.stacksaga.mysql.config.MysqlDatabaseConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StopWatch;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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


    @GetMapping("/sql")
    public ResponseEntity<?> test() {
        try (Connection connection = MysqlDatabaseConfiguration.getConnection()) {
            connection.setReadOnly(true);
            connection.setAutoCommit(true);
            PreparedStatement preparedStatement = connection.prepareStatement("select now() as test_date");
            ResultSet resultSet = preparedStatement.executeQuery();
            resultSet.next();
                System.out.println("test_date 1 = " + resultSet.getString("test_date"));
            PreparedStatement preparedStatement2 = connection.prepareStatement("select now() as test_date");
            ResultSet resultSet2 = preparedStatement2.executeQuery();
            resultSet2.next();
            System.out.println("test_date 2 = " + resultSet2.getString("test_date"));
            return ResponseEntity.ok().build();
        } catch (SQLException e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }


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

