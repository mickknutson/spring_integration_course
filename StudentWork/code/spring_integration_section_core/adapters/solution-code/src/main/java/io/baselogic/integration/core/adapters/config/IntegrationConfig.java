package io.baselogic.integration.core.adapters.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.BridgeFrom;
import org.springframework.integration.annotation.BridgeTo;
import org.springframework.integration.annotation.Poller;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.channel.QueueChannel;
import org.springframework.integration.channel.RendezvousChannel;
import org.springframework.integration.dsl.MessageChannels;

@Configuration
@Slf4j
@SuppressWarnings({"Duplicates", "SpringJavaInjectionPointsAutowiringInspection"})
public class IntegrationConfig {



    //---------------------------------------------------------------------------//
    // FLOWS



    //---------------------------------------------------------------------------//
    // CHANNELS

    @Bean
    @BridgeTo(value = "subscribableOutputChannel")
    public DirectChannel subscribableInputChannel() {
        return new DirectChannel();
    }


    @Bean
    public QueueChannel subscribableOutputChannel() {
        return new QueueChannel(20);
    }


    /**
     * Need to Poll the 'subscribableOutputChannel' for available messages
     * @return
     */
    @Bean
    @BridgeFrom(value = "subscribableOutputChannel",
            poller = @Poller(fixedRate = "200")
    )
    public DirectChannel alternateDirectChannel() {
        return MessageChannels.direct().get();
    }




} // The End...
