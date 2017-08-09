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
import io.opentracing.SpanContext;
import org.springframework.http.HttpHeaders;

import java.util.Map;

public interface SpanOperations {

    void doInTracer(SpanCallback callback);

    Span start(String name);

    ActiveSpan startActive(String name);

    ActiveSpan startActive(String name, ActiveSpan parent);

    ActiveSpan startActive(String name, SpanContext parent);

    void inject(SpanContext ctx, HttpHeaders headers);

    SpanContext extract(final Map<String, Object> map);
}
