package org.impalaframework.extension.event;

import junit.framework.TestCase;

import org.impalaframework.extension.event.TransactionalEventSynchronizer.EventTransactionalSynchronization;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

public class TransactionalEventSynchronizerTest extends TestCase {

	private TransactionalEventSynchronizer synchronizer;

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		synchronizer = new TransactionalEventSynchronizer();
		clear();
	}
	
	@Override
	protected void tearDown() throws Exception {
		super.tearDown();
		clear();
	}

	private void clear() {
		if (TransactionSynchronizationManager.isSynchronizationActive())
			TransactionSynchronizationManager.clear();
		synchronizer.clear();
		synchronizer.stop();
	}
	
	public void testLifeCycle() throws Exception {
		assertFalse(synchronizer.isRunning());
		synchronizer.start();
		assertTrue(synchronizer.isRunning());
		synchronizer.stop();
		assertFalse(synchronizer.isRunning());
	}
	
	public void testGetTransactionSynchronization() {

		TransactionSynchronizationManager.initSynchronization();
		
		assertFalse(synchronizer.hasTransactionalSynchronization());
		EventTransactionalSynchronization sync = synchronizer.getTransactionSynchronization();
		assertTrue(synchronizer.hasTransactionalSynchronization());
		
		//calling it again returns same object
		assertSame(sync, synchronizer.getTransactionSynchronization());
	}
	
	public void testTestCommit() throws Exception {
		doSynchronizationTest(1, TransactionSynchronization.STATUS_COMMITTED);
	}
	
	public void testTestRollback() throws Exception {
		doSynchronizationTest(0, TransactionSynchronization.STATUS_ROLLED_BACK);
	}
	
	public void testTestUnknown() throws Exception {
		doSynchronizationTest(0, TransactionSynchronization.STATUS_UNKNOWN);
	}

	private void doSynchronizationTest(int expectedEvent, int status)
			throws InterruptedException {
		
		synchronizer.start();
		TransactionSynchronizationManager.initSynchronization();
		TransactionSynchronizationManager.setActualTransactionActive(true);
		EventTransactionalSynchronization sync = synchronizer.getTransactionSynchronization();
		
		EventType eventType = new EventType("mytype");
		Event event = new Event(eventType, "1", "mytype");
		TestEventListener listener = new TestEventListener("asyncListener");
		
		EventTask task = new EventTask(event, listener);

		assertTrue(synchronizer.hasTransactionalSynchronization());
		synchronizer.awaitTransactionCompletion(task);
		sync.afterCompletion(status);
		
		Thread.sleep(100);
		assertEquals(expectedEvent, listener.getEventList().size());
		assertFalse(synchronizer.hasTransactionalSynchronization());
	}

}
