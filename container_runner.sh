#!/bin/bash

# 명령이 0이 아닌 종료값을 가질때 즉시 종료
set -e

echo "Starting all docker containers..."
docker-compose up -d

echo "Pruning unused Docker images..."
docker image prune -f

echo "Containers are up and running."
docker-compose ps -a