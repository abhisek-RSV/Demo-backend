#!/usr/bin/env bash
# Builds the frontend. Usage: build-ui.sh <path-to-demo-frontend>
set -euo pipefail

UI_PATH="${1:?usage: build-ui.sh <ui.project.path>}"
cd "$UI_PATH"

if [ ! -d node_modules ]; then
  npm ci
fi

bash ./run-build.sh
