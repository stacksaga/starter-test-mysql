package com.example.executors.sub;

import com.example.MyMicroServices;
import com.example.aggregator.OrderAggregator;
import org.mono.stacksaga.RevertHintStore;
import org.mono.stacksaga.annotation.Executor;
import org.mono.stacksaga.exception.NetworkException;
import org.mono.stacksaga.executor.RevertExecutor;
import org.mono.stacksaga.executor.utils.ProcessStack;

@Executor(executeFor = MyMicroServices.LOG_SERVICE)
// TODO: 5/18/2022 check wha to do for coming error when there is a revert executor and it has not annotated as a sub executor.
public class PaymentNotifySubExecutor implements RevertExecutor<OrderAggregator> {
    @Override
    public void doProcess(ProcessStack<OrderAggregator> previousProcessStack, Exception processException, RevertHintStore revertHintStore) throws NetworkException {

    }
}
