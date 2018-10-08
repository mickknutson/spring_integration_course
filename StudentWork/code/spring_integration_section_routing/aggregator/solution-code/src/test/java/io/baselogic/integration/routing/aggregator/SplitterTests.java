package io.baselogic.integration.routing.aggregator;

import io.baselogic.integration.Application;
import io.baselogic.integration.common.TestUtils;
import io.baselogic.integration.routing.aggregator.config.SplitterConfig;
import io.baselogic.integration.routing.aggregator.config.TestConfig;
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

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ContextConfiguration(classes = {TestConfig.class, SplitterConfig.class})

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
@SpringIntegrationTest
@Slf4j
@SuppressWarnings({"Duplicates", "SpringJavaInjectionPointsAutowiringInspection"})
public class SplitterTests {

    private static final String LINE = "+" + new String(new char[40]).replace('\0', '-') + "+";

    private MessagingTemplate messagingTemplate = new MessagingTemplate();

    @Autowired
    private MockIntegrationContext mockIntegrationContext;

    @Autowired
    private TestUtils testUtils;

    @Autowired
    private DirectChannel inputSplitterChannel;

    @Autowired
    private QueueChannel outputSplitterChannel;


    @Before
    public void beforeEachTest(){
        // Reset the channel stats
        inputSplitterChannel.reset();
        outputSplitterChannel.reset();
    }



    //-----------------------------------------------------------------------//


    @Test
    public void test_SPLIT_movies() throws Exception {

        log.info(LINE);

        List<String> movies = testUtils.getMovies(100);

        movies.forEach(
                movie -> {
                    Message<String> message = MessageBuilder.withPayload(movie).build();

                    // Send message...
                    messagingTemplate.send(inputSplitterChannel, message);
                }
        );

        log.info("inputSplitterChannel.getSendCount(): {}", inputSplitterChannel.getSendCount());

        assertThat(inputSplitterChannel.getSendCount()).isEqualTo(100);

        for(int i = 0; i < 300; i++){
            // Receive message with a 1000ms timeout
            GenericMessage<String> result = (GenericMessage<String>) outputSplitterChannel.receive(1_000);

            log.info("\n==> Result: [{}]", result.getPayload());
        }

        log.info("outputChannel messages received: {}", outputSplitterChannel.getReceiveCount());

        assertThat(outputSplitterChannel.getReceiveCount()).isEqualTo(300);

        log.info(LINE);
    }



    //-----------------------------------------------------------------------//


} // The End
