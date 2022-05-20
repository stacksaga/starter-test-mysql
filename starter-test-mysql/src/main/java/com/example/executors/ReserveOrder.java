package com.example.executors;

import com.example.MyMicroServices;
import com.example.aggregator.OrderAggregator;
import org.mono.stacksaga.RevertHintStore;
import org.mono.stacksaga.annotation.Executor;
import org.mono.stacksaga.common.Resources;
import org.mono.stacksaga.exception.NetworkException;
import org.mono.stacksaga.exception.execution.ExecutorException;
import org.mono.stacksaga.executor.CommandExecutor;
import org.mono.stacksaga.executor.utils.ProcessStack;
import org.mono.stacksaga.executor.utils.ProcessStepManager;

import java.io.IOException;
import java.util.Date;

@Executor(executeFor = MyMicroServices.STOCK_SERVICE)
public class ReserveOrder implements CommandExecutor<OrderAggregator> {

    @Override
    public ProcessStepManager doProcess(ProcessStack<OrderAggregator> previousProcessStack, OrderAggregator currentAggregate) throws Exception {
        currentAggregate.setUpdatedStatus(currentAggregate.getUpdatedStatus() + "ReserveOrder>");
        currentAggregate.setTime(new Date());
        Thread.sleep(5);
        return ProcessStepManager.next(CheckUserExecutor.class);
//        throw new RuntimeException("ReserveOrder UNEXPECTED RuntimeException exception");
    }

    @Override
    public void doRevert(ProcessStack<OrderAggregator> previousProcessStack, ExecutorException executorException, OrderAggregator currentAggregate, RevertHintStore revertHintStore) throws NetworkException {
        /*if (currentAggregate.getType().equals(OrderAggregator.Type.revert_error)) {
            NetworkException ioException = new NetworkException(new IOException("Network exception from ReserveOrder"));
            ioException.printStackTrace();
            throw ioException;
        }*/
        try {
            Thread.sleep(8);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        try {
            if (Resources.Testing.VAL.get() != 2) {
                int val = 1 / 0;
            }
        } catch (Exception e) {
            throw new NetworkException(e);
        }

    }
}
