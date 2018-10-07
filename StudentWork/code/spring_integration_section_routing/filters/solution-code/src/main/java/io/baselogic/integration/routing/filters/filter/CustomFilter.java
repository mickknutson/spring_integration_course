package io.baselogic.integration.routing.filters.filter;

import org.springframework.integration.annotation.Filter;
import org.springframework.integration.annotation.MessageEndpoint;

//@MessageEndpoint
public class CustomFilter {

    @Filter(inputChannel="inputChannel", outputChannel="outputChannel",
            discardChannel="discardChannel")
    public boolean filter(String s) {
        return s.contains("approved");
    }
} // The End...