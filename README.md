# opentracing-materials

[OpenTracing](http://opentracing.io/) aims to provide a consistent and tracer-agnostic API for distributed transaction instrumentation. This repository contains a number of examples and scenarios for different programming languages, frameworks, libraries, etc.

# Getting started

Because OpenTracing is just an API, you need to deploy a distributed tracer that is able to ingest / store the spans.

The easiest way to bootstrap a tracer is via Docker. For example, to run the [Zipkin](http://zipkin.io/) server inside container:

```bash
$  docker run -d –name zipkin -p 9411:9411 openzipkin/zipkin
```

After that, Docker daemon will expose the port 9411 on the host machine so you can browse the UI on http://localhost:9411 and explore the traces.
