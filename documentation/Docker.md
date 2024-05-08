## Live Remote Debugging

* video tutorial: https://www.youtube.com/watch?v=QuBgmaILdWI
* additional info: https://ealebed.github.io/tags/docker/
* habr note: https://habr.com/ru/articles/513520/

### IDE setup

Edit configuration >  Run / Debug > Remote > Remote Debugging or Debugging

run java app:

```
java -agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:8000 -jar target/remote-debugging-spring-boot-application.jar
```

docker:

```
FROM openjdk:11-jdk-slim
VOLUME /tmp
COPY target/remote-debugging-spring-boot-application.jar app.jar
ENTRYPOINT ["java","-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:8000","-jar","/app.jar"]
```

### Docker documentation

#### build

The docker build command builds Docker images from a Dockerfile and "context"

```
docker build .
```

A build's context is the set of files located in the specified PATH or URL.
The build process can refer to any of the files in the context.

Build with URL

```
docker build github.com/creack/docker-firefox
```  

info: https://docs.docker.com/reference/cli/docker/image/build/

##### options:

-t, --tag : Name and optionally a tag in the name:tag format

-f, --file : Name of the Dockerfile (Default is PATH/Dockerfile)

```
docker build -t spring-rest-app ./ --file .\docker_live_remote_debugging\Dockerfile
```

#### create

#### start

#### run

1. find IMAGE ID

```
docker images
```

2. run image

```
docker run -d -p 8080:8080 -p 5005:5005 --name spring-rest-app-debug [IMAGE ID]
```

### Dockerfile reference

#### EXPOSE

Describe which ports your application is listening on.

```
EXPOSE <port> [<port>/<protocol>...]
```

The EXPOSE instruction informs Docker that the container listens on the specified network ports at runtime. You can
specify whether the port listens on TCP or UDP, and the default is TCP if you don't specify a protocol.

The EXPOSE instruction doesn't actually publish the port. 