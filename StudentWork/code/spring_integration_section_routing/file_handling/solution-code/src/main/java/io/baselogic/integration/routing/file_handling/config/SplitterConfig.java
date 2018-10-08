package io.baselogic.integration.routing.file_handling.config;

import io.baselogic.integration.routing.file_handling.splitter.CustomSplitter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.channel.QueueChannel;
import org.springframework.integration.dsl.MessageChannels;

//@Configuration
@Slf4j
@SuppressWarnings({"Duplicates", "SpringJavaInjectionPointsAutowiringInspection"})
public class SplitterConfig {


    //---------------------------------------------------------------------------//
    // FLOWS



    //---------------------------------------------------------------------------//
    // SERVICES

    @Bean
    public CustomSplitter CustomSplitter(){
        return new CustomSplitter();
    }


    //---------------------------------------------------------------------------//
    // CHANNELS

    @Bean
    public DirectChannel inputSplitterChannel() {
        return MessageChannels.direct().get();
    }

    @Bean
    public QueueChannel outputSplitterChannel() {
        return MessageChannels.queue(500).get();
    }


} // The End...
