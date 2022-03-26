package com.example.executors;

import com.example.MyMicroServices;
import com.example.aggregator.OrderAggregator;
import org.mono.stacksaga.RevertHintStore;
import org.mono.stacksaga.annotation.Executor;
import org.mono.stacksaga.exception.execution.ExecutorException;
import org.mono.stacksaga.executor.CommandExecutor;
import org.mono.stacksaga.executor.utils.ProcessStack;
import org.mono.stacksaga.executor.utils.ProcessStepManager;

import java.io.IOException;
import java.util.Date;

@Executor(executeFor = MyMicroServices.STOCK_SERVICE)
public class ReserveOrder implements CommandExecutor<OrderAggregator> {

    @Override
    public ProcessStepManager doProcess(ProcessStack<OrderAggregator> previousProcessStack, OrderAggregator currentAggregate) {

        currentAggregate.setUpdatedStatus(currentAggregate.getUpdatedStatus() + "ReserveOrder>");
        currentAggregate.setTime(new Date());
        return ProcessStepManager.next(CheckUserExecutor.class);
    }

    @Override
    public void doRevert(ProcessStack<OrderAggregator> previousProcessStack, ExecutorException executorException, OrderAggregator currentAggregate, RevertHintStore revertHintStore) throws IOException {
        if (currentAggregate.getType().equals(OrderAggregator.Type.revert_error)) {
            throw new IOException("Network exception from ReserveOrder");
        }
    }
}
