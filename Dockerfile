FROM openjdk
RUN mkdir /app
COPY target/itoken-service-redis-0.0.1-SNAPSHOT.jar /app
CMD java -jar /app/itoken-service-redis-0.0.1-SNAPSHOT.jar 
EXPOSE 8502
