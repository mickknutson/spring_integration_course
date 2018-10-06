package io.baselogic.integration;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.integration.config.EnableIntegration;
import org.springframework.integration.config.EnableIntegrationManagement;
import org.springframework.integration.http.config.EnableIntegrationGraphController;

import java.util.Arrays;


@SpringBootApplication
@EnableIntegration
@EnableIntegrationManagement
@EnableIntegrationGraphController(allowedOrigins = "http://localhost:8080")

@Slf4j
@SuppressWarnings({"Duplicates", "SpringJavaInjectionPointsAutowiringInspection"})
public class Application {


    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
	}



    private static final String LINE = "+" + new String(new char[78]).replace('\0', '-') + "+";

    //@Profile("trace")
    @Bean
    public CommandLineRunner commandLineRunner(ApplicationContext ctx) {
        return args -> {

            StringBuilder sb = new StringBuilder();

            sb.append("\n\n").append(LINE).append("\n");
            sb.append("Let's inspect the beans provided by Spring Boot:");
            sb.append("\n").append(LINE).append("\n\n");

            String[] beanNames = ctx.getBeanDefinitionNames();
            Arrays.sort(beanNames);
            for (String beanName : beanNames) {
                sb.append(beanName).append("\n");
            }

            sb.append("\n").append(LINE).append("\n");
            sb.append(LINE).append("\n");
            sb.append(LINE).append("\n\n");

            log.trace(sb.toString());

        };
    }

} // The End...
