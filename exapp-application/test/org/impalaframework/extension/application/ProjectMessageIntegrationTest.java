package org.impalaframework.extension.application;

import org.impalaframework.facade.Impala;
import org.impalaframework.interactive.definition.source.TestDefinitionSource;
import org.impalaframework.module.RootModuleDefinition;
import org.impalaframework.interactive.InteractiveTestRunner;

import org.impalaframework.extension.root.BaseIntegrationTest;
import org.impalaframework.extension.root.MessageService;



public class ProjectMessageIntegrationTest extends BaseIntegrationTest {

    public static void main(String[] args) {
        InteractiveTestRunner.run(ProjectMessageIntegrationTest.class);
    }

    public void testIntegration() {
        MessageService service = Impala.getModuleBean("exapp-application", "messageService", MessageService.class);
        System.out.println(service.getMessage());
    }

    public RootModuleDefinition getModuleDefinition() {
        return new TestDefinitionSource("exapp-root", "exapp-application").getModuleDefinition();
    }

}
