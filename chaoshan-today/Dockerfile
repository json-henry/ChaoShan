FROM openjdk:8-jre
ENV APP_HOME=/apps
WORKDIR $APP_HOME
COPY ./chaoshan-today-1.0.jar ./today.jar
EXPOSE 9902
ENTRYPOINT ["java","-jar"]
CMD ["today.jar"]