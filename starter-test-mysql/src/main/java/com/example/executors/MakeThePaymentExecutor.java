package com.example.executors;

import com.example.MyMicroServices;
import com.example.aggregator.OrderAggregator;
import com.example.executors.sub.MakePaymentLogUpdate;
import com.example.ms.customerwalletservice.entity.CustomerWalletEntity;
import com.example.ms.customerwalletservice.repository.CustomerWalletRepository;
import lombok.AllArgsConstructor;
import org.mono.stacksaga.RevertHintStore;
import org.mono.stacksaga.annotation.Executor;
import org.mono.stacksaga.annotation.RevertBefore;
import org.mono.stacksaga.annotation.RevertExecutorConfig;
import org.mono.stacksaga.exception.execution.ExecutorException;
import org.mono.stacksaga.executor.CommandExecutor;
import org.mono.stacksaga.executor.utils.AggregatorContainer;
import org.mono.stacksaga.executor.utils.ProcessStack;
import org.mono.stacksaga.executor.utils.ProcessStepManager;

import java.io.IOException;
import java.util.Date;
import java.util.Optional;

@Executor(executeFor = MyMicroServices.WALLET_SERVICE)
@AllArgsConstructor
public class MakeThePaymentExecutor implements CommandExecutor<OrderAggregator> {
    private final CustomerWalletRepository customerWalletRepository;

    @Override
    public ProcessStepManager<OrderAggregator> doProcess(ProcessStack<OrderAggregator> previousProcessStack, AggregatorContainer<OrderAggregator> currentAggregate) {
        System.out.println("MakeThePaymentExecutor.doProcess");
        currentAggregate.get().setUpdatedStatus(currentAggregate.get().getUpdatedStatus() + "MakeThePaymentExecutor>");
        currentAggregate.get().setTime(new Date());
        return ProcessStepManager.next(AddCustomerOrderExecutor.class, currentAggregate);
    }

    @Override
    @RevertBefore(executors = {
            @RevertExecutorConfig(order = 1, revertExecutor = MakePaymentLogUpdate.class)
    })
    public void doRevert(ProcessStack<OrderAggregator> previousProcessStack, ExecutorException executorException, RevertHintStore revertHintStore) throws IOException {
        System.out.println("MakeThePaymentExecutor.doRevert");
        revertHintStore.put("MakeThePaymentExecutor", new Date());
        OrderAggregator aggregateStateIn = previousProcessStack.getExecutedExecutorData(0).getProcess().getAggregateStateIn();
        if (aggregateStateIn.getType().equals(OrderAggregator.Type.revert_error)) {
            throw new IOException("revert error from MakeThePaymentExecutor");
        }
    }
}
