package com.az.walletclient.services;

import com.az.wallet.server.proto.WalletResponse;
import com.google.common.util.concurrent.ListenableFuture;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

/**
 * @author - Azhar Mobeen
 *
 * Description:
 *  =>  In this class every round from roundCount will call random round from TaskRoundService.
 */
@Slf4j
@Service
public class RoundService {

    private final TaskRoundService taskRoundService;
    private final ResponseService responseService;
    public RoundService(TaskRoundService taskRoundService,ResponseService responseService) {
        this.taskRoundService= taskRoundService;
        this.responseService = responseService;
    }

    /*
        =>  In this method I'm calling randomly round methods from taskRoundService
        =>  In return it will get List of ListenableFuture<WalletResponse>.
    */
    public List<CompletableFuture<ListenableFuture<WalletResponse>>> callTaskRoundServiceProcessPerRound(int roundCount) throws ExecutionException, InterruptedException {
        List<Integer> randomMethod = Arrays.asList(1,2,3);
        Random random = new Random();
        List<CompletableFuture<ListenableFuture<WalletResponse>>> list = new ArrayList<>();

        for(int round=1;round<=roundCount;round++) {
            CompletableFuture<List<CompletableFuture<ListenableFuture<WalletResponse>>>> roundMethodResult = null;
            int randomIndex = random.nextInt(randomMethod.size());
            switch(randomIndex) {
                case 0:
                    roundMethodResult = taskRoundService.roundA();
                    list.addAll(roundMethodResult.get());
                    break;
                case 1:
                    roundMethodResult = taskRoundService.roundB();
                    list.addAll(roundMethodResult.get());
                    break;
                case 2:
                    roundMethodResult = taskRoundService.roundC();
                    list.addAll(roundMethodResult.get());
                    break;
            }
        }
        return list;
    }

    /*
        =>  It's called by RequestService concurrently/Async way.
        =>  In this method I'm calling randomly round methods from taskRoundService
        =>  In return it will give totalTransactionsPerRound.
    */
    @Async
    public CompletableFuture<Integer> callTaskRoundServiceProcessPerRound_v2(int roundCount) throws ExecutionException, InterruptedException {
        List<Integer> randomMethod = Arrays.asList(1,2,3);
        Random random = new Random();
        int totalTransactionsPerRound = 0;
        for(int round=1;round<=roundCount;round++) {
            CompletableFuture<List<CompletableFuture<ListenableFuture<WalletResponse>>>> roundMethodResult = null;
            int randomIndex = random.nextInt(randomMethod.size());
            switch(randomIndex) {
                case 0:
                    roundMethodResult = taskRoundService.roundA();
                    totalTransactionsPerRound += roundMethodResult.get().size();
                    responseService.getResponseFromServer_v2(roundMethodResult);

                    break;
                case 1:
                    roundMethodResult = taskRoundService.roundB();
                    totalTransactionsPerRound += roundMethodResult.get().size();
                    responseService.getResponseFromServer_v2(roundMethodResult);
                    break;
                case 2:
                    roundMethodResult = taskRoundService.roundC();
                    totalTransactionsPerRound += roundMethodResult.get().size();
                    responseService.getResponseFromServer_v2(roundMethodResult);
                    break;
            }
        }
        return CompletableFuture.completedFuture(totalTransactionsPerRound);
    }
}
