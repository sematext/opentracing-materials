package com.sematext.opentracing.span;

import com.sematext.opentracing.SpanOperations;
import com.sematext.opentracing.TracerInitializer;
import com.sematext.opentracing.Tracers;
import io.opentracing.ActiveSpan;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.HttpHeaders;

import java.util.HashMap;
import java.util.Map;

public class SpanTemplateTest {

    private TracerInitializer tracerInitializer = new TracerInitializer(Tracers.ZIPKIN);

    private SpanOperations spanOps = new SpanTemplate();
    private static boolean setup = false;

    @Before
    public void setUp() {
        // JUnit 5 (@BeforeAll) would deal with this
        if (setup) {
            return;
        }
        tracerInitializer.setup("localhost", 9411, "console-app");
        setup = true;
    }

    @Test
    public void testStartActive() throws InterruptedException {
        try (ActiveSpan span = spanOps.startActive("hello sematextains")) {
            span.setTag("app", "console");
        }
        Thread.sleep(2000);
    }

    @Test
    public void testStartActiveWithParent() throws InterruptedException {
        try (ActiveSpan parent = spanOps.startActive("parent")) {
            parent.setTag("app", "console");
            // simulate I/O op
            Thread.sleep(500);
            try (ActiveSpan child = spanOps.startActive("child", parent)) {
                child.setTag("encoding", "utf-8");
            }
        }
        Thread.sleep(2000);
    }

    @Test
    public void testInject() {
        try (ActiveSpan span = spanOps.startActive("parent")) {
            Map<String, Object> map = new HashMap<>();
            map.put("app", "console");
            HttpHeaders headers = new HttpHeaders();
            spanOps.inject(span.context(), headers);
        }
    }
}
