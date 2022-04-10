package com.example.service;

import com.example.aggregator.OrderAggregator;
import com.example.executors.ReserveOrder;
import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mono.stacksaga.TransactionResponse;
import org.mono.stacksaga.common.Resources;
import org.mono.stacksaga.executor.utils.ProcessStatus;
import org.mono.stacksaga.redis.template.BinaryBinaryRedisTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.StopWatch;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class PlaceOrderServiceTest {

    @Autowired
    private PlaceOrderService placeOrderService;

    @Autowired
    private BinaryBinaryRedisTemplate binaryBinaryRedisTemplate;


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

    @Test
    void testUploadBigfile() throws IOException {
        byte[] channel = "test-c".getBytes();
        byte[] bytes = FileUtils.readFileToByteArray(new File("C:\\Users\\kalha\\Downloads\\test.zip"));
        System.out.println("length > " + bytes.length);
        Long publish = Objects.requireNonNull(binaryBinaryRedisTemplate.getTemplate().getConnectionFactory())
                .getConnection()
                .publish(channel, bytes);
        System.out.println("publish > " + publish);
    }
}