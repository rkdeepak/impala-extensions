package org.impalaframework.extension.mvc.annotation.collector;

import static org.easymock.EasyMock.createMock;
import junit.framework.TestCase;

import org.springframework.beans.BeanWrapperImpl;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.web.context.request.NativeWebRequest;

public class ModelArgumentCollectorTest extends TestCase {

	public void testGetArgument() {
		ModelArgumentCollector collector = new ModelArgumentCollector();
		ExtendedModelMap implicitModel = new ExtendedModelMap();
		assertSame(implicitModel, collector.getArgument(createMock(NativeWebRequest.class), implicitModel, new BeanWrapperImpl()));
	}

}
