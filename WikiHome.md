[Wiki page listing](http://code.google.com/p/impala-extensions/w/list)

# Impala Extensions #

The Impala Extensions is a home for extensions to Impala which do not belong in the core project. The extensions project is a collection of potentially unrelated extensions to Impala and Spring.

## Spring MVC Extensions ##

Extensions to Spring MVC's annotation based controller framework.
Includes various `CustomArgumentResolver` implementations, as well an annotation `HandlerAdapter` implementation with support for flash scope as well as other minor enhancements.

[Source](https://impala-extensions.googlecode.com/svn/trunk/impala-extension-mvc)
[Snapshot build](https://impala-extensions.googlecode.com/svn/trunk/maven/repo/org/impalaframework/extension/impala-extension-mvc/0.1-SNAPSHOT)

## Event Framework ##

A framework for publishing and consuming general purpose application events for use within Spring-based applications. Using the event framework allows for greater decoupling of business processes, in a way which is quite similar to JMS. However, as no extra infrastructure is required, it is much more lightweight and simpler to set up than a JMS-based solution.

Supports a variety of event types and use cases, from transient and synchronous events through to persistent and asynchronous events, making it suitable for transactional, enterprise applications.

[Source](https://impala-extensions.googlecode.com/svn/trunk/impala-extension-event)
[Snapshot build](https://impala-extensions.googlecode.com/svn/trunk/maven/repo/org/impalaframework/extension/impala-extension-event/0.1-SNAPSHOT)