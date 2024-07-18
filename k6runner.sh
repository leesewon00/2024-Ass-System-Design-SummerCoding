#!/bin/bash

# 명령이 0이 아닌 종료값을 가질때 즉시 종료
set -e

# 인자로 스크립트 파일 지정하도록 커스텀 가능
k6 run \
  --out influxdb=http://localhost:8086/admin \
  script.js
