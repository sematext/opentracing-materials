#
#    Copyright (c) Sematext International
#    All Rights Reserved
#
#    THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF Sematext International
#    The copyright notice above does not evidence any
#    actual or intended publication of such source code.
#
import opentracing
import time
import os

from opentracing_materials.initializer import TracerInitializer


def _emit_span():
    """Initialize the `Jaeger` tracer client and creates a new
    span with associated tags.
    """
    initializer = TracerInitializer('jaeger')
    initializer.setup('localhost', 5775, 'opentracing-python')
    with opentracing.tracer.start_span('emit') as span:
        span.set_tag('pid', os.getpid())
        span.set_tag('ppid', os.getppid())
    time.sleep(2)
    initializer.tracer.close()

if __name__ == '__main__':
    _emit_span()