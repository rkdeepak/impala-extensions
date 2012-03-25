package org.impalaframework.extension.mvc.annotation.handler;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Marker annotation, used to annotate other annotations for the purpose of determining
 * which handler adapter to apply for a particular controller.
 * @author Phil Zoio
 */
@Target(ElementType.ANNOTATION_TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Handler {

}
