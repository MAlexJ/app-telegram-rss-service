### RSS news service

##### Description:

* Java 21
* Springboot 3.3.4
* Gradle 8.10
* Mongo database
* render.com webservice
* UptimeRobot webservice monitoring

### Springboot project setup

create .env file with properties:

```
APP_SERVICE_PORT=8080
APP_SERVICE_NAME=monolith-app
APP_SERVICE_ENABLE_JWT_SECURITY=false

APP_SERVICE_ROOT_LOG_LEVEL=INFO     or TRACE
APP_SERVICE_SPRING_LOG_LEVEL=INFO   or TRACE

APP_SERVICE_RSS_PUBLISH_TOPIC_LIMIT=20

APP_SERVICE_TELEGRAM_BOT_USERNAME=user
APP_SERVICE_TELEGRAM_BOT_TOKEN=token

APP_SERVICE_MONGODB_URI=mongodb_uri
APP_SERVICE_MONGODB_DATABASE=app-telegram-rss-service-db

APP_RSS_PROCESSOR_SERVICE_JWT_TOKEN=token
APP_RSS_PROCESSOR_SERVICE_JWT_SECRET_KEY=secret

APP_SERVICE_SCHEDULER_PROCESSING_RSS_CRONE=0 */1 * * * *
APP_SERVICE_SCHEDULER_PROCESSING_PUBLISHER_CRONE=*/15 * * * * *
```

### API documentation

Project uses OpenAPI (link: https://springdoc.org/)

Configuration api documentation endpoint in *.yaml file

```
springdoc:
  swagger-ui:
    path: /api/documentation
```

API documentation endpoint:  <br>

* http://{URL}:{port}/api/documentation

### Java code style

Java code style refers to the conventions and guidelines that developers follow when writing Java code to ensure
consistency and readability.

project: google-java-format,
link: https://github.com/google/google-java-format/blob/master/README.md#intellij-jre-config

### Telegram API

1. Register new app in https://my.telegram.org/auth
2. After registration save telegram_token and register a new bot
3. add bot username and telegram_token to .env file

#### How to create Telegram Bot and send messages to your group

* Create Telegram bot: <br>
    1. Search for user @BotFather in Telegram app and type in the chat `/newbot` command <br>
    2. @BotFather will give you HTTP API token, remember it and
       keep `1234567890:xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx` <br><br>

* How to add bot to my group.<br>
    1. Create new group in web or mobile application
    2. Go to your group settings and pick “Add Members” option, then type @you_bot_name in search field. <br>
       link: https://telegram-bot.app/learning-centre/how-to-add-bot-to-my-group/

### Spring

#### Cron Expressions

All Spring cron expressions have to conform to the same format, whether you are using them in @Scheduled annotations,
task:scheduled-tasks elements, or someplace else

link: https://docs.spring.io/spring-framework/reference/integration/scheduling.html#scheduling-cron-expression

#### Cache

@Cachable on methods without input
parameters: https://stackoverflow.com/questions/48888760/cachable-on-methods-without-input-parameters

```
@Cacheable(value = CACHE, key = "#root.method.name")
```

#### Spring Actuator

* [Spring Boot Actuator: Health check, Auditing, Metrics gathering and Monitoring](https://www.callicoder.com/spring-boot-actuator/#:~:text=You%20can%20enable%20or%20disable,the%20identifier%20for%20the%20endpoint)

Endpoint ID Description:

* info - Displays information about your application.
* health - Displays your application’s health status.

Enable info and health endpoint in *.yaml file

```
management:
  endpoints:
    web:
      exposure:
        include: health,info
```

Actuator endpoints:

* /actuator
* /actuator/health
* /actuator/info

### Atlas cloud MongoDb

* Cloud db: https://cloud.mongodb.com/
* [Working with Spring Data Repositories](https://docs.spring.io/spring-data/mongodb/docs/current/reference/html/#repositories)

### Hosting Springboot app

Render is a cloud platform that offers a variety of services for developers, including hosting for web applications,
databases, and static sites. Render aims to simplify the process of deploying and scaling applications by providing a
user-friendly interface and seamless integration with popular development tools.

Deploy for Free - https://render.com/ <br>
You can deploy instances of some Render services <br>
link: https://docs.render.com/free

Deploying a Spring Boot Application with Docker Image on Render <br>
tutorial: https://medium.com/@nithinsudarsan/deploying-a-spring-boot-application-with-docker-image-on-render-com-9a87f5ce5f72

### UptimeRobot: Monitor anything

UptimeRobot is a website monitoring service that checks the status of your websites, servers, and other online services
at regular intervals. It notifies you if any of your monitored services go down, helping you to quickly address any
issues and minimize downtime.

link: https://uptimerobot.com/ <br>
tutorial: https://www.youtube.com/watch?v=wP-tFyZhoio <br>

### Git tag

In Git, a tag marks an important point in a repository’s history.

Git supports two types of tags:

* Lightweight tags point to specific commits, and contain no other information.
  Also known as soft tags. Create or remove them as needed.
* Annotated tags contain metadata, can be signed for verification purposes, and can’t be changed.

link: https://docs.gitlab.com/ee/user/project/repository/tags/#create-a-tag

### Github action

issue:  ./gradlew: Permission denied
link: https://stackoverflow.com/questions/17668265/gradlew-permission-denied

You need to update the execution permission for gradlew

1. add action workflow

2. Locally pull changes

3. run Git command:

```
git update-index --chmod=+x gradlew
git add .
git commit -m "Changing permission of gradlew"
git push
```

### Dockerfile

build jar file contains layers.idx

link: https://for-each.dev/lessons/b/-docker-layers-spring-boot

video: https://www.youtube.com/watch?v=QrH4UFA8Rlw&t=79s

```
- "dependencies":
  - "BOOT-INF/lib/"
- "spring-boot-loader":
  - "org/"
- "snapshot-dependencies":
- "application":
  - "BOOT-INF/classes/"
  - "BOOT-INF/classpath.idx"
  - "BOOT-INF/layers.idx"
  - "META-INF/"
```

### Gradle

#### Gradle Versions Plugin

Displays a report of the project dependencies that are up-to-date, exceed the latest version found, have upgrades, or
failed to be resolved, info: https://github.com/ben-manes/gradle-versions-plugin

command:

```
gradle dependencyUpdates
```

#### Gradle wrapper

Gradle Wrapper Reference:
https://docs.gradle.org/current/userguide/gradle_wrapper.html

How to Upgrade Gradle Wrapper:
https://dev.to/pfilaretov42/tiny-how-to-upgrade-gradle-wrapper-3obl

```
./gradlew wrapper --gradle-version latest
```

#### Gradle ignore test

To skip any task from the Gradle build, we can use the -x or –exclude-task option. In this case, we’ll use “-x test” to
skip tests from the build.

To see it in action, let’s run the build command with -x option:

```
gradle build -x test
```

link: https://docs.gradle.org/current/dsl/org.gradle.api.tasks.testing.Test.html

```
  beforeTest { descriptor ->
        logger.lifecycle("Running test: " + descriptor)
    }
    testLogging.showStandardStreams = true
    testLogging.exceptionFormat = 'full'
```