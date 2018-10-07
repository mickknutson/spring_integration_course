package io.baselogic.integration.core.channels;

import io.baselogic.integration.Application;
import io.baselogic.integration.common.TestUtils;
import io.baselogic.integration.core.channels.config.PriorityChannelConfig;
import io.baselogic.integration.core.channels.config.TestConfig;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.integration.channel.PriorityChannel;
import org.springframework.integration.core.MessagingTemplate;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.integration.test.context.SpringIntegrationTest;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ContextConfiguration(classes = {TestConfig.class, PriorityChannelConfig.class})

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
@SpringIntegrationTest
@Slf4j
@SuppressWarnings({"Duplicates", "SpringJavaInjectionPointsAutowiringInspection"})
public class PriorityChannelTests {


    private static final String LINE = "+" + new String(new char[40]).replace('\0', '-') + "+";

    private MessagingTemplate messagingTemplate = new MessagingTemplate();

    @Autowired
    private TestUtils testUtils;

    @Autowired
    private PriorityChannel priorityChannel;



    @Before
    public void beforeEachTest(){
        // prepare for test
        priorityChannel.reset();
    }



    //-----------------------------------------------------------------------//


    @Test
    public void test_integration__send_message__priorityChannel() throws Exception {

        log.info(LINE);

        List<String> titles = testUtils.getMovieTitles(100);

        titles.forEach(
                t -> {
                    Message<String> message = MessageBuilder.withPayload(t).build();

                    // Send message...
                    messagingTemplate.send(priorityChannel, message);
                }
        );

        for(int i = 0; i < titles.size(); i++){

            // Receive message with a 200ms timeout
            GenericMessage<String> result = (GenericMessage<String>) priorityChannel.receive(200);

            log.info("==> Result: [{}]", result.getPayload());
        }

        log.info("priorityChannel.getReceiveCount(): {}", priorityChannel.getReceiveCount());


        assertThat(priorityChannel.getReceiveCount()).isEqualTo(100);


        log.info(LINE);
    }



    //-----------------------------------------------------------------------//


} // The End
