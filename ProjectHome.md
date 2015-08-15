This project contains extensions to [Impala](http://impala.googlecode.com), a dynamic modular application development framework based on [Spring](http://www.springsource.org/about).

The idea for this project is to host web framework integrations and other extensions to Impala which are generic enough to be useful across multiple projects, but are not sufficiently "core" to be included in the main Impala code base.

## Extension Modules ##

### Spring MVC Extensions ###

Extensions to Spring MVC's annotation based controller framework.
Includes various `CustomArgumentResolver` implementations, as well an annotation `HandlerAdapter` implementation with support for flash scope as well as other minor enhancements.

[Source](https://impala-extensions.googlecode.com/svn/trunk/impala-extension-mvc)
[Snapshot build](https://impala-extensions.googlecode.com/svn/trunk/maven/repo/org/impalaframework/extension/impala-extension-mvc/0.1-SNAPSHOT)

### Event Framework ###

A framework for publishing and consuming general purpose application events for use within Spring-based applications. Using the event framework allows for greater decoupling of business processes, in a way which is quite similar to JMS. However, as no extra infrastructure is required, it is much more lightweight and simpler to set up than a JMS-based solution.

Supports a variety of event types and use cases, from transient and synchronous events through to persistent and asynchronous events, making it suitable for transactional, enterprise applications.

[Source](https://impala-extensions.googlecode.com/svn/trunk/impala-extension-event)
[Snapshot build](https://impala-extensions.googlecode.com/svn/trunk/maven/repo/org/impalaframework/extension/impala-extension-event/0.1-SNAPSHOT)