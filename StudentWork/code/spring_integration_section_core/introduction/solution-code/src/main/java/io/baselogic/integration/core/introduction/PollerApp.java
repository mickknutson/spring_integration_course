package io.baselogic.integration.core.introduction;

import org.springframework.context.support.ClassPathXmlApplicationContext;

public class PollerApp {

	/**
	 * Simple application that polls the current system time 2 times every
	 * 20 seconds (20000 milliseconds).
	 *
	 * The resulting message contains the time in milliseconds and the message
	 * is routed to a Logging Channel Adapter which will print the time to the
	 * command prompt.
	 *
	 * @param args Not used.
	 */
	@SuppressWarnings("resource")
	public static void main(String[] args) throws Exception{
		new ClassPathXmlApplicationContext("META-INF/spring/integration/delay.xml");
	}

} // The End...
