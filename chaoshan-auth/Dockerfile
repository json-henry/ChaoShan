FROM openjdk:8-jre
ENV APP_HOME=/apps
WORKDIR $APP_HOME
COPY ./chaoshan-auth-1.0.jar ./auth.jar
EXPOSE 9000
ENTRYPOINT ["java","-jar"]
CMD ["auth.jar"]