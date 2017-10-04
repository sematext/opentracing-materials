package main

import (
	"log"
	"github.com/uber/jaeger-client-go"
)

// Demonstrates a basic usage of Jaeger client to initialize the tracer
// and generate a new span.
func main() {

	sampler := jaeger.NewConstSampler(true)
	transport, err := jaeger.NewUDPTransport("", 0)
	if err != nil {
		log.Print("Could not start UDP transport")
		return
	}
	reporter := jaeger.NewRemoteReporter(transport)

	tracer, closer := jaeger.NewTracer("octi",
		sampler,
		reporter,
		jaeger.TracerOptions.Tag("api-token", "c15a1793-71b7-46a5-88c5-bc76f9c772a0"),
	)

	span := tracer.StartSpan("octi-request")
	span.SetTag("error", false)
	span.Finish()

	defer closer.Close()
}

