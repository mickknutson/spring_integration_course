package io.baselogic.integration.core.endpoints.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.channel.QueueChannel;
import org.springframework.messaging.PollableChannel;

/**
 * TODO:
 * NEED to create demos:
 * 1. FLOW
 * 2. Multiple Messages
 * 3. Error Messages
 *
 */
@Configuration
@Slf4j
@SuppressWarnings({"Duplicates", "SpringJavaInjectionPointsAutowiringInspection"})
public class IntegrationConfig {



    //---------------------------------------------------------------------------//
    // FLOWS



    //---------------------------------------------------------------------------//
    // CHANNELS

    @Bean
    public DirectChannel inputChannel() {
        return new DirectChannel();
    }


    @Bean
    public PollableChannel outputChannel() {
        return new QueueChannel(20);
    }





} // The End...
