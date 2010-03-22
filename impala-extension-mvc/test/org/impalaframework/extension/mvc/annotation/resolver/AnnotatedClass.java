package org.impalaframework.extension.mvc.annotation.resolver;

import org.impalaframework.extension.mvc.annotation.RequestAttribute;
import org.impalaframework.extension.mvc.annotation.RequestBody;
import org.impalaframework.extension.mvc.annotation.RequestHeader;
import org.impalaframework.extension.mvc.annotation.RequestParameterMap;
import org.impalaframework.extension.mvc.annotation.SessionAttribute;

public class AnnotatedClass {
	
	public void requestAttribute(@RequestAttribute("attribute") String name){}
	public void sessionAttribute(@SessionAttribute("attribute") String name){}
	public void requestHeader(@RequestHeader("attribute") String name){}
	public void requestBody(@RequestBody("UTF8") String name){}
	public void requestParameterMap(@RequestParameterMap String name){}

}
