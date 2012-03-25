package org.impalaframework.extension.root.suite;

import org.impalaframework.extension.root.MessageIntegrationTest;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class AutomatedRootTests extends TestCase {
    public static Test suite() {
        TestSuite suite = new TestSuite();
        suite.addTestSuite(MessageIntegrationTest.class);
        return suite;
    }
}
