package io.baselogic.integration.routing.routers.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.channel.QueueChannel;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.MessageChannels;
import org.springframework.integration.router.HeaderValueRouter;
import org.springframework.integration.router.PayloadTypeRouter;

//@Configuration
@Slf4j
@SuppressWarnings({"Duplicates", "SpringJavaInjectionPointsAutowiringInspection"})
public class HeaderValueRouterConfig {



    //---------------------------------------------------------------------------//
    // FLOWS


    //---------------------------------------------------------------------------//
    // SERVICES

    @Bean
    @ServiceActivator(inputChannel = "routingChannel")
    public HeaderValueRouter router() {
        HeaderValueRouter router = new HeaderValueRouter("routingHeader");
        router.setChannelMapping("headerValueA", "channelA");
        router.setChannelMapping("headerValueB", "channelB");
        return router;
    }



    //---------------------------------------------------------------------------//
    // CHANNELS

    @Bean
    public DirectChannel routingChannel() {
        return MessageChannels.direct().get();
    }

    @Bean
    public QueueChannel channelA() {
        return MessageChannels.queue(5).get();
    }

    @Bean
    public QueueChannel channelB() {
        return MessageChannels.queue(5).get();
    }

} // The End...
