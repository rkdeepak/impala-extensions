package org.impalaframework.extension.event.suite;

import org.impalaframework.extension.event.DefaultEventListenerRegistryTest;
import org.impalaframework.extension.event.EventListenerContributorTest;
import org.impalaframework.extension.event.DefaultEventServiceTest;
import org.impalaframework.extension.event.EventServiceTest;
import org.impalaframework.extension.event.EventTaskTest;
import org.impalaframework.extension.event.RecordingEventTaskTest;
import org.impalaframework.extension.event.SimpleEventSynchronizerTest;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class AutomatedEventTests extends TestCase {

	public static Test suite() {
		TestSuite suite = new TestSuite();
		suite.addTestSuite(DefaultEventListenerRegistryTest.class);
		suite.addTestSuite(EventListenerContributorTest.class);
		suite.addTestSuite(DefaultEventServiceTest.class);
		suite.addTestSuite(EventServiceTest.class);
		suite.addTestSuite(EventTaskTest.class);
		suite.addTestSuite(RecordingEventTaskTest.class);
		suite.addTestSuite(SimpleEventSynchronizerTest.class);
		return suite;
	}
}
