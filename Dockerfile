FROM gradle:7.6.3-jdk17-alpine
WORKDIR /monitoring
COPY . /monitoring

EXPOSE 8484
RUN gradle build
ENTRYPOINT ["gradle", "bootRun"]
