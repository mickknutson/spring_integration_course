package io.baselogic.integration.core.introduction.config;

import org.apache.activemq.command.ActiveMQQueue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.jms.Queue;

@Configuration
public class MessagingConfig {

    public static final String HELLO_QUEUE = "hello.queue";
    @Bean
    public Queue helloJMSQueue() {
        return new ActiveMQQueue(HELLO_QUEUE);
    }


}
