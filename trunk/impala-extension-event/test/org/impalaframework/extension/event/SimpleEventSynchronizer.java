package org.impalaframework.extension.event;

import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Simple test implementation of {@link EventSynchronizer}
 * @author Phil Zoio
 */
public class SimpleEventSynchronizer implements EventSynchronizer {
	
	private Set<EventTask> submitted = new LinkedHashSet<EventTask>();

	public void awaitTransactionCompletion(final EventTask eventTask) {
		new Thread(new Runnable() {

			public void run() {
				try {
					//sleep for random period of up to a second
					Thread.sleep(10);
					System.out.println("Finished waiting ");
				} catch (InterruptedException e) {
				}
				
				System.out.println("Submitting task");
				submitTask(eventTask);
			}
			
		}).start();
	}
	
	public void submitTask(EventTask eventTask) {
		submitted.add(eventTask);

		System.out.println("Running event task " + eventTask.getEventListener().getConsumerName() + " for event of type " + eventTask.getEvent().getEventType().getType());
		eventTask.run();
	}

	public Set<EventTask> getSubmitted() {
		return submitted;
	}	

}
