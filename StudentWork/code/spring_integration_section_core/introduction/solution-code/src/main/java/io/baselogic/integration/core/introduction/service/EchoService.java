package io.baselogic.integration.core.introduction.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@SuppressWarnings({"Duplicates", "SpringJavaInjectionPointsAutowiringInspection"})
public class EchoService {

    /**
     * Converted from:
     * <service-activator input-channel="inputChannel"
     * 	                   output-channel="outputChannel"
     * 	                   ref="helloService"
     * 	                   method="echo"/>
     * @param inboundPayload
     */
    @ServiceActivator(inputChannel = "inputChannel",
            outputChannel = "outputChannel"
    )
    public String echo(String inboundPayload) {

        log.info("Inbound message: {}", inboundPayload);

	    // TODO: Convert to String parse
		return "Echo: [" + inboundPayload + "]";
	}

    @ServiceActivator(inputChannel = "inputChannel",
            outputChannel = "outputChannel"
    )
    public String FOOBAR(String inboundPayload) {

        log.info("Inbound message: {}", inboundPayload);

	    // TODO: Convert to String parse
		return "Echo: [" + inboundPayload + "]";
	}

} // The End...
