package io.baselogic.integration.core.introduction.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.InboundChannelAdapter;
import org.springframework.integration.annotation.Poller;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.channel.PriorityChannel;
import org.springframework.integration.channel.QueueChannel;
import org.springframework.integration.core.MessageSource;
import org.springframework.integration.dsl.Pollers;
import org.springframework.integration.scheduling.PollerMetadata;
import org.springframework.messaging.PollableChannel;
import org.springframework.messaging.support.GenericMessage;

import java.io.File;
import java.text.SimpleDateFormat;

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

//    @Bean
//    public IntegrationFlow upcaseFlow() {
//        return IntegrationFlows.from("input")
//                .transform(String::toUpperCase)
//                .get();
//    }



    //---------------------------------------------------------------------------//
    // CHANNELS

    @Bean
    public DirectChannel inputChannel() {
        return new DirectChannel();
    }


    @Bean
    public PollableChannel outputChannel() {
        return new PriorityChannel(5_000);
    }


    /*
    	<int:inbound-channel-adapter expression="T(java.lang.System).currentTimeMillis()" channel="logger">
		<int:poller fixed-delay="20000" max-messages-per-poll="2" />
	</int:inbound-channel-adapter>

     */

    /**
     *
     * 	<int:inbound-channel-adapter expression="T(java.lang.System).currentTimeMillis()" channel="logger">
     * 		<int:poller fixed-delay="20000" max-messages-per-poll="2" />
     * 	</int:inbound-channel-adapter>
     *
     * @return
     */
//    @InboundChannelAdapter(channel = "fooBar")
//    public PollerMetadata inboundChannel() {
//        log.info("inboundChannel()");
//        return Pollers.fixedDelay(1_00).get();
//    }





    /**
     * 	<int:inbound-channel-adapter expression="T(java.lang.System).currentTimeMillis()" channel="logger">
     * 		<int:poller fixed-delay="20000" max-messages-per-poll="2" />
     * 	</int:inbound-channel-adapter>
     *
     * @return
     */
    @Bean(name = PollerMetadata.DEFAULT_POLLER)
    public PollerMetadata poller() {
        log.info("poller()");
        return Pollers.fixedDelay(500).get();
    }


    @Bean
    public DirectChannel directChannel() {
        return new DirectChannel();
    }


    @Bean
    public QueueChannel queueChannel() {
        return new QueueChannel();
    }


    /*
//    DirectChannel
//    PriorityChannel
//    QueueChannel
//    ExecutorChannel
//    PublishSubscribeChannel


//  @Publisher

@MessageEndpoint
@IntegrationComponentScan ??
@Transformer
@Poller

@InboundChannelAdapter


    */


    /*@Bean
    @InboundChannelAdapter(value = "inputChannel", autoStartup = "true",
            poller = @Poller(fixedDelay = "500", maxMessagesPerPoll = "1"))
    public MessageSource<String> timerMessageSource() {
        return () -> new GenericMessage<String>("timerMessageSource");
    }*/


//    @Bean
//    @InboundChannelAdapter(value = "timerMessageSource", poller = @Poller(fixedDelay = "2000"))
//    public MessageSource<String> timerMessageSource2() {
//        //        headers.put("content-type", "application/user");
//        return () -> new GenericMessage<>("{\"name\":\"didi\", \"age\":30}");
//    }





} // The End...
