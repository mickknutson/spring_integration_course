package io.baselogic.integration.core.endpoints;

import io.baselogic.integration.Application;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.Resource;
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
import static org.junit.Assert.assertEquals;

/**
 * TODO:
 * Need to fix:
 * 1. Clear out messages
 * 2. use MessagingTemplate for sending
 *
 *
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
@SpringIntegrationTest
@Slf4j
@SuppressWarnings({"Duplicates", "SpringJavaInjectionPointsAutowiringInspection"})
public class IntegrationTests {


    private static final String LINE = "+" + new String(new char[40]).replace('\0', '-') + "+";


    private MessagingTemplate messagingTemplate = new MessagingTemplate();

    @Autowired
    private MockIntegrationContext mockIntegrationContext;


    @Autowired
    private MessageChannel inputChannel;

    @Autowired
    private PollableChannel outputChannel;

    @Value("classpath:inputs/movies.csv")
//    @Value("classpath:inputs/products.csv")
    private Resource movies;


    @Before
    public void beforeEachTest(){
        // prepare for test
    }



    //-----------------------------------------------------------------------//


    @Test
    public void test_integration__send_message__withPayload() throws Exception {

        log.info(LINE);


        long expirationLong = Date.from(Instant.now().plus(1, ChronoUnit.DAYS)).getTime();

        Message<String> message = MessageBuilder.withPayload("We have Integration")
                .setExpirationDate(expirationLong)
                .setPriority(42)
                .setHeader("customHeader", "my customHeader")
                .setHeader("chucknorris", "Can divide by zero")
                .build();

        // Send message...
        inputChannel.send(message);

        // Receive message with a 200ms timeout
        GenericMessage<String> result = (GenericMessage<String>) outputChannel.receive(200);

        log.info(LINE);
        log.info("==> Result: [{}]", result.getPayload());
        result.getHeaders().forEach( (k,v) -> log.info("Header [{}] = [{}]", k, v));

        assertThat(result.getPayload()).contains("Echo: [We have Integration]");
        assertThat(result.getHeaders().containsKey("id")).isTrue();
        assertThat(result.getHeaders().containsKey("timestamp")).isTrue();
        assertThat(result.getHeaders().containsKey("customHeader")).isTrue();
        assertThat(result.getHeaders().get("expirationDate")).isEqualTo(expirationLong);
        assertThat(result.getHeaders().get("priority")).isEqualTo(42);
        assertThat(result.getHeaders().get("customHeader")).isEqualTo("my customHeader");
        assertThat(result.getHeaders().get("chucknorris")).isEqualTo("Can divide by zero");

        log.info(LINE);
    }


    @Test
    public void test_integration__send_single_message__fromMessage() throws Exception {

        log.info(LINE);

        Message<String> message = MessageBuilder.withPayload("We have Integration")
                .setPriority(42)
                .setHeader("customHeader", "my customHeader")
                .build();


        Message<String> newMessage = MessageBuilder.fromMessage(message)
                .setHeaderIfAbsent("chucknorris", "Can divide by zero")
                .build();


        // Send message...
        messagingTemplate.send(outputChannel, newMessage);

        // Receive message with a 200ms timeout
        GenericMessage<String> result = (GenericMessage<String>) outputChannel.receive(200);

        log.info(LINE);
        log.info("==> Result: [{}]", result.getPayload());
        result.getHeaders().forEach( (k,v) -> log.info("Header [{}] = [{}]", k, v));

        assertThat(result.getPayload()).contains("We have Integration");

        log.info(LINE);
    }


    //-----------------------------------------------------------------------//


} // The End
