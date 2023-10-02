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

