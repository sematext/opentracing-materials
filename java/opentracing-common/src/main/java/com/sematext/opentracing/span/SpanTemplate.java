/*
 *    Copyright (c) Sematext International
 *    All Rights Reserved
 *
 *    THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF Sematext International
 *    The copyright notice above does not evidence any
 *    actual or intended publication of such source code.
 */
package com.sematext.opentracing.span;

import com.sematext.opentracing.SpanOperations;
import com.sematext.opentracing.carrier.HttpHeadersExtractAdapter;
import com.sematext.opentracing.carrier.HttpHeadersInjectAdapter;
import io.opentracing.ActiveSpan;
import io.opentracing.Span;
import io.opentracing.SpanContext;
import io.opentracing.Tracer;
import io.opentracing.propagation.Format;
import org.springframework.http.HttpHeaders;

import java.util.Map;

public class SpanTemplate implements SpanOperations {

    private Tracer tracer;

    public SpanTemplate(Tracer tracer) {
        this.tracer = tracer;
    }

    /**
     * Do custom work inside the closure.
     *
     * @param callback closure
     */
    @Override
    public void doInTracer(SpanCallback callback) {
        callback.doInTracer(tracer);
    }

    /**
     * Starts a new span.
     *
     * @param name name of the span
     * @return the instance of the class that implements {@link Span}
     */
    @Override
    public Span start(String name) {
        return tracer.buildSpan(name).startManual();
    }

    /**
     * Starts a new span that's automatically registered for intra-process propagation.
     *
     * @param name name of the span
     * @return an instance of the class that implements {@link ActiveSpan}
     */
    @Override
    public ActiveSpan startActive(String name) {
        return tracer.buildSpan(name)
                        .startActive();
    }

    /**
     * Starts a new span that's automatically registered for intra-process propagation and
     * add the reference to a parent span.
     *
     * @param name name of the span
     * @param parent the instance of the {@link ActiveSpan} that represents the parent span
     * @return an instance of the class that implements {@link ActiveSpan}
     */
    @Override
    public ActiveSpan startActive(String name, ActiveSpan parent) {
        return tracer.buildSpan(name)
                        .asChildOf(parent)
                        .startActive();
    }

    /**
     * Starts a new span that's automatically registered for intra-process propagation and
     * add the reference to a parent span as {@link SpanContext}.
     *
     * @param name name of the span
     * @param parent the instance of the {@link ActiveSpan} that represents the parent span
     * @return an instance of the class that implements {@link ActiveSpan}
     */
    @Override
    public ActiveSpan startActive(String name, SpanContext parent) {
        return tracer.buildSpan(name)
                        .asChildOf(parent)
                        .startActive();
    }

    /**
     * Injects a SpanContext into a {@link io.opentracing.propagation.TextMap} carrier
     * that can be propagated to another process via HTTP headers.
     *
     * @param ctx span context
     * @param headers the carrier
     */
    @Override
    public void inject(SpanContext ctx, HttpHeaders headers) {
        HttpHeadersInjectAdapter injectAdapter = new HttpHeadersInjectAdapter(headers);
        tracer.inject(ctx, Format.Builtin.HTTP_HEADERS, injectAdapter);
    }

    /**
     * Decodes the content of the HTTP headers to extract the SpanContext.
     *
     * @param map payload as extracted from the headers
     * @return an instance of {@link SpanContext}
     */
    @Override
    public SpanContext extract(Map<String, Object> map) {
        HttpHeadersExtractAdapter extractAdapter = new HttpHeadersExtractAdapter(map);
        return tracer.extract(Format.Builtin.HTTP_HEADERS, extractAdapter);
    }
}
