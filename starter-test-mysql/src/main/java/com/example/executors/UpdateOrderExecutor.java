package com.example.executors;

import com.example.MyMicroServices;
import com.example.aggregator.OrderAggregator;
import org.mono.stacksaga.RevertHintStore;
import org.mono.stacksaga.SagaAggregate;
import org.mono.stacksaga.annotation.Executor;
import org.mono.stacksaga.annotation.RevertAfter;
import org.mono.stacksaga.annotation.RevertBefore;
import org.mono.stacksaga.annotation.RevertExecutorConfig;
import org.mono.stacksaga.exception.execution.NetworkException;
import org.mono.stacksaga.exception.execution.ProcessException;
import org.mono.stacksaga.exception.execution.RevertException;
import org.mono.stacksaga.executor.CommandExecutor;
import org.mono.stacksaga.executor.RevertExecutor;
import org.mono.stacksaga.executor.SagaExecutor;
import org.mono.stacksaga.executor.utils.ExecutorExecutedData;
import org.mono.stacksaga.executor.utils.ProcessStack;
import org.mono.stacksaga.executor.utils.ProcessStepManager;
import org.mono.stacksaga.executor.utils.AggregatorContainer;

import java.util.Map;

@Executor(executeFor = MyMicroServices.ORDER_SERVICE)
public class UpdateOrderExecutor implements CommandExecutor<OrderAggregator> {

    @Override
    public ProcessStepManager<OrderAggregator> doProcess(ProcessStack<OrderAggregator> processStack, AggregatorContainer<OrderAggregator> aggregate) throws NetworkException {
        return null;
    }

    @Override
    @RevertAfter(executors = {
            @RevertExecutorConfig(order = 1, revertExecutor = Revert1.class),
            @RevertExecutorConfig(order = 2, revertExecutor = Revert2.class),
    })
    @RevertBefore(executors = {
            @RevertExecutorConfig(order = 0, revertExecutor = Revert3.class)
    })
    public void doRevert(ProcessStack<OrderAggregator> finalProcessStack, ProcessException processException, RevertHintStore revertHintStore) throws RevertException {
        // TODO document why this method is empty
    }

    @Executor(executeFor = "revert-service1")
    public static class Revert1 implements RevertExecutor<OrderAggregator> {

        @Override
        public void doProcess(Map<Class<? extends SagaExecutor<? extends SagaAggregate>>, ExecutorExecutedData<OrderAggregator>> aggregateStateHistory, ProcessException processException) throws RevertException {

        }
    }

    @Executor(executeFor = "revert-service1")
    public static class Revert2 implements RevertExecutor<OrderAggregator> {

        @Override
        public void doProcess(Map<Class<? extends SagaExecutor<? extends SagaAggregate>>, ExecutorExecutedData<OrderAggregator>> aggregateStateHistory, ProcessException processException) throws RevertException {

        }
    }

    @Executor(executeFor = "revert-service2")
    public static class Revert3 implements RevertExecutor<OrderAggregator> {

        @Override
        public void doProcess(Map<Class<? extends SagaExecutor<? extends SagaAggregate>>, ExecutorExecutedData<OrderAggregator>> aggregateStateHistory, ProcessException processException) throws RevertException {

        }
    }


}
