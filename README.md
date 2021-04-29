# Home Hub

Home hub API for IFTTT, Toggl

## Use for private

- call by IFTTT Webhook for starting Toggl time
  - study, exercise, develop...etc
- call IFTTT Webhook

## Build(local to azure appservice)

```shell script
$ # build native image
$ ./mvnw spring-boot:build-image -Dspring-boot.build-image.imageName=connector
$ # docker tag
$ docker tag docker.io/library/connector:latest vagivagi/connector:latest
$ # docker push
$ docker push vagivagi/connector:latest
$ # deploy azure(after az login command)
$ az webapp create --resource-group ${RESOURCE_GROUP} \
$    --plan ${PLAN} --name ${APP_NAME} \
$    --multicontainer-config-type compose \
$    --multicontainer-config-file docker-compose.yml
```

## curl

```shell script
$ curl -X POST -H "Content-Type: application/json" \
$ http://localhost:8080/toggl/start \
$ -d '{"description":"description","key":"your_key"}'
```