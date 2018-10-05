package io.baselogic.integration.core.introduction;

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
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.PollableChannel;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
@Slf4j
@SuppressWarnings({"Duplicates", "SpringJavaInjectionPointsAutowiringInspection"})
public class EchoTests {


    private static final String LINE = "+" + new String(new char[40]).replace('\0', '-') + "+";


    private MessagingTemplate messagingTemplate;


    @Autowired
    private MessageChannel inputChannel;

    @Autowired
    private PollableChannel outputChannel;


    //-----------------------------------------------------------------------//

    @Before
    public void beforeEachTest(){
        // prepare for test
        messagingTemplate = new MessagingTemplate();
    }



    //-----------------------------------------------------------------------//
    // TESTS


    @Test
    public void test_integration__send_message__from_InputChannel() throws Exception {

        log.info(LINE);

        // Send message...
        inputChannel.send(new GenericMessage<String>("We have Integration"));

        Message<String> title = (GenericMessage) outputChannel.receive(100);

        log.info("==> Result: [{}]", title.getPayload());

        assertThat(title.getPayload()).contains("Echo: [We have Integration]");

        log.info(LINE);
    }


    @Test
    public void test_integration__send_single_message__from_MessagingTemplate() throws Exception {

        log.info(LINE);

        // Send message...
        messagingTemplate.send(outputChannel, new GenericMessage<String>("We have Integration"));

        Message<String> title = (GenericMessage) outputChannel.receive(100);

        log.info("==> Result: [{}]", title.getPayload());

        assertThat(title.getPayload()).contains("We have Integration");

        log.info(LINE);
    }


    //-----------------------------------------------------------------------//


} // The End
