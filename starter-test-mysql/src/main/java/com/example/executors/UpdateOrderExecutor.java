package com.example.executors;

import com.example.MyMicroServices;
import com.example.aggregator.OrderAggregator;
import org.mono.stacksaga.RevertHintStore;
import org.mono.stacksaga.annotation.Executor;
import org.mono.stacksaga.exception.execution.NetworkException;
import org.mono.stacksaga.exception.execution.ProcessException;
import org.mono.stacksaga.exception.execution.RevertException;
import org.mono.stacksaga.executor.CommandExecutor;
import org.mono.stacksaga.executor.utils.ProcessStack;
import org.mono.stacksaga.executor.utils.ProcessStepManager;
import org.mono.stacksaga.executor.utils.Wrapper;

@Executor(executeFor = MyMicroServices.ORDER_SERVICE)
public class UpdateOrderExecutor implements CommandExecutor<OrderAggregator> {

    @Override
    public ProcessStepManager<OrderAggregator> doProcess(ProcessStack<OrderAggregator> processStack, Wrapper<OrderAggregator> aggregate) throws NetworkException {
        return null;
    }

    @Override
    public void doRevert(ProcessStack<OrderAggregator> finalProcessStack, ProcessException processException, RevertHintStore revertHintStore) throws RevertException {
        // TODO document why this method is empty
    }
}
