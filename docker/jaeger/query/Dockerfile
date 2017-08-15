FROM centos:7

RUN yum install -y wget
RUN wget https://github.com/jwilder/dockerize/releases/download/v0.1.0/dockerize-linux-amd64-v0.1.0.tar.gz
RUN tar -C /usr/local/bin -xzvf dockerize-linux-amd64-v0.1.0.tar.gz && rm dockerize-linux-amd64-v0.1.0.tar.gz

EXPOSE 16686

COPY query-linux /go/bin/
ADD jaeger-ui-build /go/jaeger-ui/

CMD ["/go/bin/query-linux", "--query.static-files=/go/jaeger-ui/"]
