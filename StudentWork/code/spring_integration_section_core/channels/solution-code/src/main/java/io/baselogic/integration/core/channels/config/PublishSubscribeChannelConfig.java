package io.baselogic.integration.core.channels.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.BridgeFrom;
import org.springframework.integration.annotation.InboundChannelAdapter;
import org.springframework.integration.annotation.Poller;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.channel.PublishSubscribeChannel;
import org.springframework.integration.channel.QueueChannel;
import org.springframework.integration.core.MessageSource;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.MessageChannels;
import org.springframework.integration.file.FileReadingMessageSource;
import org.springframework.integration.file.filters.SimplePatternFileListFilter;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.io.File;

@Configuration
@Slf4j
@SuppressWarnings({"Duplicates", "SpringJavaInjectionPointsAutowiringInspection"})
public class PublishSubscribeChannelConfig {



    //---------------------------------------------------------------------------//
    // FLOWS

    @Bean
    public IntegrationFlow publishSubscribeChannelFlow() {
        return f -> f
                .channel(pubSubInputChannel())
                .publishSubscribeChannel(threadPoolTaskExecutor(), p -> p
                        .subscribe(f1 -> f1.channel(outputChannelA()))
                        .subscribe(f2 -> f2.channel(outputChannelB()))
                );
    }



    //---------------------------------------------------------------------------//
    // CHANNELS

    /**
     * FIXME: Need to figure out how to use this Channel vs the Flow
     *
     * @return
     */
    @Bean
    public PublishSubscribeChannel publishSubscribeChannel() {
        return MessageChannels.publishSubscribe().get();
    }


    @Bean
    public DirectChannel pubSubInputChannel() {
        return MessageChannels.direct().get();
    }

    @Bean
    public QueueChannel outputChannelA() {
        return MessageChannels.queue(5).get();
    }

    @Bean
    public QueueChannel outputChannelB() {
        return MessageChannels.queue(5).get();
    }




    //---------------------------------------------------------------------------//
    // THREAD POOLS

    @Bean
    public ThreadPoolTaskExecutor threadPoolTaskExecutor() {
        ThreadPoolTaskExecutor threadPoolTaskExecutor = new ThreadPoolTaskExecutor();
        threadPoolTaskExecutor.setMaxPoolSize(2);
        threadPoolTaskExecutor.setCorePoolSize(2);
        threadPoolTaskExecutor.setQueueCapacity(10);
        return threadPoolTaskExecutor;
    }


} // The End...
