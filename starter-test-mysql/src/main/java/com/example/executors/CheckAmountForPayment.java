package com.example.executors;

import com.example.MyMicroServices;
import com.example.aggregator.OrderAggregator;
import com.example.ms.customerwalletservice.entity.CustomerWalletEntity;
import com.example.ms.customerwalletservice.repository.CustomerWalletRepository;
import lombok.AllArgsConstructor;
import org.mono.stacksaga.annotation.Executor;
import org.mono.stacksaga.exception.execution.ExecutorException;
import org.mono.stacksaga.executor.QueryExecutor;
import org.mono.stacksaga.executor.utils.AggregatorContainer;
import org.mono.stacksaga.executor.utils.ProcessStack;
import org.mono.stacksaga.executor.utils.ProcessStepManager;

import java.util.Date;
import java.util.Optional;

@Executor(executeFor = MyMicroServices.PAYMENT_SERVICE)
@AllArgsConstructor
public class CheckAmountForPayment implements QueryExecutor<OrderAggregator> {
    private final CustomerWalletRepository customerWalletRepository;

    @Override
    public ProcessStepManager<OrderAggregator> doProcess(ProcessStack<OrderAggregator> processStack, AggregatorContainer<OrderAggregator> aggregate) {
        System.out.println("CheckAmountForPayment.doProcess");
        aggregate.get().setUpdatedStatus(aggregate.get().getUpdatedStatus() + "CheckAmountForPayment>");
        aggregate.get().setTime(new Date());
        return ProcessStepManager.next(MakeThePaymentExecutor.class, aggregate);

    }
}
