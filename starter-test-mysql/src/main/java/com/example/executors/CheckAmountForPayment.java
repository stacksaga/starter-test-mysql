package com.example.executors;

import com.example.MyMicroServices;
import com.example.aggregator.OrderAggregator;
import com.example.ms.customerwalletservice.repository.CustomerWalletRepository;
import lombok.AllArgsConstructor;
import org.mono.stacksaga.annotation.Executor;
import org.mono.stacksaga.exception.NetworkException;
import org.mono.stacksaga.executor.QueryExecutor;
import org.mono.stacksaga.executor.utils.ProcessStack;
import org.mono.stacksaga.executor.utils.ProcessStepManager;

import java.io.IOException;
import java.util.Date;

import static org.mono.stacksaga.common.Resources.Testing.DO_PROCESS_COUNT;

@Executor(executeFor = MyMicroServices.PAYMENT_SERVICE)
@AllArgsConstructor
public class CheckAmountForPayment implements QueryExecutor<OrderAggregator> {
    private final CustomerWalletRepository customerWalletRepository;

    @Override
    public ProcessStepManager doProcess(ProcessStack<OrderAggregator> processStack, OrderAggregator aggregate) throws Exception {
        DO_PROCESS_COUNT.incrementAndGet();
        System.out.println("CheckAmountForPayment.doProcess");
        aggregate.setUpdatedStatus(aggregate.getUpdatedStatus() + "CheckAmountForPayment>");
        aggregate.setTime(new Date());
        return ProcessStepManager.next(MakeThePaymentExecutor.class);

    }
}
