package com.example.executors;

import com.example.MyMicroServices;
import com.example.aggregator.OrderAggregator;
import org.mono.stacksaga.RevertHintStore;
import org.mono.stacksaga.annotation.Executor;
import org.mono.stacksaga.exception.execution.ExecutorException;
import org.mono.stacksaga.executor.CommandExecutor;
import org.mono.stacksaga.executor.utils.AggregatorContainer;
import org.mono.stacksaga.executor.utils.ProcessStack;
import org.mono.stacksaga.executor.utils.ProcessStepManager;

import java.io.IOException;
import java.util.Date;

@Executor(executeFor = MyMicroServices.STOCK_SERVICE)
public class ReserveOrder implements CommandExecutor<OrderAggregator> {

    @Override
    public ProcessStepManager<OrderAggregator> doProcess(ProcessStack<OrderAggregator> previousProcessStack, AggregatorContainer<OrderAggregator> currentAggregate) {
        currentAggregate.get().setUpdatedStatus(currentAggregate.get().getUpdatedStatus() + "ReserveOrder>");
        currentAggregate.get().setTime(new Date());
        return ProcessStepManager.next(CheckUserExecutor.class, currentAggregate);
    }

    @Override
    public void doRevert(ProcessStack<OrderAggregator> previousProcessStack, ExecutorException executorException, RevertHintStore revertHintStore) throws IOException {
        if (previousProcessStack.getExecutedExecutorData(0).getProcess().getAggregateStateIn().getType().equals(OrderAggregator.Type.revert_error)) {
            throw new IOException("Network exception from ReserveOrder");
        }
    }
}
