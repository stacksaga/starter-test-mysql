package com.example.executors;

import com.example.MyMicroServices;
import com.example.aggregator.OrderAggregator;
import com.example.ms.customerwalletservice.repository.CustomerWalletRepository;
import lombok.AllArgsConstructor;
import org.mono.stacksaga.annotation.Executor;
import org.mono.stacksaga.executor.QueryExecutor;
import org.mono.stacksaga.executor.utils.ProcessStack;
import org.mono.stacksaga.executor.utils.ProcessStepManager;

import java.util.Date;

@Executor(executeFor = MyMicroServices.PAYMENT_SERVICE)
@AllArgsConstructor
public class CheckAmountForPayment implements QueryExecutor<OrderAggregator> {
    private final CustomerWalletRepository customerWalletRepository;

    @Override
    public ProcessStepManager doProcess(ProcessStack<OrderAggregator> processStack, OrderAggregator aggregate) throws Exception {
        System.out.println("CheckAmountForPayment.doProcess");
        aggregate.setUpdatedStatus(aggregate.getUpdatedStatus() + "CheckAmountForPayment>");
        aggregate.setTime(new Date());
        Thread.sleep(2);

        return ProcessStepManager.next(MakeThePaymentExecutor.class);

    }
}
