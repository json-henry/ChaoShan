FROM openjdk:8-jre
ENV APP_HOME=/apps
WORKDIR $APP_HOME
COPY ./chaoshan-gateway-1.0.jar ./gateway.jar
EXPOSE 8000
ENTRYPOINT ["java","-jar"]
CMD ["gateway.jar"]