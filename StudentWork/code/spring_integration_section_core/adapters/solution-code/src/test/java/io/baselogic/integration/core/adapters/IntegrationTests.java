package io.baselogic.integration.core.adapters;

import io.baselogic.integration.Application;
import io.baselogic.integration.core.adapters.handlers.TextMessageHandler;
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
import org.springframework.integration.test.context.SpringIntegrationTest;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.test.context.junit4.SpringRunner;

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
    private QueueChannel subscribableOutputChannel;

    @Autowired
    private DirectChannel alternateDirectChannel;

    @Autowired
    private TextMessageHandler textMessageHandler;



    //-----------------------------------------------------------------------//

    @Before
    public void beforeEachTest(){
        // prepare for test
        subscribableOutputChannel.clear();
    }



    //-----------------------------------------------------------------------//


    @Test
    public void test_integration__bridging() throws Exception {

        log.info(LINE);

        subscribableInputChannel.subscribe(textMessageHandler);

        alternateDirectChannel.subscribe(
                (message) -> log.info("--> Message: {}", message)
        );

        Message<String> message = MessageBuilder.withPayload("We have Integration - " + new Date())
                .build();

        // Send message...
        messagingTemplate.send(subscribableInputChannel, message);

        log.info(LINE);

        log.info("subscribableInputChannel.getSendCount: {}", subscribableInputChannel.getSendCount());

        log.info(LINE);

        log.info("alternateDirectChannel.getSendCount: {}", alternateDirectChannel.getSendCount());

        log.info(LINE);

        log.info("subscribableOutputChannel.getSendCount: {}", subscribableOutputChannel.getSendCount());
        log.info("subscribableOutputChannel.getReceiveCount: {}", subscribableOutputChannel.getReceiveCount());

        log.info(LINE);

        assertThat(subscribableInputChannel.getSendCount()).isEqualTo(1);
        assertThat(subscribableOutputChannel.getReceiveCount()).isEqualTo(1);
        assertThat(alternateDirectChannel.getSendCount()).isEqualTo(1);

    }


    //-----------------------------------------------------------------------//


} // The End
