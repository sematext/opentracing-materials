/*
 *    Copyright (c) Sematext International
 *    All Rights Reserved
 *
 *    THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF Sematext International
 *    The copyright notice above does not evidence any
 *    actual or intended publication of such source code.
 */
package com.sematext.opentracing.carrier;

import io.opentracing.propagation.TextMap;
import org.springframework.http.HttpHeaders;

import java.util.Iterator;
import java.util.Map;

/**
 * Accepts {@link HttpHeaders} as a constructor arg and populate the HTTP headers with data required for the
 * context propagation, such as trace identifier or span identifier.
 */
public class HttpHeadersInjectAdapter implements TextMap {

    private final HttpHeaders headers;

    public HttpHeadersInjectAdapter(HttpHeaders headers) {
        this.headers = headers;
    }

    @Override
    public Iterator<Map.Entry<String, String>> iterator() {
        throw new UnsupportedOperationException("TextMapInjectAdapter should only be used with Tracer.inject()");
    }

    @Override
    public void put(String key, String value) {
        this.headers.add(key, value);
    }
}
