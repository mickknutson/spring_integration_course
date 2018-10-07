package io.baselogic.integration.routing.routers.gateway;

import org.springframework.integration.annotation.Gateway;
import org.springframework.integration.annotation.GatewayHeader;
import org.springframework.integration.annotation.MessagingGateway;


@MessagingGateway(name = "textMessageGateway",
        defaultRequestChannel = "inputChannel",
        defaultReplyChannel = "outputChannel",
        defaultHeaders = @GatewayHeader(name = "calledMethod",
                expression="#gatewayMethod.name")
)
public interface TextMessageGateway {

    @Gateway(requestChannel = "inputChannel", replyTimeout = 1000, requestTimeout = 1000)
    String echo(String payload);

    @Gateway(requestChannel = "inputUpperCaseChannel", headers = @GatewayHeader(name = "thing1", value="thing2"))
    String echoUpperCase(String payload);


} // The End...
