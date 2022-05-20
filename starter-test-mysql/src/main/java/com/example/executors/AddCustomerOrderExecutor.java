package com.example.executors;

import com.example.MyMicroServices;
import com.example.aggregator.OrderAggregator;
import lombok.AllArgsConstructor;
import org.mono.stacksaga.RevertHintStore;
import org.mono.stacksaga.annotation.Executor;
import org.mono.stacksaga.exception.NetworkException;
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
    public ProcessStepManager doProcess(ProcessStack<OrderAggregator> processStack, OrderAggregator aggregate) throws Exception {
        System.out.println("AddCustomerOrderExecutor.doProcess");
        aggregate.setUpdatedStatus(aggregate.getUpdatedStatus() + "AddCustomerOrderExecutor>");
        aggregate.setTime(new Date());
        Thread.sleep(50);

        if (aggregate.getType().equals(OrderAggregator.Type.process_complete)) {
            return ProcessStepManager.compete();
        } else {
            throw new RuntimeException(" other error from AddCustomerOrderExecutor");
//            throw new NetworkException(new RuntimeException("NetworkException error from AddCustomerOrderExecutor"));
        }
    }

    @Override
    public void doRevert(ProcessStack<OrderAggregator> previousProcessStack, ExecutorException executorException, OrderAggregator aggregate, RevertHintStore revertHintStore) {
        System.out.println("AddCustomerOrderExecutor.doRevert");
        revertHintStore.put("AddCustomerOrderExecutor", new Date());
        throw new RuntimeException(" other error from AddCustomerOrderExecutor:doRevert");

    }
}
