#!/bin/bash
set -euo pipefail

ROOT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
cd "$ROOT_DIR"

OUT_DIR="$ROOT_DIR/docker/jars"
mkdir -p "$OUT_DIR"

if command -v mvn >/dev/null 2>&1; then
  mvn -Dmaven.test.skip=true clean package
elif command -v docker >/dev/null 2>&1; then
  docker run --rm -v "$ROOT_DIR:/app" -v "$ROOT_DIR/maven/settings.xml:/root/.m2/settings.xml:ro" -w /app docker.m.daocloud.io/library/maven:3.8.5-openjdk-8 mvn -Dmaven.test.skip=true clean package
else
  echo "未找到 docker 或 mvn" >&2
  exit 1
fi

copy_latest_jar() {
  local module_path="$1"
  local dest_name="$2"
  local target_dir="$ROOT_DIR/$module_path/target"

  local jar
  jar="$(ls -1t "$target_dir"/*.jar 2>/dev/null | grep -vE '\.original$|-sources\.jar$|-javadoc\.jar$' | head -n 1 || true)"

  if [[ -z "$jar" ]]; then
    echo "未找到可用 jar: $target_dir" >&2
    exit 1
  fi

  cp -f "$jar" "$OUT_DIR/$dest_name"
}

copy_latest_jar "jc-smarteroj-backend-gateway" "gateway.jar"
copy_latest_jar "jc-smarteroj-backend-user-service" "user-service.jar"
copy_latest_jar "jc-smarteroj-backend-question-service" "question-service.jar"
copy_latest_jar "jc-smarteroj-backend-judge-service" "judge-service.jar"
copy_latest_jar "jc-smarteroj-backend-post-service" "post-service.jar"
copy_latest_jar "jc-smarteroj-backend-room-service" "room-service.jar"

echo "JAR 已输出到: $OUT_DIR"
