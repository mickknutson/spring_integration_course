package io.baselogic.integration.core.endpoints;


import io.baselogic.integration.Application;
import io.baselogic.integration.common.TestUtils;
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
public class InboundChannelAdapterTests {

    private static final String LINE = "+" + new String(new char[40]).replace('\0', '-') + "+";

    private MessagingTemplate messagingTemplate = new MessagingTemplate();

    @Autowired
    private MockIntegrationContext mockIntegrationContext;


    @Autowired
    private DirectChannel inputChannel;

    @Autowired
    private QueueChannel outputChannel;

    @Autowired
    private PublishSubscribeChannel publishSubscribeChannel;


    //-----------------------------------------------------------------------//

    @Before
    public void beforeEachTest(){
        // prepare for test
        outputChannel.clear();
        outputChannel.reset();
        publishSubscribeChannel.reset();
    }



    //-----------------------------------------------------------------------//


    @Test
    public void test_integration__send_message__withPayload() throws Exception {

        log.info(LINE);

        // NOTE: Messages sent by {@link InboundChannelAdapterConfig.timerMessageSource()}
        //Message<String> message = MessageBuilder.withPayload("We have Integration - " + new Date()).build();

        // Send message...
        //messagingTemplate.send(inputChannel, message);


        // Receive message with a 200ms timeout
        GenericMessage<String> result = (GenericMessage<String>) outputChannel.receive(200);

        outputChannel.receive(200);
        outputChannel.receive(200);
        outputChannel.receive(200);

        log.info(LINE);

        log.info("==> Result: [{}]", result.getPayload());

        log.info("inputChannel.getSendCount: {}", inputChannel.getSendCount());
        log.info("inputChannel.getSendErrorCount: {}", inputChannel.getSendErrorCount());

        log.info(LINE);

        log.info("outputChannel.getQueueSize: {}", outputChannel.getQueueSize());
        log.info("outputChannel.getRemainingCapacity: {}", outputChannel.getRemainingCapacity());
        log.info("outputChannel.getSendCount: {}", outputChannel.getSendCount());
        log.info("outputChannel.getReceiveCount: {}", outputChannel.getReceiveCount());
        log.info("outputChannel.getReceiveErrorCount: {}", outputChannel.getReceiveErrorCount());
        log.info("outputChannel.getSendErrorCount: {}", outputChannel.getSendErrorCount());

        log.info(LINE);

        log.info("publishSubscribeChannel.getSendCount: {}", publishSubscribeChannel.getSendCount());
        log.info("publishSubscribeChannel.getSendErrorCount: {}", publishSubscribeChannel.getSendErrorCount());

        log.info(LINE);

        assertThat(inputChannel.getSendCount()).isGreaterThan(3);

        assertThat(outputChannel.getReceiveCount()).isGreaterThan(3);

    }


    //-----------------------------------------------------------------------//


} // The End
