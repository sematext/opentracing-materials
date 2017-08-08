/*
 *    Copyright (c) Sematext International
 *    All Rights Reserved
 *
 *    THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF Sematext International
 *    The copyright notice above does not evidence any
 *    actual or intended publication of such source code.
 */
package com.sematext.opentracing;

import com.sematext.opentracing.span.SpanCallback;
import io.opentracing.ActiveSpan;
import io.opentracing.Span;
import io.opentracing.Tracer;

public interface SpanOperations {

    Tracer tracer();

    void doInTracer(SpanCallback callback);

    Span start(String name);

    ActiveSpan startActive(String name);

    ActiveSpan startActive(String name, ActiveSpan parent);
}
