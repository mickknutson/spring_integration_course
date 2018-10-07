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
    private MockIntegrationContext mockIntegrationContext;


    @Autowired
    private DirectChannel inputChannel;

    @Autowired
    private QueueChannel outputChannel;


    @Before
    public void beforeEachTest(){
        // prepare for test
        outputChannel.clear();
    }



    //-----------------------------------------------------------------------//


    @Test
    public void test_integration__send_message__withPayload() throws Exception {

        long expirationLong = Date.from(Instant.now().plus(1, ChronoUnit.DAYS)).getTime();

        Message<String> message = MessageBuilder.withPayload("We have Integration")
                .setExpirationDate(expirationLong)
                .setPriority(42)
                .setHeader("customHeader", "my customHeader")
                .setHeader("chucknorris", "Can divide by zero")
                .build();

        // Send message...
        messagingTemplate.send(inputChannel, message);

        // Receive message with a 200ms timeout
        GenericMessage<String> result = (GenericMessage<String>) outputChannel.receive(200);


        assertThat(result.getPayload()).contains("Echo: [We have Integration]");
        assertThat(result.getHeaders().containsKey("id")).isTrue();
        assertThat(result.getHeaders().containsKey("timestamp")).isTrue();
        assertThat(result.getHeaders().containsKey("customHeader")).isTrue();
        assertThat(result.getHeaders().get("expirationDate")).isEqualTo(expirationLong);
        assertThat(result.getHeaders().get("priority")).isEqualTo(42);
        assertThat(result.getHeaders().get("customHeader")).isEqualTo("my customHeader");
        assertThat(result.getHeaders().get("chucknorris")).isEqualTo("Can divide by zero");

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



    @Test
    public void test_integration__send_multiple_messages() throws Exception {

        log.info(LINE);

        // 5-names
        String[] data = new String[]{
                "Mick Knutson",
                "Chuck Norris",
                "Bob Burgers",
                "Han Solo",
                "Sheldon Cooper",

                // Cannot send more than the Queue capacity.
                // The will block until the timeout
                // Then fail:

//                "Joe Dirt",
        };

        for(String item: data){

            Message<String> message = MessageBuilder.withPayload(item)
                    .build();

            // Send message...
            // NOTE: If the channel is full, then the messagingTemplate does not timeout and blocks!
//            messagingTemplate.send(outputChannel, message);

            // Send a message with a timeout:
            inputChannel.send(message, 1_000);
        }

        log.info("inputChannel messages sent: {}", inputChannel.getSendCount());
        assertThat(inputChannel.getSendCount()).isEqualTo(data.length);


        for(int i = 0; i < data.length; i++){

            // Receive message with a 200ms timeout
            GenericMessage<String> result = (GenericMessage<String>) outputChannel.receive(200);

            log.info("==> Result: [{}]", result.getPayload());
            assertThat(result.getPayload()).contains(data[i]);

        }


        log.info("outputChannel messages received: {}", outputChannel.getReceiveCount());
        log.info("getRemainingCapacity: {}", outputChannel.getRemainingCapacity());
        log.info("getRemainingCapacity: {}", outputChannel.getQueueSize());


        assertThat(outputChannel.getReceiveCount()).isEqualTo(data.length);

        log.info(LINE);
    }


    //-----------------------------------------------------------------------//


} // The End
