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
import io.opentracing.ActiveSpan;
import io.opentracing.Span;
import io.opentracing.Tracer;
import io.opentracing.util.GlobalTracer;

public class SpanTemplate implements SpanOperations {

    /**
     * Returns the current Tracer.
     *
     * @return returns the reference to {@link GlobalTracer} or an instance
     * of a class which implements the {@link Tracer} interface.
     */
    @Override
    public Tracer tracer() {
        return GlobalTracer.get();
    }

    /**
     * Do custom work inside the closure.
     *
     * @param callback closure
     */
    @Override
    public void doInTracer(SpanCallback callback) {
        callback.doInTracer(tracer());
    }

    /**
     * Starts a new span.
     *
     * @param name name of the span
     * @return the instance of the class that implements {@link Span}
     */
    @Override
    public Span start(String name) {
        return tracer().buildSpan(name).startManual();
    }

    /**
     * Starts a new span that's automatically registered for intra-process propagation.
     *
     * @param name name of the span
     * @return an instance of the class that implements {@link ActiveSpan}
     */
    @Override
    public ActiveSpan startActive(String name) {
        return tracer().buildSpan(name)
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
        return tracer().buildSpan(name)
                        .asChildOf(parent)
                        .startActive();
    }

}
