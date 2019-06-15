package com.az.walletclient.services;

import com.az.wallet.server.proto.*;
import com.google.common.util.concurrent.ListenableFuture;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * @author - Azhar Mobeen
 *
 *  Description:
 *  =>  In this class I'm calling Actull rounds (roundA, roundB and roundC).
 *  =>  Every round have number of transaction avg transactions are 8.
 *  =>  I'm calling perround as Spring Boot @Async which means as a thrad.
 *  =>  I'm returning List of ListenableFuture<WalletResponse>.
 */

@Slf4j
@Service
public class TaskRoundService {

    private final TransactionService transactionService;

    public TaskRoundService(TransactionService transactionService){
        this.transactionService = transactionService;
    }

    /*
        =>  Deposit 100 USD
        =>  Withdraw 200 USD
        =>  Deposit 100 EUR
        =>  Get Balance
        =>  Withdraw 100 USD
        =>  Get Balance
        =>  Withdraw 100 USD
    */
    @Async
    public CompletableFuture<List<CompletableFuture<ListenableFuture<WalletResponse>>>> roundA() {
        List<CompletableFuture<ListenableFuture<WalletResponse>>> list = new ArrayList<>();
        CompletableFuture<ListenableFuture<WalletResponse>> t1= transactionService.deposit(100,WalletCurrency.USD);
        CompletableFuture<ListenableFuture<WalletResponse>> t2=transactionService.withdraw(200,WalletCurrency.USD);
        CompletableFuture<ListenableFuture<WalletResponse>> t3=transactionService.deposit(100,WalletCurrency.EUR);
        CompletableFuture<ListenableFuture<WalletResponse>> t4=transactionService.balance();
        CompletableFuture<ListenableFuture<WalletResponse>> t5=transactionService.withdraw(100,WalletCurrency.USD);
        CompletableFuture<ListenableFuture<WalletResponse>> t6=transactionService.balance();
        CompletableFuture<ListenableFuture<WalletResponse>> t7=transactionService.withdraw(100,WalletCurrency.USD);

        list.addAll(Arrays.asList(t1,t2,t3,t3,t4,t5,t6,t7));
        return CompletableFuture.completedFuture(list);
    }

    /*
        =>  Withdraw 100 GBP
        =>  Deposit 300 GPB
        =>  Withdraw 100 GBP
        =>  Withdraw 100 GBP
        =>  Withdraw 100 GBP
    */
    @Async
    public CompletableFuture<List<CompletableFuture<ListenableFuture<WalletResponse>>>> roundB() {
        List<CompletableFuture<ListenableFuture<WalletResponse>>> list = new ArrayList<>();
        CompletableFuture<ListenableFuture<WalletResponse>> t1 = transactionService.withdraw(100,WalletCurrency.GBP);
        CompletableFuture<ListenableFuture<WalletResponse>> t2 = transactionService.deposit(300,WalletCurrency.GBP);
        CompletableFuture<ListenableFuture<WalletResponse>> t3 = transactionService.withdraw(100,WalletCurrency.GBP);
        CompletableFuture<ListenableFuture<WalletResponse>> t4 = transactionService.withdraw(100,WalletCurrency.GBP);
        CompletableFuture<ListenableFuture<WalletResponse>> t5 = transactionService.withdraw(100,WalletCurrency.GBP);

        list.addAll(Arrays.asList(t1,t2,t3,t3,t4,t5));
        return CompletableFuture.completedFuture(list);
    }

    /*
        =>  Get Balance
        =>  Deposit 100 USD
        =>  Deposit 100 USD
        =>  Withdraw 100 USD
        =>  Deposit 100 USD
        =>  Get Balance
        =>  Withdraw 200 USD
        =>  Get Balance
    */
    @Async
    public CompletableFuture<List<CompletableFuture<ListenableFuture<WalletResponse>>>> roundC() {
        List<CompletableFuture<ListenableFuture<WalletResponse>>> list = new ArrayList<>();
        CompletableFuture<ListenableFuture<WalletResponse>> t1= transactionService.balance();
        CompletableFuture<ListenableFuture<WalletResponse>> t2=transactionService.deposit(100,WalletCurrency.USD);
        CompletableFuture<ListenableFuture<WalletResponse>> t3=transactionService.deposit(100,WalletCurrency.USD);
        CompletableFuture<ListenableFuture<WalletResponse>> t4=transactionService.withdraw(100,WalletCurrency.USD);
        CompletableFuture<ListenableFuture<WalletResponse>> t5=transactionService.deposit(100,WalletCurrency.USD);
        CompletableFuture<ListenableFuture<WalletResponse>> t6=transactionService.balance();
        CompletableFuture<ListenableFuture<WalletResponse>> t7=transactionService.withdraw(200,WalletCurrency.USD);
        CompletableFuture<ListenableFuture<WalletResponse>> t8= transactionService.balance();
        list.addAll(Arrays.asList(t1,t2,t3,t3,t4,t5,t6,t7,t8));
        return CompletableFuture.completedFuture(list);

    }
}