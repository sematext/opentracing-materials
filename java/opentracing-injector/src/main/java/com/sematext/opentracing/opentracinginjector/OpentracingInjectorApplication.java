package com.sematext.opentracing.opentracinginjector;

import com.sematext.opentracing.SpanOperations;
import com.sematext.opentracing.TracerInitializer;
import com.sematext.opentracing.Tracers;
import com.sematext.opentracing.span.SpanTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class OpentracingInjectorApplication implements CommandLineRunner {

	@Value("${tracer.host}")
	private String tracerHost;
	@Value("${tracer.port}")
	private int tracerPort;
	@Value("${tracer.type}")
	private Tracers tracerType;

	public static void main(String[] args) {
		SpringApplication.run(OpentracingInjectorApplication.class, args);
	}

	@Override
	public void run(String... strings) throws Exception {
		TracerInitializer tracerInitializer = new TracerInitializer(tracerType);
		tracerInitializer.setup(tracerHost, tracerPort, "opentracing-injector");
	}

	@Bean
	public SpanOperations spanOps() {
		return new SpanTemplate();
	}
}
