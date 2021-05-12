#!/bin/bash
docker-compose down 
docker rmi itoken-redis

mvn clean package

docker-compose up -d
 
