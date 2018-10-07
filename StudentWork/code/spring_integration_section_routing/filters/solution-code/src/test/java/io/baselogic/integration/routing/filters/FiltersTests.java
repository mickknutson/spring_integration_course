package io.baselogic.integration.routing.filters;

import io.baselogic.integration.Application;
import io.baselogic.integration.routing.filters.config.FiltersConfig;
import io.baselogic.integration.routing.filters.config.TestConfig;
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

@ContextConfiguration(classes = {TestConfig.class, FiltersConfig.class})

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
@SpringIntegrationTest
@Slf4j
@SuppressWarnings({"Duplicates", "SpringJavaInjectionPointsAutowiringInspection"})
public class FiltersTests {

    private static final String LINE = "+" + new String(new char[40]).replace('\0', '-') + "+";

    private MessagingTemplate messagingTemplate = new MessagingTemplate();

    @Autowired
    private MockIntegrationContext mockIntegrationContext;


    @Autowired
    private DirectChannel inputChannel;

    @Autowired
    private QueueChannel outputChannel;

    @Autowired
    private QueueChannel discardChannel;


    @Before
    public void beforeEachTest(){
        // Reset the channel stats
        inputChannel.reset();
        outputChannel.reset();
        discardChannel.reset();
    }



    //-----------------------------------------------------------------------//


    @Test
    public void test_Filter__NOT_discarded() throws Exception {

        String messagePayload = "Loan approved";


        log.info(LINE);

        Message<String> message = MessageBuilder.withPayload(messagePayload).build();

        // Send message...
        messagingTemplate.send(inputChannel, message);

        // Receive message with a 1000ms timeout
        GenericMessage<String> result = (GenericMessage<String>) outputChannel.receive(1_000);

        log.info("routingChannel.getSendCount(): {}", inputChannel.getSendCount());
        log.info("outputChannel messages received: {}", outputChannel.getReceiveCount());
        log.info("discardChannel messages received: {}", discardChannel.getReceiveCount());

        assertSoftly(
                softAssertions -> {
                    assertThat(result.getPayload()).isEqualTo(messagePayload);
                    assertThat(inputChannel.getSendCount()).isEqualTo(1);
                    assertThat(outputChannel.getReceiveCount()).isEqualTo(1);
                    assertThat(discardChannel.getReceiveCount()).isEqualTo(0);
                }
        );

        log.info(LINE);
    }


    @Test
    public void test_Filter__DISCARDED() throws Exception {

        String messagePayload = "Loan rejected";


        log.info(LINE);

        Message<String> message = MessageBuilder.withPayload(messagePayload).build();

        // Send message...
        messagingTemplate.send(inputChannel, message);

        // Receive message with a 1000ms timeout
        GenericMessage<String> result = (GenericMessage<String>) discardChannel.receive(1_000);

        log.info("routingChannel.getSendCount(): {}", inputChannel.getSendCount());
        log.info("outputChannel messages received: {}", outputChannel.getReceiveCount());
        log.info("discardChannel messages received: {}", discardChannel.getReceiveCount());

        assertSoftly(
                softAssertions -> {
                    assertThat(result.getPayload()).isEqualTo(messagePayload);
                    assertThat(inputChannel.getSendCount()).isEqualTo(1);
                    assertThat(outputChannel.getReceiveCount()).isEqualTo(0);
                    assertThat(discardChannel.getReceiveCount()).isEqualTo(1);
                }
        );

        log.info(LINE);
    }


    //-----------------------------------------------------------------------//


} // The End
