package io.baselogic.integration.routing.filters.config;

import io.baselogic.integration.routing.filters.filter.CustomFilter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.integration.annotation.Filter;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.channel.QueueChannel;
import org.springframework.integration.dsl.MessageChannels;
import org.springframework.integration.router.HeaderValueRouter;

//@Configuration
@Slf4j
@SuppressWarnings({"Duplicates", "SpringJavaInjectionPointsAutowiringInspection"})
public class FiltersConfig {



    //---------------------------------------------------------------------------//
    // FLOWS



    //---------------------------------------------------------------------------//
    // SERVICES


    @Bean
    public CustomFilter customFilter(){
        return new CustomFilter();
    }


    //---------------------------------------------------------------------------//
    // CHANNELS

    @Bean
    public DirectChannel inputChannel() {
        return MessageChannels.direct().get();
    }

    @Bean
    public QueueChannel outputChannel() {
        return MessageChannels.queue(5).get();
    }

    @Bean
    public QueueChannel discardChannel() {
        return MessageChannels.queue(5).get();
    }

} // The End...
