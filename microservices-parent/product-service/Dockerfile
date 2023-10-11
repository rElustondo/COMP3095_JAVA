#------------
# Docker file for product-service
#----------

#------------
# BUILD stage
#----------

#base image
FROM gradle:8-jdk17-alpine AS builder

#copy the application files from the host machin to the image filesystem

COPY --chown=gradle:gradle . /home/gradle/src

# set directory within the image, for executing future commands
WORKDIR /home/gradle/src

# run gradle build within container - not within host machine

RUN gradle build -x test

#------------
# Package stage
#----------
FROM openjdk:17-alpine

# Create Directory within the container - not the host machine

RUN mkdir /app

COPY --from=builder /home/gradle/src/build/libs/*.jar /app/product-service.jar

ENV MONGO_DB_USERNAME=rootadmin \
    MONGO_DB_PWD=password

EXPOSE 8084

ENTRYPOINT ["java", "-jar", "/app/product-service.jar"]



