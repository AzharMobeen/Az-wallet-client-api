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
 *  =>  In this class we are sending each user requestCoount and roundCount for Transactions.
 */

@Slf4j
@Service
public class UserService {

    private final RequestService requestService;
    public UserService(RequestService requestService){
        this.requestService = requestService;
    }

    /*
        =>  In this method we are sending user Request and round for Transactions
        =>  In return it will get List of ListenableFuture<WalletResponse>.
    */
    public List<CompletableFuture<ListenableFuture<WalletResponse>>> callTaskRoundServiceProcessPerUser(int userCount, int requestCount, int roundCount) throws ExecutionException, InterruptedException {

        List<CompletableFuture<ListenableFuture<WalletResponse>>> list = new ArrayList<>();
        for(int user= 1; user<=userCount;user++) {
            list.addAll(requestService.callTaskRoundServiceProcessPerRequest(requestCount,roundCount));
        }
        return list;
    }
}
