package com.example.executors;

import com.example.MyMicroServices;
import com.example.aggregator.OrderAggregator;
import com.example.executors.sub.MakePaymentLogUpdate;
import com.example.executors.sub.MakePaymentLogUpdateAfter;
import com.example.executors.sub.PaymentNotifySubExecutor;
import com.example.ms.customerwalletservice.repository.CustomerWalletRepository;
import lombok.AllArgsConstructor;
import org.mono.stacksaga.RevertHintStore;
import org.mono.stacksaga.annotation.Executor;
import org.mono.stacksaga.annotation.RevertAfter;
import org.mono.stacksaga.annotation.RevertBefore;
import org.mono.stacksaga.annotation.RevertExecutorConfig;
import org.mono.stacksaga.common.Resources;
import org.mono.stacksaga.exception.NetworkException;
import org.mono.stacksaga.exception.execution.ExecutorException;
import org.mono.stacksaga.executor.CommandExecutor;
import org.mono.stacksaga.executor.utils.ProcessStack;
import org.mono.stacksaga.executor.utils.ProcessStepManager;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.util.Date;

@Executor(executeFor = MyMicroServices.WALLET_SERVICE)
@AllArgsConstructor
public class MakeThePaymentExecutor implements CommandExecutor<OrderAggregator> {
    private final CustomerWalletRepository customerWalletRepository;

    @Override
    public ProcessStepManager doProcess(ProcessStack<OrderAggregator> previousProcessStack, OrderAggregator currentAggregate) throws Exception {
        System.out.println("MakeThePaymentExecutor.doProcess");
        currentAggregate.setUpdatedStatus(currentAggregate.getUpdatedStatus() + "MakeThePaymentExecutor>");
        currentAggregate.setTime(new Date());
        Thread.sleep(5);
        return ProcessStepManager.next(AddCustomerOrderExecutor.class);
/*        if (Resources.Testing.VAL.get() == 1) {
            throw new NetworkException(new RuntimeException("network failed[1]"));
        } else {
            return ProcessStepManager.next(AddCustomerOrderExecutor.class);
        }*/
    }

    @Override
    @RevertBefore(executors = {
            @RevertExecutorConfig(order = 1, revertExecutor = MakePaymentLogUpdate.class),
            @RevertExecutorConfig(order = 2, revertExecutor = PaymentNotifySubExecutor.class),
    })
    @RevertAfter(executors = {
            @RevertExecutorConfig(order = 1, revertExecutor = MakePaymentLogUpdateAfter.class)
    })
    public void doRevert(ProcessStack<OrderAggregator> previousProcessStack, ExecutorException executorException, OrderAggregator currentAggregate, RevertHintStore revertHintStore) throws NetworkException {
        System.out.println("MakeThePaymentExecutor.doRevert");
        revertHintStore.put("MakeThePaymentExecutor", new Date());
        try {
            Thread.sleep(9);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

//        throw new NetworkException(new RuntimeException(" other error from MakePaymentLogUpdate:doRevert"));

/*        if (currentAggregate.getType().equals(OrderAggregator.Type.revert_error)) {
            IOException ioException = new IOException("revert error from MakeThePaymentExecutor");
            ioException.printStackTrace();
            throw ioException;
        }*/
    }
}
