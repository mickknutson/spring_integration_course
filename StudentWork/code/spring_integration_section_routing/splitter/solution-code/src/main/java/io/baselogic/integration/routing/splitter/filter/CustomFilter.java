package io.baselogic.integration.routing.splitter.filter;

import org.springframework.integration.annotation.Filter;

//@MessageEndpoint
public class CustomFilter {

    @Filter(inputChannel="inputChannel", outputChannel="outputChannel",
            discardChannel="discardChannel")
    public boolean filter(String s) {
        return s.contains("approved");
    }
} // The End...