package io.baselogic.integration.routing.routers;

import io.baselogic.integration.Application;
import io.baselogic.integration.routing.routers.config.PayloadTypeRouterConfig;
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
import static org.junit.Assert.assertEquals;

@ContextConfiguration(classes = {TestConfig.class, PayloadTypeRouterConfig.class})

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
@SpringIntegrationTest
@Slf4j
@SuppressWarnings({"Duplicates", "SpringJavaInjectionPointsAutowiringInspection"})
public class PayloadTypeRouterTests {

    private static final String LINE = "+" + new String(new char[40]).replace('\0', '-') + "+";

    private MessagingTemplate messagingTemplate = new MessagingTemplate();

    @Autowired
    private MockIntegrationContext mockIntegrationContext;


    @Autowired
    private DirectChannel routingChannel;

    @Autowired
    private QueueChannel stringChannel;

    @Autowired
    private QueueChannel numberChannel;


    @Before
    public void beforeEachTest(){
        // Reset the channel stats
        routingChannel.reset();
        stringChannel.reset();
        numberChannel.reset();
    }



    //-----------------------------------------------------------------------//


    @Test
    public void test_PayloadTypeRouter_STRING_Type() throws Exception {

        log.info(LINE);

        Message<String> stringMessage = MessageBuilder.withPayload("String Type Message").build();

        // Send message...
        messagingTemplate.send(routingChannel, stringMessage);

        // Receive message with a 1000ms timeout
        GenericMessage<String> result = (GenericMessage<String>) stringChannel.receive(1_000);

        log.info("routingChannel.getSendCount(): {}", routingChannel.getSendCount());
        log.info("outputChannel messages received: {}", stringChannel.getReceiveCount());

        assertSoftly(
                softAssertions -> {
                    assertThat(result.getPayload()).isEqualTo("String Type Message");
                    assertThat(routingChannel.getSendCount()).isEqualTo(1);
                    assertThat(stringChannel.getReceiveCount()).isEqualTo(1);
                    assertThat(numberChannel.getReceiveCount()).isEqualTo(0);
                }
        );

        log.info(LINE);
    }


    @Test
    public void test_PayloadTypeRouter_NUMBER_Type() throws Exception {

        log.info(LINE);

        Message<Integer> integerMessage = MessageBuilder.withPayload(42).build();
        messagingTemplate.send(routingChannel, integerMessage);
        GenericMessage<Number> integerResult = (GenericMessage<Number>) numberChannel.receive(1_000);

        Message<Long> longMessage = MessageBuilder.withPayload(454L).build();
        messagingTemplate.send(routingChannel, longMessage);
        GenericMessage<Number> longResult = (GenericMessage<Number>) numberChannel.receive(1_000);

        log.info("routingChannel.getSendCount(): {}", routingChannel.getSendCount());
        log.info("outputChannel messages received: {}", stringChannel.getReceiveCount());
        log.info("numberChannel messages received: {}", numberChannel.getReceiveCount());

        assertSoftly(
                softAssertions -> {
                    assertThat(integerResult.getPayload()).isEqualTo(42);
                    assertThat(longResult.getPayload()).isEqualTo(454L);
                    assertThat(routingChannel.getSendCount()).isEqualTo(2);
                    assertThat(stringChannel.getReceiveCount()).isEqualTo(0);
                    assertThat(numberChannel.getReceiveCount()).isEqualTo(2);
                }
        );

        log.info(LINE);
    }


    //-----------------------------------------------------------------------//


} // The End
