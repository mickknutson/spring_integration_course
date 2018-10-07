package io.baselogic.integration.core.channels.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.channel.PriorityChannel;
import org.springframework.integration.dsl.MessageChannels;

@Configuration
@Slf4j
@SuppressWarnings({"Duplicates", "SpringJavaInjectionPointsAutowiringInspection"})
public class PriorityChannelConfig {



    //---------------------------------------------------------------------------//
    // FLOWS



    //---------------------------------------------------------------------------//
    // CHANNELS

    @Bean
    public PriorityChannel priorityChannel() {
        return MessageChannels.priority()
                .capacity(100)
                .comparator((a, b) ->
                        ((String) a.getPayload()).compareTo(
                                ((String) b.getPayload())))
                .get();
    }



} // The End...
