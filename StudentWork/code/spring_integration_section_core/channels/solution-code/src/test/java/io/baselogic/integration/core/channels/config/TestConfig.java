package io.baselogic.integration.core.channels.config;

import io.baselogic.integration.common.TestUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TestConfig {


    @Bean
    public TestUtils testUtils(){
        return new TestUtils();
    }

} // The End...
