package io.baselogic.integration.core.gateways.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.channel.QueueChannel;
import org.springframework.integration.dsl.MessageChannels;
import org.springframework.integration.http.outbound.HttpRequestExecutingMessageHandler;

@Configuration
@Slf4j
@SuppressWarnings({"Duplicates", "SpringJavaInjectionPointsAutowiringInspection"})
public class HttpOutboundGatewayConfig {

    //---------------------------------------------------------------------------//
    // FLOWS



    //---------------------------------------------------------------------------//
    // CHANNELS


    /**
     * DirectChannel to receive messages and routing them to the
     * httpOutboundResponseChannel.
     * @return DirectChannel
     */
    @Bean
    public DirectChannel httpOutboundRequestChannel() {
        return MessageChannels.direct().get();
    }

    /**
     * DirectChannel to receive response messages from the httpOutboundChannel
     * @return DirectChannel
     */
    @Bean
    public QueueChannel httpOutboundResponseChannel() {
        return MessageChannels.queue(5).get();
    }


    /**
     * Channel for POSTing requests external to https://httpbin.org/post
     * and getting a response.
     * Sending the response to the 'httpOutboundResponseChannel' channel
     *
     * @return HttpRequestExecutingMessageHandler handling outbound HTTP requests
     */
    @ServiceActivator(inputChannel = "httpOutboundRequestChannel")
    @Bean
    public HttpRequestExecutingMessageHandler httpOutboundGateway() {
        HttpRequestExecutingMessageHandler handler =
                new HttpRequestExecutingMessageHandler("https://httpbin.org/post");
        handler.setHttpMethod(HttpMethod.POST);
        handler.setRequiresReply(true);
        handler.setExpectedResponseType(String.class);
        handler.setOutputChannel(httpOutboundResponseChannel());
        return handler;
    }





} // The End...
