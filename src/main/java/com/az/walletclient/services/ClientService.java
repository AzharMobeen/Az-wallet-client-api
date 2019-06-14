package com.az.walletclient.services;


import com.az.wallet.server.proto.ResponseStatus;
import com.az.wallet.server.proto.WalletResponse;
import com.google.common.util.concurrent.ListenableFuture;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

/**
 * @author - Azhar Mobeen
 *
 * Description:
 *  =>  It's main service that is receiving Requests from CLI and then processing.
 */

@Slf4j
@Service
public class ClientService {

    private final UserService userService;
    public ClientService(UserService userService){
        this.userService = userService;
    }

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

    /*
        =>  this method is for User CLI
        =>  It will provide option for transaction or exist the program
    */
    public boolean callUserCLI(){
        int option;
        do{
            System.out.println("Please enter your desire option");
            System.out.println("For Transaction >>  1");
            System.out.println("Fot Exit >>  0");
            Scanner scanner = new Scanner(System.in);
            option = scanner.nextInt();
            scanner.nextLine();
            switch (option){
                case 1:
                    this.callTransactionMethod();
                    break;
                case 0:
                    break;
            }

        }while (option!=0);
        return true;
    }

    /*
        =>  This method called from User CLI option for Transactions as per user request parameters
    */
    private void callTransactionMethod(){

        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter User Count :");
        int userCount = scanner.nextInt();
        scanner.nextLine();
        System.out.println("Enter Request Count :");
        int requestCount = scanner.nextInt();
        scanner.nextLine();
        System.out.println("Enter Round Count :");
        int roundCount = scanner.nextInt();
        scanner.nextLine();
        long startTime = System.currentTimeMillis();
        int totalTransaction = transactionProcess(userCount,requestCount,roundCount);
        long endTime = System.currentTimeMillis();
        log.info("Task Execution Time in seconds {}, Total Transaction {}", TimeUnit.MILLISECONDS.toSeconds(endTime-startTime), totalTransaction);
    }

    /*
        =>  In this method I'm sending userCount, requestCount and roundCount to userService to process.
        =>  And User Service will return me List of ListenableFuture<WalletResponse>
        =>  Then I will call static method for server response.
    */
    private int transactionProcess(int userCount,int requestCount, int roundCount){
        List<CompletableFuture<ListenableFuture<WalletResponse>>> list = Collections.EMPTY_LIST;
        try {
            list = userService.callTaskRoundServiceProcessPerUser(userCount,requestCount,roundCount);
            //list.forEach(ClientService::accept);
        } catch (ExecutionException | InterruptedException e) {
            log.error("ExecutionException | InterruptedException ",e);
        }
        return list.size();
    }
}
