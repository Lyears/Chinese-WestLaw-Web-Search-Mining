FROM openjdk:11.0.6
# the main work_dir and other required code dirs
RUN mkdir -p /srv/wsm/server
RUN mkdir -p /srv/wsm/root
ENV WSM_ROOT_DIR /srv/wsm/root

# copy own codes into container
COPY ./build/libs/Chinese-WestLaw-Web-Search-Mining-0.0.1-SNAPSHOT.jar /srv/wsm/server/server.jar

# entry point
# should expose one port for service
WORKDIR /srv/wsm/server/
ENTRYPOINT ["java", "-jar", "server.jar"]