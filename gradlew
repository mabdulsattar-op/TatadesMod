#!/usr/bin/env bash
set -euo pipefail
ROOT="$(cd "$(dirname "$0")" && pwd)"
DIST_VERSION="9.2"
ZIP="$ROOT/gradle-${DIST_VERSION}-bin.zip"
DEST="$ROOT/gradle"

if [ ! -f "$ZIP" ]; then
  echo "Downloading Gradle ${DIST_VERSION} from downloads.gradle.org..."
  wget -q -O "$ZIP" "https://downloads.gradle.org/distributions/gradle-${DIST_VERSION}-bin.zip"
fi

if [ ! -d "$DEST/gradle-${DIST_VERSION}" ]; then
  echo "Extracting Gradle to $DEST..."
  mkdir -p "$DEST"
  unzip -q "$ZIP" -d "$DEST"
fi

GRADLE_BIN="$DEST/gradle-${DIST_VERSION}/bin/gradle"
if [ ! -x "$GRADLE_BIN" ]; then
  chmod +x "$GRADLE_BIN" || true
fi

exec "$GRADLE_BIN" "$@"
