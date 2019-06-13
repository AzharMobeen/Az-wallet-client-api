package com.az.walletclient.services;

import com.az.wallet.server.proto.WalletResponse;
import com.google.common.util.concurrent.ListenableFuture;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CompletableFuture;

@Slf4j
@Service
public class RoundService {

    private final TaskRoundService taskRoundService;

    public RoundService(TaskRoundService taskRoundService) {
        this.taskRoundService= taskRoundService;
    }

    public List<CompletableFuture<ListenableFuture<WalletResponse>>> callTaskRoundServiceProcessPerRound(int roundCount) {
        List<Integer> randomMethod = Arrays.asList(1,2,3);
        Random random = new Random();
        List<CompletableFuture<ListenableFuture<WalletResponse>>> list = new ArrayList<>();

        for(int round=1;round<=roundCount;round++) {
            int randomIndex = random.nextInt(randomMethod.size());
            switch(randomIndex) {
                case 0:
                    list.addAll(taskRoundService.roundA());
                    break;
                case 1:
                    list.addAll(taskRoundService.roundB());
                    break;
                case 2:
                    list.addAll(taskRoundService.roundC());
                    break;
            }
        }
        return list;
    }
}
