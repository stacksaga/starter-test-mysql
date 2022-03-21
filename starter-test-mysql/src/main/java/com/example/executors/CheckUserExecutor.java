package com.example.executors;

import com.example.MyMicroServices;
import com.example.aggregator.OrderAggregator;
import com.example.ms.userservice.entity.UserEntity;
import com.example.ms.userservice.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.mono.stacksaga.annotation.Executor;
import org.mono.stacksaga.exception.execution.ExecutorException;
import org.mono.stacksaga.executor.QueryExecutor;
import org.mono.stacksaga.executor.utils.ProcessStack;
import org.mono.stacksaga.executor.utils.ProcessStepManager;
import org.mono.stacksaga.executor.utils.AggregatorContainer;

import java.util.Date;
import java.util.Optional;

@AllArgsConstructor
@Executor(executeFor = MyMicroServices.ORDER_SERVICE)
public class CheckUserExecutor implements QueryExecutor<OrderAggregator> {

    private final UserRepository repository;

    @Override
    public ProcessStepManager<OrderAggregator> doProcess(ProcessStack<OrderAggregator> processStack, AggregatorContainer<OrderAggregator> aggregate) {
        System.out.println("CheckUserExecutor.doProcess");
        aggregate.get().setUpdatedStatus(aggregate.get().getUpdatedStatus() + "CheckUserExecutor>");
        aggregate.get().setTime(new Date());
        return ProcessStepManager.next(CheckAmountForPayment.class, aggregate);
    }
}