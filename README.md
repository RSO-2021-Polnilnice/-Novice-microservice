# Novice microservice

This microservice sends emails whenever a new charging station is added.

## Prerequisites

Create network **rsonet** if there is none:

```bash
docker network create rsonet
```

Run Consul Docker container:
```bash
docker run -d --name consul -p 8500:8500 --network rsonet consul
```

## Build and run Docker containers

Building **novice** container:
```bash
docker build -f .\Dockerfile_with_maven_build -t novice:latest .
```

Run:
```bash
docker run -p 8081:8081 --network rsonet -e KUMULUZEE_CONFIG_CONSUL_AGENT=http://consul:8500 --name novice-instance novice
```

## Usage

Put the values inside Consul. Those values will be used when sending emails to subscribers.