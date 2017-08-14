#
#    Copyright (c) Sematext International
#    All Rights Reserved
#
#    THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF Sematext International
#    The copyright notice above does not evidence any
#    actual or intended publication of such source code.
#
from jaeger_client import Config


class TracerInitializer(object):
    """Initialize OpenTracing compatible distributed tracers.
    """
    def __init__(self, tracer_type):
        """Creates an instance of the tracer initializer.

        :param str tracer_type: determines the type of the tracer used. `jaeger`|`zipkin`
        """
        self._tracer_type = tracer_type
        self._tracer = None

    def setup(self, host, port, component):
        """Setups the OpenTracing compatible tracer system.

        :param str host: address or host name where tracer is accepting the ingestion of the spans
        :param int port: the port of the tracer's ingestion endpoint
        :param str component: in OpenTracing terminology, service name, library, framework, etc. that
                              generated the span
        """
        if self._tracer_type not in ['jaeger',
                                     'zipkin']:
            raise ValueError('%s tracer is not supported' % self._tracer_type)

        if 'jaeger' in self._tracer_type:
            config = Config(
                config={
                    'local_agent': {
                        'reporting_host': host,
                        'reporting_port': port
                    },
                    'logging': True,
                    'sampler': {
                        'type': 'const',
                        'param': 1,
                    },
                },
                service_name=component
            )
            self._tracer = config.initialize_tracer()

    @property
    def tracer_type(self):
        return self._tracer_type

    @property
    def tracer(self):
        return self._tracer
