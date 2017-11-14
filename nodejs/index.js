/*
  Configures the Jaeger client and emits an HTTP request to the specificed endpoint.
  A span is created around HTTP client events and submitted to Jaeger tracing system.
*/

const jaeger = require('jaeger-client');
const http = require('http');
const opentracing = require('opentracing');
const UDPSender = require('jaeger-client/dist/src/reporters/udp_sender').default;

const serviceName = "octi";
const options = {
    tags: {
      'api-token': '<your-token>'
    }
};

const httpOpts = {
    host : 'localhost',
    method: 'GET',
    port : '3000',
    path: '/',
};

var reporter = new jaeger.RemoteReporter(new UDPSender());
var sampler = new jaeger.ConstSampler(1);
var tracer = new jaeger.Tracer(serviceName, reporter, sampler, options);

const span = tracer.startSpan('http-request');

http.request(httpOpts, res => {
    res.setEncoding('utf8');
    res.on('error', err => {
        span.setTag(opentracing.Tags.ERROR, true);
        span.log({'event': 'error', 'error.object': err, 'message': err.message, 'stack': err.stack});
        span.finish();
    });
    res.on('data', chunk => {
        span.log({'event': 'data_received', 'chunk_length': chunk.length});
    });
    res.on('end', () => {
        span.log({'event': 'request_end'});
        span.finish();
    });
}).end();
