/*
 *    Copyright (c) Sematext International
 *    All Rights Reserved
 *
 *    THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF Sematext International
 *    The copyright notice above does not evidence any
 *    actual or intended publication of such source code.
 */
package com.sematext.opentracing;

import brave.Tracing;
import brave.opentracing.BraveTracer;
import com.uber.jaeger.Configuration;
import io.opentracing.Tracer;
import io.opentracing.util.GlobalTracer;
import zipkin.Span;
import zipkin.reporter.AsyncReporter;
import zipkin.reporter.Reporter;
import zipkin.reporter.Sender;
import zipkin.reporter.okhttp3.OkHttpSender;

import static java.lang.String.format;

public class TracerInitializer {

    private Tracers tracerType;
    private Tracer tracer;

    public TracerInitializer(Tracers tracerType) {
        this.tracerType = tracerType;
    }

    /**
     * Setups the OpenTracing compatible tracer system.
     *
     * @param host address or host name where tracer is accepting the ingestion of the spans
     * @param port the port of the tracer's ingestion endpoint
     * @param component in OpenTracing terminology, service name, library, framework, etc. that
     *                  generated the span
     */
    public void setup(String host, int port, String component) {
        switch(tracerType) {
            case ZIPKIN: {
                String endpoint = format("http://%s:%d/api/v1/spans", host, port);
                Sender sender = OkHttpSender.create(endpoint);
                Reporter<Span> reporter = AsyncReporter.builder(sender)
                                            .build();
                // `Brave` is the bridge between Zipkin and OpenTracing API.
                // `BraveTracer.create` returns an OpenTracing compatible tracer impl
                this.tracer = BraveTracer.create(Tracing.newBuilder()
                                          .localServiceName(component)
                                          .reporter(reporter)
                                          .build());
                GlobalTracer.register(tracer);
                break;
            }
            case JAEGER: {
                Configuration config = new Configuration(component,
                        new Configuration.SamplerConfiguration("const", 1),
                        new Configuration.ReporterConfiguration(
                                true,
                                host,
                                port,
                                1000,
                                10000)

                );
                this.tracer = config.getTracer();
                GlobalTracer.register(tracer);
                break;
            }
        }
    }

    public Tracer getTracer() {
        return this.tracer;
    }
}
