#!/bin/bash

set -e

echo "Building Spring Boot application..."
./gradlew build -x test || { echo 'gradlew build failed'; exit 1; }

echo "Building Docker image..."
docker build -t springboot . || { echo 'docker build failed'; exit 1; }
# springboot는 이미지 이름을 지정한 것이다.

echo "Pruning unused Docker images..."
docker image prune -f