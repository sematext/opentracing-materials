/*
 *    Copyright (c) Sematext International
 *    All Rights Reserved
 *
 *    THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF Sematext International
 *    The copyright notice above does not evidence any
 *    actual or intended publication of such source code.
 */
package com.sematext.opentracing.jdbc.api;

import com.sematext.opentracing.SpanOperations;
import io.opentracing.ActiveSpan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.List;

@RestController
public class AppController {

    @Autowired
    private NamedParameterJdbcTemplate jdbcTemplate;

    @Autowired
    private SpanOperations spanOps;

    @RequestMapping(value = "/app/{name}", method = RequestMethod.POST)
    public void create(@PathVariable String name) {
        String sql = "INSERT INTO apps (name) VALUES (:name)";
        try (ActiveSpan span = spanOps.startActive("create-app")) {
            span.setTag("db.instance", "apps");
            span.setTag("db.type", "sql");
            span.setTag("db.statement", sql.replaceAll(":name", name));
            jdbcTemplate.update(sql, Collections.singletonMap("name", name));
        }
    }

    @RequestMapping(value = "/app", method = RequestMethod.GET)
    public List<String> list() {
        String sql = "SELECT * FROM apps";
        try (ActiveSpan span = spanOps.startActive("list-apps")) {
            span.setTag("db.instance", "apps");
            span.setTag("db.type", "sql");
            span.setTag("db.statement", sql);
            return jdbcTemplate.getJdbcOperations().queryForList(sql, String.class);
        }
    }
}
