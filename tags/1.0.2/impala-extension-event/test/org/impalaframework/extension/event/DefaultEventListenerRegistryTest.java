package org.impalaframework.extension.event;
import java.util.List;

import org.springframework.core.Ordered;
import org.springframework.util.StringUtils;

import junit.framework.TestCase;

public class DefaultEventListenerRegistryTest extends TestCase {

    private EventListenerRegistry registry;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        
        registry = new DefaultEventListenerRegistry();
    }
    
    public void testAddRemoveListener() {

        EventListener[] listeners = EventTestUtils.listeners();
        
        registry.addListener("mytype", listeners[0]);
        registry.addListener("anothertype", listeners[1]);
        registry.addListener("mytype", listeners[2]);
        
        assertEquals(2, registry.getListenerCountForType("mytype"));
        assertEquals(1, registry.getListenerCountForType("anothertype"));
        
        assertTrue(registry.removeListener("mytype", listeners[0]));
        assertFalse(registry.removeListener("mytype", listeners[1]));
        assertTrue(registry.removeListener("mytype", listeners[2]));
        
        assertTrue(registry.removeListener("anothertype", listeners[1]));
        assertFalse(registry.removeListener("anothertype", listeners[0]));
        
        assertEquals(0, registry.getListenerCountForType("mytype"));
        assertEquals(0, registry.getListenerCountForType("anothertype"));
        assertEquals(0, registry.getListenerCount());
    }
    
    public void testOrdering() throws Exception {
        registry.addListener("type", new OrderdEventListener("first",1));
        registry.addListener("type", new OrderdEventListener("second",2));
        registry.addListener("type", new OrderdEventListener("third",2));
        final List<EventListener> eventListeners = registry.getEventListeners("type");
        assertEquals("first,second,third", StringUtils.collectionToCommaDelimitedString(eventListeners));
    }
    
    public void testRandomOrdering() throws Exception {
        registry.addListener("type", new OrderdEventListener("second",2));
        registry.addListener("type", new OrderdEventListener("fourth",null));
        registry.addListener("type", new OrderdEventListener("third",3));
        registry.addListener("type", new OrderdEventListener("first",1));
        final List<EventListener> eventListeners = registry.getEventListeners("type");
        assertEquals("first,second,third,fourth", StringUtils.collectionToCommaDelimitedString(eventListeners));
    }

    class OrderdEventListener implements EventListener {
        
        private Integer order;

        private String name;
        
        public OrderdEventListener(String name, Integer order) {
            super();
            this.name = name;
            this.order = order;
        }

        public String getConsumerName() {
            return null;
        }

        public boolean getMarkProcessed() {
            return false;
        }

        public void onEvent(Event event) {
        }

        public int getOrder() {
            return (order != null ? order : Ordered.LOWEST_PRECEDENCE);
        }
        
        @Override
        public String toString() {
            return name;
        }
        
    }
}
