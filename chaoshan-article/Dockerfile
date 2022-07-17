FROM openjdk:8-jre
ENV APP_HOME=/apps
WORKDIR $APP_HOME
COPY ./chaoshan-article-1.0.jar ./article.jar
EXPOSE 9100
ENTRYPOINT ["java","-jar"]
CMD ["article.jar"]