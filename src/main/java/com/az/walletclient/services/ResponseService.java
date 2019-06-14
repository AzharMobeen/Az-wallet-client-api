package com.az.walletclient.services;

import com.az.wallet.server.proto.ResponseStatus;
import com.az.wallet.server.proto.WalletResponse;
import com.google.common.util.concurrent.ListenableFuture;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@Slf4j
@Service
public class ResponseService {


    /*
        =>  It's a static method called just after getting list of ListenableFuture<WalletResponse>
        =>  After getting sending all the requests to server we need to check response.
    */
    private static void accept(CompletableFuture<ListenableFuture<WalletResponse>> completableFuture) {
        try {
            ListenableFuture<WalletResponse> walletResponseListenableFuture = completableFuture.get();
            walletResponseListenableFuture.get();
        } catch (InterruptedException | ExecutionException e) {
            if(e.getMessage().contains(ResponseStatus.INSUFFICIENT_FUNDS.name()))
                log.warn("Warning in Withdraw Transaction {}",ResponseStatus.INSUFFICIENT_FUNDS);
        }
    }

    @Async
    public void getResponseFromServer(CompletableFuture<List<CompletableFuture<ListenableFuture<WalletResponse>>>> walletResponse){

        try {
            List<CompletableFuture<ListenableFuture<WalletResponse>>> list = walletResponse.get();
            list.forEach(ResponseService::accept);
        } catch (InterruptedException |ExecutionException e) {
            e.printStackTrace();
        }
    }
}
