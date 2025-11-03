FROM eclipse-temurin:8-jre

RUN mkdir -p /opt/MFile/logs

WORKDIR /opt/MFile

COPY ../../build/MFile-1.1.0.RELEASE /opt/MFile

CMD /opt/MFile/bin/start.sh && tail -f /opt/MFile/logs/MFile.log
