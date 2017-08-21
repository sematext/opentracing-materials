#
#    Copyright (c) Sematext International
#    All Rights Reserved
#
#    THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF Sematext International
#    The copyright notice above does not evidence any
#    actual or intended publication of such source code.
#
from argparse import ArgumentParser

import sys
from logbook import Logger, StreamHandler

from flask import Flask
from flask_opentracing import FlaskTracer

from opentracing_materials.initializer import TracerInitializer

app = Flask(__name__)


def __init_logging():
    StreamHandler(sys.stdout).push_application()
    return Logger('flask-opentracing')

logger = __init_logging()

tracer_type = 'jaeger'
tracer_host = 'localhost'
tracer_port = 5775

initializer = TracerInitializer(tracer_type)
initializer.setup(tracer_host, tracer_port, 'flask')

logger.info('%s tracer initialized on endpoint %s:%s'
            % (tracer_type, tracer_host, tracer_port))

parser = ArgumentParser(prog="OpenTracing demonstration for Flask web framework")
parser.add_argument("--trace-all", help="determines if all requests are traced", action="store_true")
args = parser.parse_args()

# initialize `flask` tracer with an instance of
# `jaeger` tracer. With this configuration all requests
# will be traced. Open your browser and go to http://localhost:5000/api/octi.
# Hit F5 several times and see how spans for the `octi` endpoint are
# reported to `Jaeger`.
if args.trace_all:
    flask_tracer = FlaskTracer(initializer.tracer, True, app)

    @app.route("/api/octi")
    def octi_endpoint():
        return "octi"
    logger.info('Tracing all endpoints. Browse to http://localhost:5000/api/octi')
else:
    tracer = FlaskTracer(initializer.tracer)

    @app.route("/api/octi")
    @tracer.trace("url")
    def octi_endpoint_traced():
        # get the current span and attach some tags
        span = tracer.get_span()
        span.set_tag("http.method", "GET")
        return "octi traced"


    @app.route("/api/octi_notraced")
    def octi_endpoint_notraced():
        return "octi not traced"

    logger.info('Tracing single endpoint. Browse to http://localhost:5000/api/octi')

if __name__ == '__main__':
    app.run()
