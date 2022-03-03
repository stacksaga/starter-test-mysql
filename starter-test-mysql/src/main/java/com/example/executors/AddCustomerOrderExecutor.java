package com.example.executors;

import com.example.MyMicroServices;
import com.example.aggregator.OrderAggregator;
import com.example.ms.orderservice.entity.CustomerOrderEntity;
import com.example.ms.orderservice.repository.CustomerOrderRepository;
import lombok.AllArgsConstructor;
import org.mono.stacksaga.RevertHintStore;
import org.mono.stacksaga.annotation.Executor;
import org.mono.stacksaga.exception.execution.ExecutorException;
import org.mono.stacksaga.executor.CommandExecutor;
import org.mono.stacksaga.executor.utils.AggregatorContainer;
import org.mono.stacksaga.executor.utils.ProcessStack;
import org.mono.stacksaga.executor.utils.ProcessStepManager;

import java.util.Date;

@Executor(executeFor = MyMicroServices.USER_SERVICE)
@AllArgsConstructor
public class AddCustomerOrderExecutor implements CommandExecutor<OrderAggregator> {
    private final CustomerOrderRepository customerOrderRepository;

    @Override
    public ProcessStepManager<OrderAggregator> doProcess(ProcessStack<OrderAggregator> processStack, AggregatorContainer<OrderAggregator> aggregate) {
        System.out.println("AddCustomerOrderExecutor.doProcess");
        aggregate.get().setUpdatedStatus(aggregate.get().getUpdatedStatus() + "AddCustomerOrderExecutor>");
        aggregate.get().setTime(new Date());
        OrderAggregator aggregateStateIn = processStack.getExecutedExecutorData(0).getProcess().getAggregateStateIn();
        if (aggregateStateIn.getType().equals(OrderAggregator.Type.process_complete)) {
            return ProcessStepManager.compete(aggregate);
        } else {
            return ProcessStepManager.revert(aggregate, ExecutorException.buildProcessException(new RuntimeException("error from AddCustomerOrderExecutor")));
        }
    }

    @Override
    public void doRevert(ProcessStack<OrderAggregator> previousProcessStack, ExecutorException executorException, RevertHintStore revertHintStore) {
        System.out.println("AddCustomerOrderExecutor.doRevert");
        revertHintStore.put("AddCustomerOrderExecutor", new Date());
    }
}
