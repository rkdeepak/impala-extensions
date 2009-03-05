package org.impalaframework.extension.dao;

import org.impalaframework.extension.event.EventDAO;
import org.impalaframework.extension.root.BaseIntegrationTest;
import org.impalaframework.facade.Impala;
import org.impalaframework.interactive.InteractiveTestRunner;
import org.impalaframework.interactive.definition.source.TestDefinitionSource;
import org.impalaframework.module.RootModuleDefinition;

public class DAOIntegrationTest extends BaseIntegrationTest {

	public static void main(String[] args) {
		InteractiveTestRunner.run(DAOIntegrationTest.class);
	}

	public void testIntegration() {
		EventDAO service = Impala.getModuleBean("exapp-dao", "eventDAO", EventDAO.class);
		service.insertProcessedEvent("myid", "myconsumer");
	}

	public RootModuleDefinition getModuleDefinition() {
		return new TestDefinitionSource("exapp-root", "exapp-dao").getModuleDefinition();
	}

}