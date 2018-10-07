package io.baselogic.integration.routing.routers;

import io.baselogic.integration.Application;
import io.baselogic.integration.routing.routers.config.PayloadTypeRouterConfig;
import io.baselogic.integration.routing.routers.config.RecipientListRouterConfig;
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

@ContextConfiguration(classes = {TestConfig.class, RecipientListRouterConfig.class})

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
@SpringIntegrationTest
@Slf4j
@SuppressWarnings({"Duplicates", "SpringJavaInjectionPointsAutowiringInspection"})
public class RecipientListRouterTests {

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

    @Autowired
    private QueueChannel channelC;


    @Before
    public void beforeEachTest(){
        // Reset the channel stats
        routingChannel.reset();
        channelA.reset();
        channelB.reset();
        channelC.reset();
    }



    //-----------------------------------------------------------------------//


    @Test
    public void test_RecipientListRouter() throws Exception {

        String messagePayload = "String Type Message";

        log.info(LINE);

        Message<String> message = MessageBuilder.withPayload(messagePayload).build();
        messagingTemplate.send(routingChannel, message);
        GenericMessage<String> resultA = (GenericMessage<String>) channelA.receive(1_000);
        GenericMessage<String> resultB = (GenericMessage<String>) channelB.receive(1_000);
        GenericMessage<String> resultC = (GenericMessage<String>) channelC.receive(1_000);

        log.info("routingChannel.getSendCount(): {}", routingChannel.getSendCount());
        log.info("channelA messages received: {}", channelA.getReceiveCount());
        log.info("channelB messages received: {}", channelB.getReceiveCount());
        log.info("channelC messages received: {}", channelC.getReceiveCount());

        assertSoftly(
                softAssertions -> {
                    assertThat(resultA.getPayload()).isEqualTo(messagePayload);
                    assertThat(resultB.getPayload()).isEqualTo(messagePayload);
                    assertThat(resultC.getPayload()).isEqualTo(messagePayload);
                    assertThat(routingChannel.getSendCount()).isEqualTo(1);
                    assertThat(channelA.getReceiveCount()).isEqualTo(1);
                    assertThat(channelB.getReceiveCount()).isEqualTo(1);
                    assertThat(channelC.getReceiveCount()).isEqualTo(1);
                }
        );

        log.info(LINE);
    }


    //-----------------------------------------------------------------------//


} // The End
