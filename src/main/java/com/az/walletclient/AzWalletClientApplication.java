package com.az.walletclient;


import com.az.walletclient.exceptions.RejectedExecutionHandlerImpl;
import com.az.walletclient.services.ClientService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

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

    @Bean
    public TaskExecutor taskExecutor(){
        ThreadPoolTaskExecutor threadPoolTaskExecutor = new ThreadPoolTaskExecutor();
        threadPoolTaskExecutor.setMaxPoolSize(4);
        threadPoolTaskExecutor.setCorePoolSize(4);
        threadPoolTaskExecutor.setQueueCapacity(500);
        threadPoolTaskExecutor.setThreadNamePrefix("threadPoolExecutor-");
        //threadPoolTaskExecutor.setWaitForTasksToCompleteOnShutdown(true);
        threadPoolTaskExecutor.setRejectedExecutionHandler(new RejectedExecutionHandlerImpl());
        threadPoolTaskExecutor.initialize();
        return threadPoolTaskExecutor;
    }

}
