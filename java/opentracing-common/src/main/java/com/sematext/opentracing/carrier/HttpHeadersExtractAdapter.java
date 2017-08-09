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

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class HttpHeadersExtractAdapter implements TextMap {

    private final Map<String, String> map;

    public HttpHeadersExtractAdapter(final Map<String, Object> headersMap) {
        this.map = new HashMap<>();
        headersMap.forEach((k, v) -> map.put(k, (String)v));
    }

    @Override
    public Iterator<Map.Entry<String, String>> iterator() {
        return map.entrySet().iterator();
    }

    @Override
    public void put(String s, String s1) {
        throw new UnsupportedOperationException("TextMapInjectAdapter should only be used with Tracer.extract()");
    }
}
