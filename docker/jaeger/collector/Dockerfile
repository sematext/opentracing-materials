FROM centos:7

RUN yum install -y wget
RUN wget https://github.com/jwilder/dockerize/releases/download/v0.1.0/dockerize-linux-amd64-v0.1.0.tar.gz
RUN tar -C /usr/local/bin -xzvf dockerize-linux-amd64-v0.1.0.tar.gz && rm dockerize-linux-amd64-v0.1.0.tar.gz

COPY collector-linux /go/bin/

EXPOSE 14267

CMD ["/go/bin/collector-linux"]
