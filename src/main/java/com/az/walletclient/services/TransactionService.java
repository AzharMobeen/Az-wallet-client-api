package com.az.walletclient.services;

import com.az.wallet.server.proto.*;
import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.StatusRuntimeException;
import lombok.extern.slf4j.Slf4j;
import org.checkerframework.checker.nullness.compatqual.NullableDecl;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import java.util.concurrent.CompletableFuture;

/**
 * @author - Azhar Mobeen
 *
 *  Description:
 *  =>  This class is actually sending evey request to GRPC server by WalletServerServiceFutureStub for concurrency.
 *  =>  Server will send response as ListenableFuture<WalletResponse> and then I'm binding with Futures callBack methos for resoonse.
 */
@Slf4j
@Service
public class TransactionService {


    private TaskExecutor taskExecutor;
    private final WalletServerServiceGrpc.WalletServerServiceFutureStub walletServerServiceFutureStub;
    public TransactionService(@Qualifier("taskExecutor") TaskExecutor taskExecutor){
        // GRpc need ssl for development purpose we have to use usePlaintext to avoid ssl.
        ManagedChannel managedChannel = ManagedChannelBuilder.forAddress("localhost",6565).usePlaintext().build();
        walletServerServiceFutureStub = WalletServerServiceGrpc.newFutureStub(managedChannel);
        this.taskExecutor = taskExecutor;
    }

    @Async
    public CompletableFuture<ListenableFuture<WalletResponse>> withdraw(long amount, WalletCurrency walletCurrency) {
        WalletRequest walletRequest = WalletRequest.newBuilder().setUserId(1).setAmount(amount).setWalletCurrency(walletCurrency).build();
        log.info("Sending request to server ");
        ListenableFuture<WalletResponse> walletResponse = walletServerServiceFutureStub.withdraw(walletRequest);

        Futures.addCallback(walletResponse, new FutureCallback<WalletResponse>() {
            @Override
            public void onSuccess(@NullableDecl WalletResponse result) {
                log.info("Wallet Response {}", result.getResponseStatus());
            }

            @Override
            public void onFailure(Throwable t) {
                StatusRuntimeException statusRuntimeException = (StatusRuntimeException) t;
                if(statusRuntimeException.getMessage().contains(ResponseStatus.INSUFFICIENT_FUNDS.name()))
                    log.warn("Error in Withdraw Transaction Response {}",ResponseStatus.INSUFFICIENT_FUNDS);
            }
        },taskExecutor);
        return CompletableFuture.completedFuture(walletResponse);
    }

    @Async
    public CompletableFuture<ListenableFuture<WalletResponse>> deposit(long amount,WalletCurrency walletCurrency) {
        WalletRequest walletRequest = WalletRequest.newBuilder().setUserId(1).setAmount(amount).setWalletCurrency(walletCurrency).build();
        log.info("Sending request to server ");
        ListenableFuture<WalletResponse> walletResponse = walletServerServiceFutureStub.deposit(walletRequest);

        Futures.addCallback(walletResponse, new FutureCallback<WalletResponse>() {
            @Override
            public void onSuccess(@NullableDecl WalletResponse result) {
                log.info("Wallet Response {}", result);
            }

            @Override
            public void onFailure(Throwable t) {
                StatusRuntimeException statusRuntimeException = (StatusRuntimeException) t;
                log.error("Error in Deposit Transaction message {} status {}",statusRuntimeException.getMessage(),statusRuntimeException.getStatus().getDescription());
            }
        },taskExecutor);
        return CompletableFuture.completedFuture(walletResponse);
    }

    @Async
    public CompletableFuture<ListenableFuture<WalletResponse>> balance(){
        log.info("Sending request to server");
        WalletRequest walletRequest = WalletRequest.newBuilder().setUserId(1).build();
        ListenableFuture<WalletResponse> walletResponse = walletServerServiceFutureStub.balance(walletRequest);

            Futures.addCallback(walletResponse, new FutureCallback<WalletResponse>() {
                @Override
                public void onSuccess(@NullableDecl WalletResponse result) {
                    log.info("Received response from server");
                    if(!CollectionUtils.isEmpty(result.getWalletBalanceList()))
                        result.getWalletBalanceList().forEach(walletBalance ->
                                log.info("UserWalletBalance {}",walletBalance)
                        );

                }

                @Override
                public void onFailure(Throwable t) {
                    StatusRuntimeException statusRuntimeException = (StatusRuntimeException) t;
                    log.error("Error In Balance Transaction message {} status {}", statusRuntimeException.getMessage(), statusRuntimeException.getStatus().getDescription());
                }
            },taskExecutor);
        return CompletableFuture.completedFuture(walletResponse);
    }
}
