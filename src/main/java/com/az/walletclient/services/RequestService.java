package com.az.walletclient.services;

import com.az.wallet.server.proto.WalletResponse;
import com.google.common.util.concurrent.ListenableFuture;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Slf4j
@Service
public class RequestService {

    private final RoundService roundService;
    public RequestService(RoundService roundService){
        this.roundService = roundService;
    }


    public List<CompletableFuture<ListenableFuture<WalletResponse>>> callTaskRoundServiceProcessPerRequest(int requestCount, int roundCount) {

        List<CompletableFuture<ListenableFuture<WalletResponse>>> list = new ArrayList<>();
        for(int request=1; request<=requestCount;request++) {
            list.addAll(roundService.callTaskRoundServiceProcessPerRound(roundCount));
        }
        return list;
    }
}
