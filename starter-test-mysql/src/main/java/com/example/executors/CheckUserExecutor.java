package com.example.executors;

import com.example.MyMicroServices;
import com.example.aggregator.OrderAggregator;
import com.example.ms.userservice.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.mono.stacksaga.annotation.Executor;
import org.mono.stacksaga.executor.QueryExecutor;
import org.mono.stacksaga.executor.utils.ProcessStack;
import org.mono.stacksaga.executor.utils.ProcessStepManager;

import java.util.Date;

@AllArgsConstructor
@Executor(executeFor = MyMicroServices.ORDER_SERVICE)
public class CheckUserExecutor implements QueryExecutor<OrderAggregator> {

    private final UserRepository repository;

    @Override
    public ProcessStepManager doProcess(ProcessStack<OrderAggregator> processStack, OrderAggregator aggregate) {
        System.out.println("CheckUserExecutor.doProcess");

        aggregate.setUpdatedStatus(aggregate.getUpdatedStatus() + "CheckUserExecutor>");
        aggregate.setTime(new Date());
        return ProcessStepManager.next(CheckAmountForPayment.class);
    }
}