package com.example.executors.sub;

import com.example.MyMicroServices;
import com.example.aggregator.OrderAggregator;
import org.mono.stacksaga.RevertHintStore;
import org.mono.stacksaga.annotation.Executor;
import org.mono.stacksaga.exception.NetworkException;
import org.mono.stacksaga.executor.RevertExecutor;
import org.mono.stacksaga.executor.utils.ProcessStack;

import java.util.Date;

@Executor(executeFor = MyMicroServices.LOG_SERVICE)
public class MakePaymentLogUpdateAfter implements RevertExecutor<OrderAggregator> {
    @Override
    public void doProcess(ProcessStack<OrderAggregator> previousProcessStack, Exception processException, RevertHintStore revertHintStore) throws NetworkException {
        System.out.println("MakePaymentLogUpdateAfter.doProcess");
        revertHintStore.put("MakePaymentLogUpdateAfter", new Date());

    }
}
