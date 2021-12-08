PROFILE = -Dspring.profiles.active=local
APP = spring-webflux
VERSION = 0.0.1-SNAPSHOT
JAR = target/${APP}-${VERSION}.jar
DELAY_DURATION = 500
DELAY_PARAMS = -Dapp.delay.enabled=true -Dapp.delay.duration=${DELAY_DURATION}

DOCKER_FOLDER = src/main/docker
DOCKER_CONF = ${DOCKER_FOLDER}/docker-compose.yml
DOCKER_IMAGE = ${APP}:latest

AB_FOLDER = ab
AB_TIME = 10
AB_CONCURRENCY = 500
WEBFLUX_SERVER_URL = localhost:8080/invoices/1
WEBFLUX_CLIENT_URL = localhost:8081/invoices/1

# Common

clean:
	mvn clean

all: clean
	mvn compile

install: clean
	mvn install

check: clean
	mvn verify

dist: clean
	mvn package -DskipTests

dist-run: dist run

run:
	java ${PROFILE} -jar ${JAR}

run-delay:
	java ${PROFILE} ${DELAY_PARAMS} -jar ${JAR}

dist-run-delay: dist run-delay

# Docker

start-docker: dist copy-jar-docker
	docker-compose -f ${DOCKER_CONF} up

copy-jar-docker:
	cp ${JAR} ${DOCKER_FOLDER}

stop-docker: docker-down rm-docker-image

docker-down:
	docker-compose -f ${DOCKER_CONF} down

rm-docker-image:
	docker rmi ${DOCKER_IMAGE}

# Benchmark

ab-all: ab-webflux-server ab-webflux-client

ab-webflux-server:
	ab -t ${AB_TIME} -c ${AB_CONCURRENCY} ${WEBFLUX_SERVER_URL} > ${AB_FOLDER}/webflux-server-s${DELAY_DURATION}-t${AB_TIME}-c${AB_CONCURRENCY}.txt

ab-webflux-client:
	ab -t ${AB_TIME} -c ${AB_CONCURRENCY} ${WEBFLUX_CLIENT_URL} > ${AB_FOLDER}/webflux-client-s${DELAY_DURATION}-t${AB_TIME}-c${AB_CONCURRENCY}.txt
