package com.az.walletclient.services;

import com.az.wallet.server.proto.WalletResponse;
import com.google.common.util.concurrent.ListenableFuture;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

/**
 * @author - Azhar Mobeen
 *
 * Description:
 *  =>  In this class we are sending roundCount of each user request for Transactions.
 */

@Slf4j
@Service
public class RequestService {

    private final RoundService roundService;
    public RequestService(RoundService roundService){
        this.roundService = roundService;
    }

    /*
        =>  In this method we are sending each request roundCount for Transactions
        =>  In return it will get List of ListenableFuture<WalletResponse>.
    */
    public List<CompletableFuture<ListenableFuture<WalletResponse>>> callTaskRoundServiceProcessPerRequest(int requestCount, int roundCount) throws ExecutionException, InterruptedException {

        List<CompletableFuture<ListenableFuture<WalletResponse>>> list = new ArrayList<>();

        for(int request=1; request<=requestCount;request++) {
            list.addAll(roundService.callTaskRoundServiceProcessPerRound(roundCount));
        }
        return list;
    }

    @Async
    public CompletableFuture<Integer> callTaskRoundServiceProcessPerRequest_v2(int requestCount, int roundCount) throws ExecutionException, InterruptedException {

        int totalTransactionsPerRequest = 0;
        for(int request=1; request<=requestCount;request++) {
            totalTransactionsPerRequest += roundService.callTaskRoundServiceProcessPerRound_v2(roundCount).get();
        }
        return CompletableFuture.completedFuture(totalTransactionsPerRequest);
    }
}
