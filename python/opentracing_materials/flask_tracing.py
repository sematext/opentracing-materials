#
#    Copyright (c) Sematext International
#    All Rights Reserved
#
#    THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF Sematext International
#    The copyright notice above does not evidence any
#    actual or intended publication of such source code.
#
from flask import Flask
from flask_opentracing import FlaskTracer

from opentracing_materials.initializer import TracerInitializer

app = Flask(__name__)

initializer = TracerInitializer('jaeger')
initializer.setup('localhost', 5775, 'flask')

# initialize `flask` tracer with the instance of
# `jaeger` tracer. With this configuration all requests
# will be traced. Open your browser and go to http://localhost:5000/api/octi.
# Hit F5 several times and see how spans for the `octi` endpoint are
# reported to `Jaeger`.
flask_tracer = FlaskTracer(
    initializer.tracer,
    True,
    app)


@app.route("/api/octi")
def octi_endpoint():
    return "octi"

if __name__ == '__main__':
    app.run()
