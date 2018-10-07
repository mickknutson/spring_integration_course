package io.baselogic.integration.routing.routers.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.channel.QueueChannel;
import org.springframework.integration.dsl.MessageChannels;
import org.springframework.integration.router.HeaderValueRouter;
import org.springframework.integration.router.PayloadTypeRouter;
import org.springframework.integration.router.RecipientListRouter;

@Configuration
@Slf4j
@SuppressWarnings({"Duplicates", "SpringJavaInjectionPointsAutowiringInspection"})
public class IntegrationConfig {



    //---------------------------------------------------------------------------//
    // FLOWS



    //---------------------------------------------------------------------------//
    // CHANNELS


    /**
     * DirectChannel is an implementation of SubscribableChannel
     * Invokes a single subscriber for each sent Message
     * The invocation will occur in the sender's thread
     * Supports Load Balancing Strategies
     * Failover strategy enabled by default
     *
     * @return DirectChannel
     */
    @Bean
    public DirectChannel inputChannel() {
        return MessageChannels.direct().get();
    }


    /**
     * PollableChannel periodically checks for messages
     * Consumer controls message processing
     * Adds overhead to maintain waiting messages
     * Reduces real-time response
     * Buffered QueueChannel implementation supports asynchronous transmission
     * Context not shared across queue
     *
     * @return PollableChannel
     */
    @Bean
    public QueueChannel outputChannel() {
        return MessageChannels.queue(5).get();
    }



} // The End...
