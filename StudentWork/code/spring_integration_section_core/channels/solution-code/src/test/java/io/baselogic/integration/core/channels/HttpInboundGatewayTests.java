package io.baselogic.integration.core.channels;

import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.integration.channel.QueueChannel;
import org.springframework.integration.core.MessagingTemplate;
import org.springframework.integration.test.context.MockIntegrationContext;
import org.springframework.integration.test.context.SpringIntegrationTest;
import org.springframework.messaging.PollableChannel;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
@SpringIntegrationTest
@Slf4j
@SuppressWarnings({"Duplicates", "SpringJavaInjectionPointsAutowiringInspection"})
public class HttpInboundGatewayTests {


    private static final String LINE = "+" + new String(new char[40]).replace('\0', '-') + "+";

    @Autowired
    private TestRestTemplate testRestTemplate;


    @Autowired
    private QueueChannel httpInboundResponseChannel;



    @Before
    public void beforeEachTest(){
        // prepare for test
        httpInboundResponseChannel.clear();
    }



    //-----------------------------------------------------------------------//


    @Test
    public void test_integration__send_message__httpInboundGateway() {

        log.info(LINE);

        String postData = "We have Http Inbound Gateway Integration";

        ResponseEntity<String> responseEntity = testRestTemplate.postForEntity("/inbound",
                postData,
                String.class);

        String response = responseEntity.getBody();

        GenericMessage<String> result = (GenericMessage<String>) httpInboundResponseChannel.receive(100);

        log.info(LINE);
        log.info("==> Result: [{}]", result.getPayload());
        result.getHeaders().forEach( (k,v) -> log.info("Header [{}] = [{}]", k, v));

        assertThat(result.getPayload()).contains(postData);
        assertThat(result.getHeaders().containsKey("id")).isTrue();
        assertThat(result.getHeaders().containsKey("timestamp")).isTrue();

        log.info("Response: {}", response);
        assertThat(response).contains(postData);

        log.info(LINE);


    }


    //-----------------------------------------------------------------------//


} // The End
