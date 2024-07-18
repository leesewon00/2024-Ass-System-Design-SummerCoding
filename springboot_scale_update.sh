#!/bin/bash

# 파라미터 수 확인
if [ $# -ne 1 ]; then
  echo "Usage: $0 <number_of_instances>"
  echo "Error: Exactly one parameter is required."
  exit 1
fi

NUM_INSTANCES=$1

# docker-compose를 사용하여 Spring Boot 서비스를 스케일
echo "Scaling Spring Boot to $NUM_INSTANCES instances..."
docker-compose up -d --scale springboot=$NUM_INSTANCES

# Nginx 컨테이너 재시작
echo "Restarting Nginx..."
docker-compose restart nginx

echo "Operation completed."
