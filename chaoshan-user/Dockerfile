FROM openjdk:8-jre
ENV APP_HOME=/apps
WORKDIR $APP_HOME
COPY ./chaoshan-user-1.0.jar ./user.jar
EXPOSE 9002
ENTRYPOINT ["java","-jar"]
CMD ["user.jar"]