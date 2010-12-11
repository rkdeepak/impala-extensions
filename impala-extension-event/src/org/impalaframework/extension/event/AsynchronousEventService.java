package org.impalaframework.extension.event;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Extension of {@link BaseAsynchronousEventService} which uses a cached thread pool.
 * @author Phil Zoio
 */
public class AsynchronousEventService extends BaseAsynchronousEventService {

	private static final Log logger = LogFactory.getLog(AsynchronousEventService.class);
	
	private ExecutorService taskExecutorService;

	/* ******************** Life cycle methods ****************** */

	protected void doSubmitEvent(Event event) {
		super.addEventToQueue(event);
	}
	
	public void afterStart() {
		logger.info("Starting cached thread pool");
		
		taskExecutorService = Executors.newCachedThreadPool();
	}

	public void beforeStop() {
		logger.info("Stopping cached thread pool");
		
		shutdown(taskExecutorService);
	}
	
	@Override
	protected void submitEventTasks(List<EventTask> eventTaskList) {
		taskExecutorService.submit(new EventTaskList(eventTaskList));
	}
}
