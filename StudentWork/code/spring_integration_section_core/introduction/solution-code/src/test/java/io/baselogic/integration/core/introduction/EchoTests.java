package io.baselogic.integration.core.introduction;

import io.baselogic.integration.Application;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.integration.core.MessagingTemplate;
import org.springframework.integration.test.context.MockIntegrationContext;
import org.springframework.integration.test.context.SpringIntegrationTest;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.PollableChannel;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
@SpringIntegrationTest
@Slf4j
@SuppressWarnings({"Duplicates", "SpringJavaInjectionPointsAutowiringInspection"})
public class EchoTests {


    private static final String LINE = "+" + new String(new char[40]).replace('\0', '-') + "+";


    private MessagingTemplate template;


    @Autowired
    private MockIntegrationContext mockIntegrationContext;

    @Autowired
    private MessageChannel inputChannel;

    @Autowired
    private PollableChannel outputChannel;


    //-----------------------------------------------------------------------//

    @Before
    public void beforeEachTest(){
        // prepare for test
        template = new MessagingTemplate();
    }



    //-----------------------------------------------------------------------//
    // TESTS


    @Test
    public void test_integration__send_message__from_InputChannel() throws Exception {

        log.info(LINE);

        GenericMessage<String> message = new GenericMessage<>("We have Integration");

        // Send message...
        inputChannel.send(message);

        // Receive message with a 200ms timeout
        GenericMessage<String> title = (GenericMessage<String>) outputChannel.receive(1_000);

        log.info("==> Result: [{}]", title.getPayload());

        assertThat(title.getPayload()).contains("Echo: [We have Integration]");

        log.info(LINE);
    }


    @Test
    public void test_integration__send_single_message__from_MessagingTemplate() throws Exception {

        log.info(LINE);

        GenericMessage<String> message = new GenericMessage<>("We have Integration");

        // Send message...
        template.send(outputChannel, message);

        // Receive message with a 200ms timeout
        GenericMessage<String> title = (GenericMessage<String>) outputChannel.receive(1_000);

        log.info("==> Result: [{}]", title.getPayload());

        assertThat(title.getPayload()).contains("We have Integration");

        log.info(LINE);
    }


    //-----------------------------------------------------------------------//


} // The End
