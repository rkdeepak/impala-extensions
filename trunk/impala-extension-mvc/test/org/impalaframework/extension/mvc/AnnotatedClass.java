package org.impalaframework.extension.mvc;

public class AnnotatedClass {
	
	public void requestAttribute(@RequestAttribute("attribute") String name){}
	public void sessionAttribute(@SessionAttribute("attribute") String name){}
	public void requestHeader(@RequestHeader("attribute") String name){}
	public void requestBody(@RequestBody("UTF8") String name){}
	public void requestParameterMap(@RequestParameterMap String name){}

}
