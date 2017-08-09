/*
 *    Copyright (c) Sematext International
 *    All Rights Reserved
 *
 *    THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF Sematext International
 *    The copyright notice above does not evidence any
 *    actual or intended publication of such source code.
 */
package com.sematext.opentracing.opentracingextractor.api;

import com.sematext.opentracing.SpanOperations;
import io.opentracing.ActiveSpan;
import io.opentracing.SpanContext;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

@RestController
public class ExtractorController {

    @Autowired
    private SpanOperations spanOperations;

    private static final Logger logger = Logger.getLogger(ExtractorController.class);

    @RequestMapping(value = "/extract", method = RequestMethod.POST)
    public String extract(HttpServletRequest req) {
        Enumeration<String> headers = req.getHeaderNames();
        Map<String, Object> map = new HashMap<>();
        while (headers.hasMoreElements()) {
            String headerName = headers.nextElement();
            map.put(headerName, req.getHeader(headerName));
            logger.info("Header name " + headerName +  " value " + req.getHeader(headerName));
        }
        // deserialize propagated context
        SpanContext parent = spanOperations.extract(map);
        if (parent != null) {
            try (ActiveSpan child = spanOperations.startActive("extract", parent)) {
                child.setTag("http.status_code", 200);
                child.setTag("http.url", "http://localhost:8081/extract");
            }
        }
        return "extract";
    }
}
