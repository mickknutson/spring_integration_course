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
     *
     * <service-activator input-channel="inputChannel"
     * 	                   output-channel="outputChannel"
     * 	                   ref="echoService"
     * 	                   method="echo"/>
     *
     * @param inboundPayload
     */
    @ServiceActivator(inputChannel = "inputChannel",
            outputChannel = "outputChannel"
    )
    public String echo(String inboundPayload) {

        log.info("EchoService.echo():: Inbound message: [{}]", inboundPayload);

        return String.format("Echo: [%s]", inboundPayload);
	}

} // The End...
