package io.baselogic.integration.routing.routers.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.channel.QueueChannel;
import org.springframework.integration.dsl.MessageChannels;
import org.springframework.integration.router.PayloadTypeRouter;
import org.springframework.integration.router.RecipientListRouter;

//@Configuration
@Slf4j
@SuppressWarnings({"Duplicates", "SpringJavaInjectionPointsAutowiringInspection"})
public class RecipientListRouterConfig {



    //---------------------------------------------------------------------------//
    // FLOWS



    //---------------------------------------------------------------------------//
    // SERVICES



    @Bean
    @ServiceActivator(inputChannel = "routingChannel")
    public RecipientListRouter router() {
        RecipientListRouter router = new RecipientListRouter();
        router.setSendTimeout(1_000);
        router.setIgnoreSendFailures(true);
        router.setApplySequence(true);
        router.addRecipient("channelA");
        router.addRecipient("channelB");
        router.addRecipient("channelC");
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

    @Bean
    public QueueChannel channelC() {
        return MessageChannels.queue(5).get();
    }

} // The End...
