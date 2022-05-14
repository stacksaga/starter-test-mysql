package com.example.controller;

import com.example.aggregator.OrderAggregator;
import com.example.executors.ReserveOrder;
import com.example.service.PlaceOrderService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.SerializationUtils;
import org.mono.stacksaga.SagaTemplate;
import org.mono.stacksaga.TransactionResponse;
import org.mono.stacksaga.cb.CircuitBreakerBroker;
import org.mono.stacksaga.common.Resources;
import org.mono.stacksaga.common.comiunication.BinaryTransformerEntity;
import org.mono.stacksaga.core.SagaRevertEngineInvoker;
import org.mono.stacksaga.core.service.ExecutorBinaryFileService;
import org.mono.stacksaga.db.entity.RelatedServiceEntity;
import org.mono.stacksaga.db.service.RelatedServiceService;
import org.mono.stacksaga.db.service.impl.mysql.InvokableChuckExecutionInvokerServiceMysqlImpl;
import org.mono.stacksaga.exception.NetworkException;
import org.mono.stacksaga.exception.ProcessStoppedWithGarbageException;
import org.mono.stacksaga.exception.execution.UnHandledException;
import org.mono.stacksaga.mysql.config.MysqlDatabaseConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StopWatch;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

@RestController
@Slf4j
public class PlaceOrderController {
    @Autowired
    private SagaTemplate<OrderAggregator> orderAggregatorSagaTemplate;

    @Autowired
    private PlaceOrderService placeOrderService;
    @Autowired
    private RelatedServiceService relatedServiceService;
    @Autowired
    private CircuitBreakerBroker circuitBreakerBroker;
    @Autowired
    private SagaRevertEngineInvoker sagaRevertEngineInvoker;


    @Autowired
    private ExecutorBinaryFileService executorBinaryFileService;

    @Autowired
    private InvokableChuckExecutionInvokerServiceMysqlImpl invokerServiceMysql;

    @GetMapping("/test")
    public ResponseEntity<?> placeOrder() {

        Resources.Testing.VAL.set(1);
        OrderAggregator orderAggregator = new OrderAggregator();
        orderAggregator.setUpdatedStatus("INIT_STEP>");
        orderAggregator.setType(OrderAggregator.Type.revert_complete);
        try {
            TransactionResponse<OrderAggregator> response = orderAggregatorSagaTemplate.process(
                    orderAggregator,
                    ReserveOrder.class
            );
            System.out.println("response = " + response);

        } catch (NetworkException e) {
//            System.out.println("e = " + e);
            e.printStackTrace();
        } catch (UnHandledException e) {
            e.printStackTrace();
        } finally {
            executorBinaryFileService.getFiles().forEach(file -> {
                try {
                    byte[] fileData = executorBinaryFileService.getFileAsByteArray(file);
                    BinaryTransformerEntity binaryTransformerEntity = SerializationUtils.deserialize(fileData);
                    if (invokerServiceMysql.invoke(binaryTransformerEntity)) {
                        executorBinaryFileService.deleteFile(binaryTransformerEntity);
                    }
                } catch (IOException | SQLException e) {
                    e.printStackTrace();
                }
            });

            Resources.Testing.VAL.set(2);
            Optional<RelatedServiceEntity> transactionRevertReasonRelatedServiceUid =
                    relatedServiceService.getTransactionRevertReasonRelatedServiceUid(
                            orderAggregator.getAggregateTransactionId());
            transactionRevertReasonRelatedServiceUid.ifPresent(relatedServiceEntity -> {
                if (circuitBreakerBroker.updateListerServiceAvailability(relatedServiceEntity.getService_name(), true)) {
                    String retryFootnote = UUID.randomUUID().toString();
                    sagaRevertEngineInvoker.invokeRevertEngine(relatedServiceEntity.getService_name(), 1, retryFootnote);
                }
            });

          /*  transactionRevertReasonRelatedServiceUid.ifPresent(relatedServiceEntity -> {
                if (circuitBreakerBroker.updateListerServiceAvailability(relatedServiceEntity.getService_name(), true)) {
                    String retryFootnote = UUID.randomUUID().toString();
                    sagaRevertEngineInvoker.invokeRevertEngine(relatedServiceEntity.getService_name(), 1, retryFootnote);
                }
            });
            transactionRevertReasonRelatedServiceUid.ifPresent(relatedServiceEntity -> {
                if (circuitBreakerBroker.updateListerServiceAvailability(relatedServiceEntity.getService_name(), true)) {
                    String retryFootnote = UUID.randomUUID().toString();
                    sagaRevertEngineInvoker.invokeRevertEngine(relatedServiceEntity.getService_name(), 1, retryFootnote);
                }
            });
*/

        }

        return ResponseEntity.ok(ThreadLocalRandom.current().nextInt(0, 100));
    }
}

