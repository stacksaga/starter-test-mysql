package com.example.executors;

import com.example.MyMicroServices;
import com.example.aggregator.OrderAggregator;
import lombok.AllArgsConstructor;
import org.mono.stacksaga.RevertHintStore;
import org.mono.stacksaga.annotation.Executor;
import org.mono.stacksaga.exception.execution.ExecutorException;
import org.mono.stacksaga.executor.CommandExecutor;
import org.mono.stacksaga.executor.utils.ProcessStack;
import org.mono.stacksaga.executor.utils.ProcessStepManager;

import java.util.Date;

@Executor(executeFor = MyMicroServices.USER_SERVICE)
@AllArgsConstructor
public class AddCustomerOrderExecutor implements CommandExecutor<OrderAggregator> {
//    private final CustomerOrderRepository customerOrderRepository;

    @Override
    public ProcessStepManager doProcess(ProcessStack<OrderAggregator> processStack, OrderAggregator aggregate) {
        System.out.println("AddCustomerOrderExecutor.doProcess");
        aggregate.setUpdatedStatus(aggregate.getUpdatedStatus() + "AddCustomerOrderExecutor>");
        aggregate.setTime(new Date());
        if (aggregate.getType().equals(OrderAggregator.Type.process_complete)) {
            return ProcessStepManager.compete();
        } else {
            return ProcessStepManager.revert(ExecutorException.buildProcessException(new RuntimeException("error from AddCustomerOrderExecutor")));
        }
    }

    @Override
    public void doRevert(ProcessStack<OrderAggregator> previousProcessStack, ExecutorException executorException, OrderAggregator aggregate, RevertHintStore revertHintStore) {
        System.out.println("AddCustomerOrderExecutor.doRevert");
        revertHintStore.put("AddCustomerOrderExecutor", new Date());
    }
}
