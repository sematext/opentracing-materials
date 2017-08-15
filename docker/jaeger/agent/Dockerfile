FROM centos:7

RUN yum install -y wget
RUN wget https://github.com/jwilder/dockerize/releases/download/v0.1.0/dockerize-linux-amd64-v0.1.0.tar.gz
RUN tar -C /usr/local/bin -xzvf dockerize-linux-amd64-v0.1.0.tar.gz && rm dockerize-linux-amd64-v0.1.0.tar.gz

EXPOSE 5775/udp 6831/udp 6832/udp 5778

COPY agent-linux /go/bin/

CMD ["/go/bin/agent-linux"]
