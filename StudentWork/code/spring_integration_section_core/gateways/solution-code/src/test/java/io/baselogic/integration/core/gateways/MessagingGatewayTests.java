package io.baselogic.integration.core.gateways;

import io.baselogic.integration.Application;
import io.baselogic.integration.common.TestUtils;
import io.baselogic.integration.core.gateways.config.MessagingGatewayConfig;
import io.baselogic.integration.core.gateways.config.TestConfig;
import io.baselogic.integration.core.gateways.gateway.TextMessageGateway;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.channel.PriorityChannel;
import org.springframework.integration.channel.QueueChannel;
import org.springframework.integration.core.MessagingTemplate;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.integration.test.context.MockIntegrationContext;
import org.springframework.integration.test.context.SpringIntegrationTest;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.SoftAssertions.assertSoftly;
import static org.junit.Assert.assertEquals;

@ContextConfiguration(classes = {TestConfig.class, MessagingGatewayConfig.class})

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
@SpringIntegrationTest
@Slf4j
@SuppressWarnings({"Duplicates", "SpringJavaInjectionPointsAutowiringInspection"})
public class MessagingGatewayTests {


    private static final String LINE = "+" + new String(new char[40]).replace('\0', '-') + "+";

    private MessagingTemplate messagingTemplate = new MessagingTemplate();

    @Autowired
    private MockIntegrationContext mockIntegrationContext;


    @Autowired
    private DirectChannel inputChannel;

    @Autowired
    private DirectChannel inputUpperCaseChannel;

    @Autowired
    private QueueChannel outputChannel;

    @Autowired
    private TextMessageGateway gateway;

    @Before
    public void beforeEachTest(){
        // Reset the channel stats
        inputChannel.reset();
        outputChannel.reset();
        inputUpperCaseChannel.reset();
    }



    //-----------------------------------------------------------------------//


    @Test
    public void test_TextMessagingGateway_echo() throws Exception {

        log.info(LINE);

        String result = gateway.echo("We have Integration");
//        outputChannel.receive(1000);

        log.info(LINE);
        log.info("==> Result: [{}]", result);

        log.info("inputChannel.getSendCount(): {}", inputChannel.getSendCount());
        log.info("inputUpperCaseChannel.getSendCount(): {}", inputUpperCaseChannel.getSendCount());

        log.info("outputChannel messages received: {}", outputChannel.getReceiveCount());

        assertSoftly(
                softAssertions -> {
                    assertThat(result).isEqualTo("Echo: [We have Integration]");
                    assertThat(inputChannel.getSendCount()).isEqualTo(1);
                    assertThat(outputChannel.getReceiveCount()).isGreaterThanOrEqualTo(1);
                }
        );

        log.info(LINE);
    }


    @Test
    public void test_TextMessagingGateway_echoUpperCase() throws Exception {

        log.info(LINE);

        String result = gateway.echoUpperCase("We have Integration");
//        outputChannel.receive(1_000);

        log.info(LINE);
        log.info("==> Result: [{}]", result);

        log.info("inputChannel.getSendCount(): {}", inputChannel.getSendCount());
        log.info("inputUpperCaseChannel.getSendCount(): {}", inputUpperCaseChannel.getSendCount());

        log.info("outputChannel messages received: {}", outputChannel.getReceiveCount());

        assertSoftly(
                softAssertions -> {
                    assertThat(result).isEqualTo("Echo UpperCase: [WE HAVE INTEGRATION]");

                    assertThat(inputUpperCaseChannel.getSendCount()).isEqualTo(1);
                    assertThat(outputChannel.getReceiveCount()).isGreaterThanOrEqualTo(1);
                }
        );


        log.info(LINE);
    }



    //-----------------------------------------------------------------------//


} // The End
