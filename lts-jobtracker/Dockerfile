FROM openjdk:8-jdk-alpine

ENV DOCKERIZE_VERSION v0.6.1
RUN wget https://github.com/jwilder/dockerize/releases/download/$DOCKERIZE_VERSION/dockerize-alpine-linux-amd64-$DOCKERIZE_VERSION.tar.gz \
    && tar -C /usr/local/bin -xzvf dockerize-alpine-linux-amd64-$DOCKERIZE_VERSION.tar.gz \
    && rm dockerize-alpine-linux-amd64-$DOCKERIZE_VERSION.tar.gz

RUN apk add --no-cache tzdata unzip curl

# 需先把 lts-1.8.0-bin.zip 拷贝到 target 目录下
COPY target/lts-1.8.0-bin.zip /opt
RUN unzip /opt/lts-1.8.0-bin.zip -d /opt \
	&& mv /opt/lts-1.8.0-bin /opt/lts \
	&& sed -i 's/null &/null/' /opt/lts/bin/lts-admin.sh \
	&& sed -i 's/null &/null/' /opt/lts/bin/jobtracker.sh

VOLUME /opt/lts/conf /opt/lts/logs

EXPOSE 3502 8719

CMD sh /opt/lts/bin/jobtracker.sh zoo start