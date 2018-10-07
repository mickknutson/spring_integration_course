package io.baselogic.integration.routing.routers.config;

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
