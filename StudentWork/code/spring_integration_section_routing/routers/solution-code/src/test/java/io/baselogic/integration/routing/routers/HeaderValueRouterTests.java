package io.baselogic.integration.routing.routers;

import io.baselogic.integration.Application;
import io.baselogic.integration.routing.routers.config.HeaderValueRouterConfig;
import io.baselogic.integration.routing.routers.config.TestConfig;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.channel.QueueChannel;
import org.springframework.integration.core.MessagingTemplate;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.integration.test.context.MockIntegrationContext;
import org.springframework.integration.test.context.SpringIntegrationTest;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

@ContextConfiguration(classes = {TestConfig.class, HeaderValueRouterConfig.class})

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
@SpringIntegrationTest
@Slf4j
@SuppressWarnings({"Duplicates", "SpringJavaInjectionPointsAutowiringInspection"})
public class HeaderValueRouterTests {

    private static final String LINE = "+" + new String(new char[40]).replace('\0', '-') + "+";

    private MessagingTemplate messagingTemplate = new MessagingTemplate();

    @Autowired
    private MockIntegrationContext mockIntegrationContext;


    @Autowired
    private DirectChannel routingChannel;

    @Autowired
    private QueueChannel channelA;

    @Autowired
    private QueueChannel channelB;


    @Before
    public void beforeEachTest(){
        // Reset the channel stats
        routingChannel.reset();
        channelA.reset();
        channelB.reset();
    }



    //-----------------------------------------------------------------------//


    @Test
    public void test_PayloadTypeRouter_HeaderA() throws Exception {

        log.info(LINE);

        Message<String> message = MessageBuilder.withPayload("We have Integration")
                .setHeader("routingHeader", "headerValueA")
                .build();

        // Send message...
        messagingTemplate.send(routingChannel, message);

        // Receive message with a 1000ms timeout
        GenericMessage<String> result = (GenericMessage<String>) channelA.receive(1_000);

        log.info("routingChannel.getSendCount(): {}", routingChannel.getSendCount());
        log.info("channelA messages received: {}", channelA.getReceiveCount());
        log.info("channelB messages received: {}", channelB.getReceiveCount());

        assertSoftly(
                softAssertions -> {
                    assertThat(result.getPayload()).isEqualTo("We have Integration");
                    assertThat(routingChannel.getSendCount()).isEqualTo(1);
                    assertThat(channelA.getReceiveCount()).isEqualTo(1);
                    assertThat(channelB.getReceiveCount()).isEqualTo(0);
                }
        );

        log.info(LINE);
    }


    @Test
    public void test_PayloadTypeRouter_HeaderB() throws Exception {

        log.info(LINE);

        Message<String> message = MessageBuilder.withPayload("We have Integration")
                .setHeader("routingHeader", "headerValueB")
                .build();

        // Send message...
        messagingTemplate.send(routingChannel, message);

        // Receive message with a 1000ms timeout
        GenericMessage<String> result = (GenericMessage<String>) channelB.receive(1_000);

        log.info("routingChannel.getSendCount(): {}", routingChannel.getSendCount());
        log.info("channelA messages received: {}", channelA.getReceiveCount());
        log.info("channelB messages received: {}", channelB.getReceiveCount());

        assertSoftly(
                softAssertions -> {
                    assertThat(result.getPayload()).isEqualTo("We have Integration");
                    assertThat(routingChannel.getSendCount()).isEqualTo(1);
                    assertThat(channelA.getReceiveCount()).isEqualTo(0);
                    assertThat(channelB.getReceiveCount()).isEqualTo(1);
                }
        );

        log.info(LINE);
    }


    //-----------------------------------------------------------------------//


} // The End
