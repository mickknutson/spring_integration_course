package io.baselogic.integration.core.endpoints.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.channel.QueueChannel;
import org.springframework.messaging.PollableChannel;

@Configuration
@Slf4j
@SuppressWarnings({"Duplicates", "SpringJavaInjectionPointsAutowiringInspection"})
public class IntegrationConfig {



    //---------------------------------------------------------------------------//
    // FLOWS



    //---------------------------------------------------------------------------//
    // CHANNELS

    @Bean
    public DirectChannel subscribableInputChannel() {
        return new DirectChannel();
    }


    @Bean
    public QueueChannel subscribableOutputChannel() {
        return new QueueChannel(20);
    }





} // The End...
