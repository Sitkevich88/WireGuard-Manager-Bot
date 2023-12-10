FROM openjdk:17-oracle
LABEL authors="vsitk"
WORKDIR /app
EXPOSE 8080

COPY build/libs/vpn-app-0.0.1.jar /app/app.jar
COPY src/main/resources /app/resources

ENV YDB_URL="jdbc:ydb:grpcs://ydb.serverless.yandexcloud.net:2135/ru-central1/***/***?saFile=file:/app/resources/sa_key.json"
ENV SSH_ADD_CLIENT_SCRIPT="/app/resources/add_client.sh"
ENV TG_BOT_TOKEN="***"
ENV TG_BOT_USERNAME="WireGuardManagerBot"
ENV TG_BOT_CREATOR_ID="324820834"

CMD ["java", "-jar", "app.jar"]