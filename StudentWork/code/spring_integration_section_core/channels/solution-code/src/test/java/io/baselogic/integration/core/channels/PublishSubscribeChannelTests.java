package io.baselogic.integration.core.channels;

import io.baselogic.integration.Application;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.channel.PublishSubscribeChannel;
import org.springframework.integration.channel.QueueChannel;
import org.springframework.integration.core.MessagingTemplate;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.integration.test.context.MockIntegrationContext;
import org.springframework.integration.test.context.SpringIntegrationTest;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
@SpringIntegrationTest
@Slf4j
@SuppressWarnings({"Duplicates", "SpringJavaInjectionPointsAutowiringInspection"})
public class PublishSubscribeChannelTests {


    private static final String LINE = "+" + new String(new char[40]).replace('\0', '-') + "+";

    private MessagingTemplate messagingTemplate = new MessagingTemplate();

    @Autowired
    private MockIntegrationContext mockIntegrationContext;


    @Autowired
    private DirectChannel pubSubInputChannel;

    @Autowired
    private QueueChannel outputChannelA;

    @Autowired
    private QueueChannel outputChannelB;

    @Autowired
    private PublishSubscribeChannel publishSubscribeChannel;



    @Before
    public void beforeEachTest(){
        // Reset the channel stats
        pubSubInputChannel.reset();
        outputChannelA.reset();
        outputChannelB.reset();
        publishSubscribeChannel.reset();
    }



    //-----------------------------------------------------------------------//


    @Test
    public void test_integration__send_message__publishSubscribeChannel() throws Exception {

        log.info(LINE);


        long expirationLong = Date.from(Instant.now().plus(1, ChronoUnit.DAYS)).getTime();

        Message<String> message = MessageBuilder.withPayload("We have PubSub Integration")
                .setExpirationDate(expirationLong)
                .setPriority(42)
                .setHeader("customHeader", "my customHeader")
                .build();

        // Send message...
        messagingTemplate.send(pubSubInputChannel, message);

        // Receive message with a 200ms timeout
        GenericMessage<String> resultA = (GenericMessage<String>) outputChannelA.receive(1_000);
        GenericMessage<String> resultB = (GenericMessage<String>) outputChannelB.receive(1_000);

        log.info("resultA: {}", resultA);
        log.info("resultB: {}", resultB);

        log.info("publishSubscribeChannel subscribers: {}", publishSubscribeChannel.getSubscriberCount());
        log.info("pubSubInputChannel subscribers: {}", pubSubInputChannel.getSubscriberCount());
        log.info("pubSubInputChannel messages sent: {}", pubSubInputChannel.getSendCount());
        log.info("outputChannelA.getReceiveCount(): {}", outputChannelA.getReceiveCount());
        log.info("outputChannelB.getReceiveCount(): {}", outputChannelB.getReceiveCount());

        assertThat(pubSubInputChannel.getSendCount()).isEqualTo(1);

        assertThat(outputChannelA.getReceiveCount()).isEqualTo(1);
        assertThat(outputChannelB.getReceiveCount()).isEqualTo(1);


        log.info(LINE);
    }



    //-----------------------------------------------------------------------//


} // The End
