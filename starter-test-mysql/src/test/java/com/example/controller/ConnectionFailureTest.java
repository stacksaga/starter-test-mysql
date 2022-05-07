package com.example.controller;

import com.example.aggregator.OrderAggregator;
import com.example.executors.ReserveOrder;
import org.apache.commons.lang3.SerializationUtils;
import org.junit.jupiter.api.Test;
import org.mono.stacksaga.SagaTemplate;
import org.mono.stacksaga.TransactionResponse;
import org.mono.stacksaga.common.comiunication.BinaryTransformerEntity;
import org.mono.stacksaga.core.service.ExecutorBinaryFileService;
import org.mono.stacksaga.db.service.TransactionService;
import org.mono.stacksaga.db.service.impl.mysql.InvokableChuckExecutionInvokerServiceMysqlImpl;
import org.mono.stacksaga.exception.NetworkException;
import org.mono.stacksaga.exception.execution.UnHandledException;
import org.mono.stacksaga.redis.publisher.ExecutorBinaryTransformerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.io.Serializable;
import java.sql.SQLException;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ConnectionFailureTest {

    @Autowired
    private TransactionService transactionService;

    @Autowired
    private InvokableChuckExecutionInvokerServiceMysqlImpl invokerServiceMysql;

    @Autowired
    private SagaTemplate<OrderAggregator> orderAggregatorSagaTemplate;

    @Autowired
    private ExecutorBinaryFileService executorBinaryFileService;

    @Test
    void invoke() {
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
            System.out.println("e = " + e);
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
        }
    }
}
