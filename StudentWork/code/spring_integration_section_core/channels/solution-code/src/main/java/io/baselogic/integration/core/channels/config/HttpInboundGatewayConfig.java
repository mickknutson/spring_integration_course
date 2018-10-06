package io.baselogic.integration.core.channels.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.dsl.MessageChannels;
import org.springframework.integration.http.dsl.Http;
import org.springframework.integration.http.inbound.HttpRequestHandlingMessagingGateway;
import org.springframework.integration.http.inbound.RequestMapping;
import org.springframework.messaging.PollableChannel;

@Configuration
@Slf4j
@SuppressWarnings({"Duplicates", "SpringJavaInjectionPointsAutowiringInspection"})
public class HttpInboundGatewayConfig {

    //---------------------------------------------------------------------------//
    // FLOWS


    @Bean
    public IntegrationFlow httpInboundGatewayFlow() {
        return IntegrationFlows.from(Http.inboundGateway("/inbound")
                .requestMapping(m -> m.methods(HttpMethod.POST))
                .requestPayloadType(String.class))
                .channel(httpInboundRequestChannel())
                .wireTap(httpInboundResponseChannel())
                .handle(
                        (payload, headers) -> {
                            log.info("headers: {}, payload: {}", headers, payload);
                            return payload;
                        }
                )
                .get();
    }


    //---------------------------------------------------------------------------//
    // CHANNELS


    /**
     * httpInboundRequestChannel
     * @return DirectChannel
     */
    @Bean
    public DirectChannel httpInboundRequestChannel() {
        return MessageChannels.direct().get();
    }

    /**
     * httpInboundResponseChannel
     * @return PollableChannel
     */
    @Bean
    public PollableChannel httpInboundResponseChannel() {
        return MessageChannels.queue(5).get();
    }


    @Bean
    public RequestMapping httpInboundGatewayMapping() {
        RequestMapping requestMapping = new RequestMapping();
        requestMapping.setPathPatterns("/inbound");
        requestMapping.setMethods(HttpMethod.POST);
        return requestMapping;
    }

    /**
     * Does not work:
     *     Spring integration Dispatcher has no subscribers for channel
     * @return
     */
    //@Bean
    public HttpRequestHandlingMessagingGateway httpInboundGateway() {
        HttpRequestHandlingMessagingGateway gateway = new HttpRequestHandlingMessagingGateway(true);

        gateway.setRequestMapping(httpInboundGatewayMapping());
        gateway.setRequestChannel(httpInboundRequestChannel());
        gateway.setReplyChannel(httpInboundResponseChannel());
        gateway.setRequestPayloadTypeClass(String.class);
        return gateway;
    }



} // The End...
