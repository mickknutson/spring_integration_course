# *Spring Integration Master Class*

##  *Section: Code Root*

*The directory is the root for the Labs and Solutions for the Spring Integration Master Class*

---

### *Sections in this course:*

 - **[Core Concepts](https://github.com/mickknutson/spring_integration_course/tree/master/StudentWork/code/spring_integration_section_core/)**

- **Summary**

---


## *NOTES*


**The code in this course is based on Maven, Java and Spring Boot 2**

[Integration testing reference](https://docs.spring.io/spring-integration/docs/current/reference/html/testing.html)

---

### Lomok annotation processor is used. You can setup your IDE specifically:
- https://projectlombok.org/setup/eclipse
- https://projectlombok.org/setup/intellij


## TO REVISIT


Channels:

    DirectChannel (setFailover)

    ExecutorChannel
    PublishSubscribeChannel
    FixedSubscriberChannel
    RendezvousChannel

    ChannelPurger

    NullChannel ??

    IntegrationFlowBuilder

Sending Messages

    @Publisher

    @MessageEndpoint
    @InboundChannelAdapter
    @ServiceActivator
    @IdempotentReceiver ??


Consumers

    PollingConsumer

    EventDrivenConsumer

SourcePollingChannelAdapter


Other operations to implement:

    @UseSpelInvoker
    @Transformer
    @Splitter
    @Aggregator
    @Router
    @Filter

    @Gateway
    @MessagingGateway

    @BridgeTo
    @BridgeFrom



What is this for:

    @Poller

Need to implement for Testing:

    MockIntegrationContext



## The End...
