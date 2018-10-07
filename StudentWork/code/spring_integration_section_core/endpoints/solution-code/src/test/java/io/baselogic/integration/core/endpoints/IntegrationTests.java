package io.baselogic.integration.core.endpoints;

import io.baselogic.integration.Application;
import io.baselogic.integration.core.endpoints.handlers.TextMessageHandler;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.Resource;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.channel.PublishSubscribeChannel;
import org.springframework.integration.channel.QueueChannel;
import org.springframework.integration.core.MessagingTemplate;
import org.springframework.integration.endpoint.EventDrivenConsumer;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.integration.test.context.MockIntegrationContext;
import org.springframework.integration.test.context.SpringIntegrationTest;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.PollableChannel;
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
public class IntegrationTests {


    private static final String LINE = "+" + new String(new char[40]).replace('\0', '-') + "+";


    private MessagingTemplate messagingTemplate = new MessagingTemplate();


    @Autowired
    private DirectChannel subscribableInputChannel;

    @Autowired
    private TextMessageHandler textMessageHandler;



    //-----------------------------------------------------------------------//

    @Before
    public void beforeEachTest(){
        // prepare for test
    }



    //-----------------------------------------------------------------------//


    @Test
    public void test_integration__event_driven_consumer() throws Exception {

        log.info(LINE);

        subscribableInputChannel.subscribe(textMessageHandler);

//        EventDrivenConsumer consumer = new EventDrivenConsumer(subscribableInputChannel, textMessageHandler);


        Message<String> message = MessageBuilder.withPayload("We have Integration - " + new Date())
                .build();

        // Send message...
        messagingTemplate.send(subscribableInputChannel, message);


        log.info("subscribableInputChannel.getSendCount: {}", subscribableInputChannel.getSendCount());
        log.info("subscribableInputChannel.getSendErrorCount: {}", subscribableInputChannel.getSendErrorCount());


        log.info(LINE);

        assertThat(subscribableInputChannel.getSendCount()).isGreaterThanOrEqualTo(1);

    }


    //-----------------------------------------------------------------------//


} // The End
