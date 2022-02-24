package com.example.executors;

import com.example.MyMicroServices;
import com.example.aggregator.OrderAggregator;
import org.mono.stacksaga.annotation.Executor;
import org.mono.stacksaga.exception.execution.NetworkException;
import org.mono.stacksaga.executor.QueryExecutor;
import org.mono.stacksaga.executor.utils.AggregatorContainer;
import org.mono.stacksaga.executor.utils.ProcessStack;
import org.mono.stacksaga.executor.utils.ProcessStepManager;

@Executor(executeFor = MyMicroServices.STOCK_SERVICE)
public class CheckStockExecutor implements QueryExecutor<OrderAggregator> {
    @Override
    public ProcessStepManager<OrderAggregator> doProcess(ProcessStack<OrderAggregator> processStack, AggregatorContainer<OrderAggregator> aggregate) throws NetworkException {
        OrderAggregator orderAggregator = aggregate.get();
        orderAggregator.setUsername("updated 3");
        orderAggregator.getTmp().setUsername("3ll3l3ll");
        return ProcessStepManager.compete(aggregate);
    }
}
