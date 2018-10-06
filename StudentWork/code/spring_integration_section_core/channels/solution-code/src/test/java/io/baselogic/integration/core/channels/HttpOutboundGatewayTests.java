package io.baselogic.integration.core.channels;

import io.baselogic.integration.Application;
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
public class HttpOutboundGatewayTests {


    private static final String LINE = "+" + new String(new char[40]).replace('\0', '-') + "+";

    private MessagingTemplate messagingTemplate = new MessagingTemplate();

    @Autowired
    private MockIntegrationContext mockIntegrationContext;

    @Autowired
    private DirectChannel httpOutboundRequestChannel;

    @Autowired
    private QueueChannel httpOutboundResponseChannel;


    @Before
    public void beforeEachTest(){
        // prepare for test
        httpOutboundResponseChannel.clear();
    }



    //-----------------------------------------------------------------------//

    @Test
    public void test_integration__send_message__httpOutboundGateway() throws Exception {

        log.info(LINE);

        Message<String> message = MessageBuilder.withPayload("We have HTTP Outbound Integration")
                .setPriority(42)
                .setHeader("customHeader", "my customHeader")
                .setHeader("chucknorris", "Can divide by zero")
                .build();

        // Send message...
        messagingTemplate.send(httpOutboundRequestChannel, message);

        // Receive message with a 200ms timeout
        GenericMessage<String> result = (GenericMessage<String>) httpOutboundResponseChannel.receive(200);

        log.info(LINE);
        log.info("==> Result: [{}]", result.getPayload());
        result.getHeaders().forEach( (k,v) -> log.info("Header [{}] = [{}]", k, v));

        assertThat(result.getPayload()).contains("\"data\": \"We have HTTP Outbound Integration\"");

        log.info(LINE);
    }


    //-----------------------------------------------------------------------//


} // The End
