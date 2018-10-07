package io.baselogic.integration.routing.routers.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.channel.PriorityChannel;
import org.springframework.integration.channel.QueueChannel;
import org.springframework.integration.channel.interceptor.WireTap;
import org.springframework.integration.dsl.MessageChannels;
import org.springframework.integration.dsl.Pollers;
import org.springframework.integration.handler.LoggingHandler;
import org.springframework.integration.router.PayloadTypeRouter;
import org.springframework.integration.scheduling.PollerMetadata;

//@Configuration
@Slf4j
@SuppressWarnings({"Duplicates", "SpringJavaInjectionPointsAutowiringInspection"})
public class PayloadTypeRouterConfig {



    //---------------------------------------------------------------------------//
    // FLOWS



    //---------------------------------------------------------------------------//
    // SERVICES

    @Bean
    @ServiceActivator(inputChannel = "routingChannel")
    public PayloadTypeRouter router() {
        PayloadTypeRouter router = new PayloadTypeRouter();
        router.setChannelMapping(String.class.getName(), "stringChannel");
        router.setChannelMapping(Number.class.getName(), "numberChannel");
        return router;
    }



    //---------------------------------------------------------------------------//
    // CHANNELS

    @Bean
    public DirectChannel routingChannel() {
        return MessageChannels.direct().get();
    }

    @Bean
    public QueueChannel stringChannel() {
        return MessageChannels.queue(5).get();
    }

    @Bean
    public QueueChannel numberChannel() {
        return MessageChannels.queue(5).get();
    }

} // The End...
