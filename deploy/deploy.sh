#!/usr/bin/env bash
# CI(GitHub Actions)가 SSH로 EC2에 접속해 실행하는 배포 스크립트.
# 이미지는 EC2에서 직접 빌드하지 않고 Docker Hub에서 pull만 한다 (빌드는 CI가 담당).
# .env는 이 스크립트가 건드리지 않는다 (git에 없는 파일이라 reset --hard의 영향을 받지 않음).
set -euo pipefail

: "${IMAGE_NAME:?IMAGE_NAME env var required (예: naeuun/monday)}"

cd ~/monday

git fetch origin main
git reset --hard origin/main

docker pull "${IMAGE_NAME}:latest"

docker stop monday || true
docker rm monday || true

docker run -d --name monday --restart unless-stopped \
  -p 127.0.0.1:8080:8080 --env-file .env "${IMAGE_NAME}:latest"

docker image prune -f
