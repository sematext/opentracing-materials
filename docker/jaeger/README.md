These are the instructions on how to build, package and deploy the latest `Jaeger` release on Docker and orchestrate the execution
of the containers via `docker-compose`.

## Get the package

Before you start make sure `go` toolchain and `glide` package manager are properly installed. Next, export the `GOPATH`
and get the latest `jaeger` package from the github repository:

```bash
$ export JAEGER_HOME=/opt/jaeger
$ export GOPATH=$JAEGER_HOME
$ go get -v github.com/uber/jaeger
```

## Install dependencies

You are now ready to install the required dependenices via `glide` package manager:

```bash
$ cd $JAEGER_HOME/src/github.com/uber/jaeger
$ glide install
```

## Build components

Run `Makefile` targets to build the components:

```bash
$ make build-agent-linux
$ make build-query-linux
$ make build-collector-linux
```

To build the UI, `yarn` package manager is required. Install it and then run the build process:

```bash
$ make build_ui
```

## Deploy containers

Copy the generated Jaeger's binaries to their corresponding directories of this repository:

```bash
$ cp $JAEGER_HOME/src/github.com/uber/jaeger/cmd/agent/agent-linux docker/jaeger/agent/
$ cp $JAEGER_HOME/src/github.com/uber/jaeger/cmd/collector/collector-linux docker/jaeger/collector/
$ cp $JAEGER_HOME/src/github.com/uber/jaeger/cmd/query/query-linux docker/jaeger/query/
$ cp -r $JAEGER_HOME/src/github.com/uber/jaeger/jaeger-ui-build/build docker/jaeger/query/jaeger-ui

```

Export the env variables with `Elasticsearch` credentials and start the deployment:

```bash
$ source docker/jaeger/.env.sh
$ docker-compose up
```


