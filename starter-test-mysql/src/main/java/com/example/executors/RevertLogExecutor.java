package com.example.executors;


import com.example.MyMicroServices;
import com.example.aggregator.OrderAggregator;
import org.mono.stacksaga.SagaAggregate;
import org.mono.stacksaga.annotation.Executor;
import org.mono.stacksaga.exception.execution.ProcessException;
import org.mono.stacksaga.exception.execution.RevertException;
import org.mono.stacksaga.executor.RevertExecutor;
import org.mono.stacksaga.executor.SagaExecutor;
import org.mono.stacksaga.executor.utils.ExecutorExecutedData;

import java.util.Map;

@Executor(executeFor = MyMicroServices.LOG_SERVICE)
public class RevertLogExecutor implements RevertExecutor<OrderAggregator> {
    @Override
    public void doProcess(Map<Class<? extends SagaExecutor<? extends SagaAggregate>>, ExecutorExecutedData<OrderAggregator>> aggregateStateHistory, ProcessException processException) throws RevertException {
        // TODO document why this method is empty
    }
}
