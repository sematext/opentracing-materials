# opentracing-materials

[OpenTracing](http://opentracing.io/) aims to provide a consistent and tracer-agnostic API for distributed transaction instrumentation. This repository contains a number of examples and scenarios for different programming languages, frameworks, libraries, etc.

# Getting started

Because OpenTracing is just an API/standard, you need to deploy a distributed tracer that is able to ingest/store the spans.

The easiest way to bootstrap a tracer is via Docker. For example, to run the [Zipkin](http://zipkin.io/) server inside container:

```bash
$  docker run -d –name zipkin -p 9411:9411 openzipkin/zipkin
```

After that, Docker daemon will expose the port 9411 on the host machine so you can browse the UI on http://localhost:9411 and explore the traces.

![zipkin ui](https://github.com/sematext/opentracing-materials/blob/master/zipkin-ui.png)

# Java

## opentracing-common

Thin abstraction layer atop OpenTracing API that deals with tracer's initialization, span creation, context injection/extraction, etc. To build the artifact use `mvn`:

```bash
$ mvn clean install
```
Include as dependency in your `pom.xml`:

```
<dependency>
  <groupId>com.sematext.opentracing</groupId>
  <artifactId>opentracing-common</artifactId>
  <version>1.0-SNAPSHOT</version>
</dependency>
```

Tracer's initialization is done via `TracerInitializer` implementation. See the example below:

```java
TracerInitializer tracerInitializer = new TracerInitializer(Tracers.ZIPKIN);
tracerInitializer.setup("localhost", 9411, "component-name");

SpanOperations spanOps = SpanTemplate();
try (ActiveSpan span = spanOps.startActive("create-app")) {
    span.setTag("db.instance", "apps");
    span.setTag("db.type", "sql");
    span.setTag("db.statement", sql);
    jdbcTemplate.update(sql, Collections.singletonMap("name", name));
}
```

## opentracing-jdbc

Spring Boot app that demonstrates how to trace JDBC calls. It exposes a REST API with these two operations:

- **GET** /app (lists current apps)
- **POST** /app/{name} (adds a new app)

Run it with `mvn spring-boot:run`.

The info about apps is persisted in an embedded **H2** database. Use the following commands to see how they trigger the creation of spans with associated OpenTracing tags:

```bash
$ curl -v -XPOST http://localhost:8080/app/slack
$ curl -v -XGET http://localhost:8080/app
```
## opentracing-{injector,extractor}

Demonstrates context propagation capabilities across JVM process boundaries. `opentracing-injector` encodes and injects the span context into HTTP headers (it actually injects `trace` and `span` identifiers). Then, it makes an HTTP request to `opentracing-extractor` which decodes the headers and constructs a propagated span context. The parent span context is used to start a new span inside the trace.

![ctx propagation](https://github.com/sematext/opentracing-materials/blob/master/inject-extract.png)

