package com.sematext.opentracing.span;

import com.sematext.opentracing.SpanOperations;
import com.sematext.opentracing.TracerInitializer;
import com.sematext.opentracing.Tracers;
import io.opentracing.ActiveSpan;
import org.junit.Before;
import org.junit.Test;

public class SpanTemplateTest {

    private TracerInitializer tracerInitializer;

    private SpanOperations spanOps = new SpanTemplate();

    @Before
    public void setup() {
        tracerInitializer = new TracerInitializer(Tracers.ZIPKIN);
        tracerInitializer.setup("localhost", 9411, "console-app");
    }

    @Test
    public void testStartActive() throws InterruptedException {
        try (ActiveSpan span = spanOps.startActive("hello")) {
            span.setTag("app", "console");
        }
        Thread.sleep(2000);
    }
}
