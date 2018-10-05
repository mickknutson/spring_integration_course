package io.baselogic.integration.core.introduction;

import io.baselogic.integration.Application;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.integration.core.MessagingTemplate;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.PollableChannel;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.StringTokenizer;
import java.util.stream.Collectors;

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
@Slf4j
@SuppressWarnings({"Duplicates", "SpringJavaInjectionPointsAutowiringInspection"})
public class EchoTests {


    private static final String LINE = "+" + new String(new char[40]).replace('\0', '-') + "+";


    private MessagingTemplate messagingTemplate = new MessagingTemplate();


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



//    @Test
    public void test_integration__send_multiple_messages() throws Exception {

        log.info(LINE);

        // Send messages...

        List<String> titles = getTitlesFromResource();
        log.info("Send {} titles as messages", titles.size());
        titles.forEach( t -> {
            inputChannel.send(new GenericMessage<String>(t));
        });


        log.info("Receive {} titles", titles.size());
        Message<String> title = (GenericMessage) outputChannel.receive(300);

        log.info("==> Echo Result: [{}]", title.getPayload());

        assertThat("foobar").contains("bar");

        log.info(LINE);
    }


    public List<String> getTitlesFromResource()
            throws IOException {

        InputStream resource = movies.getInputStream();

        try (
                BufferedReader reader = new BufferedReader(
                new InputStreamReader(resource))
        ) {

            //   title,release_date,tagline

            List<String> titles = reader.lines().skip(1)
                    .map(m -> m.substring(0, m.indexOf(',')) )
                    .sorted()
                    .collect(Collectors.toList());

//            titles.forEach(System.out::println);
            return titles;
        }
    }
/*
    public List<String> getTokensFromFile(String path , String delim ) {

        List<String> tokens = new ArrayList<>();

        String currLine = "";

        StringTokenizer tokenizer;

        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(Application.class.getResourceAsStream(
                        "/" + path )))) {
            while (( currLine = br.readLine()) != null ) {

                tokenizer = new StringTokenizer( currLine , delim );
                while (tokenizer.hasMoreElements()) {
                    tokens.add(tokenizer.nextToken());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return tokens;
    }*/


    //-----------------------------------------------------------------------//


} // The End
