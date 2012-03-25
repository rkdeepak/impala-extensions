package org.impalaframework.extension.web;

import java.util.Map;

import org.impalaframework.extension.mvc.annotation.handler.LightweightAdaptable;
import org.impalaframework.extension.root.MessageService;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

@org.springframework.stereotype.Controller
@LightweightAdaptable
@SuppressWarnings("unchecked")
public class MessageController {
    
    private MessageService messageService;
    
    @ModelAttribute("className")
    public String getClassName() {
        return MessageController.class.getName();
    }

    @ModelAttribute("timestamp")
    public Long getTimestamp() {
        return System.currentTimeMillis();
    }
    
    @RequestMapping("/message.htm")
    public String message(
            @ModelAttribute("className") String className,
            @ModelAttribute("timestamp") Long timestamp,
            Map model) {
        
        model.put("message", className + " (" + timestamp + "): " + messageService.getMessage());
        return "test";
    }

    public void setMessageService(MessageService messageService) {
        this.messageService = messageService;
    }
}
