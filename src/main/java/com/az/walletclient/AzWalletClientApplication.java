package com.az.walletclient;

import com.az.walletclient.services.ClientService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.scheduling.annotation.EnableAsync;


@EnableAsync
@Slf4j
@SpringBootApplication
public class AzWalletClientApplication {

    public static void main(String[] args) {

        ConfigurableApplicationContext applicationContext = SpringApplication.run(AzWalletClientApplication.class, args);
        ClientService clientService = applicationContext.getBean(ClientService.class);
        if(clientService.callUserCLI())
            applicationContext.close();

    }
}
