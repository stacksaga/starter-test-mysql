package com.example.executors;

import com.example.MyMicroServices;
import com.example.aggregator.OrderAggregator;
import org.mono.stacksaga.annotation.Executor;
import org.mono.stacksaga.exception.execution.NetworkException;
import org.mono.stacksaga.executor.QueryExecutor;
import org.mono.stacksaga.executor.utils.ProcessStack;
import org.mono.stacksaga.executor.utils.ProcessStepManager;
import org.mono.stacksaga.executor.utils.AggregatorContainer;

@Executor(executeFor = MyMicroServices.USER_SERVICE)
public class CheckUserExecutor implements QueryExecutor<OrderAggregator> {
    @Override
    public ProcessStepManager<OrderAggregator> doProcess(ProcessStack<OrderAggregator> processStack, AggregatorContainer<OrderAggregator> aggregate) throws NetworkException {
        aggregate.get().getTmp().setUsername("updated 1");
        aggregate.get().setUsername("mafei updated");
        return ProcessStepManager.compete(aggregate);
    }
}