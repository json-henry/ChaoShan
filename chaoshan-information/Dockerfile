FROM openjdk:8-jre
ENV APP_HOME=/apps
WORKDIR $APP_HOME
COPY ./chaoshan-information-1.0.jar ./information.jar
EXPOSE 9901
ENTRYPOINT ["java","-jar"]
CMD ["information.jar"]