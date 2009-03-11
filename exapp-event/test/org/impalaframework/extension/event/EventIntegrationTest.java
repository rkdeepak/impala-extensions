package org.impalaframework.extension.event;

import org.impalaframework.extension.root.BaseIntegrationTest;
import org.impalaframework.facade.Impala;
import org.impalaframework.interactive.InteractiveTestRunner;
import org.impalaframework.interactive.definition.source.TestDefinitionSource;
import org.impalaframework.module.RootModuleDefinition;



public class EventIntegrationTest extends BaseIntegrationTest {

	public static void main(String[] args) {
		InteractiveTestRunner.run(EventIntegrationTest.class);
	}

	public void testIntegration() throws InterruptedException {
		EventService eventService = Impala.getModuleBean("exapp-event", "eventService", EventService.class);
		EventListenerRegistry asyncRegistry = Impala.getModuleBean("exapp-event", "asyncEventListenerRegistry", EventListenerRegistry.class);
		
		EventListener listener = new EventListener() {

			public String getConsumerName() {
				return "mylistener";
			}

			public boolean getMarkProcessed() {
				System.out.println("checking for marking as processed");
				return true;
			}

			public void onEvent(Event event) {
				System.out.println("Handling event: " + event);
			}
			
		};
		
		asyncRegistry.addListener("myeventtype", listener);
		
		EventType eventType = new EventType("myeventtype");
		
		for (int i = 0; i < 10; i++) {
			Event event = new Event(eventType, "1", "mysubjecttype");
			eventService.submitEvent(event);
		}
		
		//allow events to be processed
		Thread.sleep(1500);
		
		asyncRegistry.removeListener("myeventtype", listener);
		
		//stops this from looping forever
		//Impala.remove("exapp-event");
	}

	public RootModuleDefinition getModuleDefinition() {
		return new TestDefinitionSource("exapp-root", "exapp-event", "exapp-dao").getModuleDefinition();
	}

}