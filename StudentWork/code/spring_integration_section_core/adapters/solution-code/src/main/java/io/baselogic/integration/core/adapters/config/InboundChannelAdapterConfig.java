package io.baselogic.integration.core.adapters.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.BridgeFrom;
import org.springframework.integration.annotation.BridgeTo;
import org.springframework.integration.annotation.InboundChannelAdapter;
import org.springframework.integration.annotation.Poller;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.channel.PublishSubscribeChannel;
import org.springframework.integration.channel.QueueChannel;
import org.springframework.integration.channel.RendezvousChannel;
import org.springframework.integration.core.MessageSource;
import org.springframework.integration.dsl.MessageChannels;
import org.springframework.integration.dsl.Pollers;
import org.springframework.integration.scheduling.PollerMetadata;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.support.GenericMessage;

import java.util.Date;

@Configuration
@Slf4j
@SuppressWarnings({"Duplicates", "SpringJavaInjectionPointsAutowiringInspection"})
public class InboundChannelAdapterConfig {



    //---------------------------------------------------------------------------//
    // FLOWS



    //---------------------------------------------------------------------------//
    // CHANNELS

    @Bean
    public DirectChannel inputChannel() {
        return MessageChannels.direct().get();
    }


    @Bean
    public QueueChannel outputChannel() {
        return MessageChannels.queue(20).get();
    }


    @Bean
    @BridgeTo("outputChannel")
    public PublishSubscribeChannel publishSubscribeChannel() {
        return MessageChannels.publishSubscribe().get();
    }


    /**
     * Setting the fixedRate to 100ms for Unit testing, so messages will be sent asap.
     *
     * @return
     */
    @Bean
    @InboundChannelAdapter(
            channel = "inputChannel",
            poller = @Poller(fixedRate = "100")
    )
    public MessageSource<String> timerMessageSource() {
        return () -> new GenericMessage<>("timerMessageSource: " + new Date());
    }


    /**
     * This is a Global Default Poller.
     *
     * @return
     */
    @Bean(name = PollerMetadata.DEFAULT_POLLER)
    public PollerMetadata poller() {
        return Pollers.fixedRate(200).get();
    }


} // The End...
