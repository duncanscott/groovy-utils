#!/bin/bash

set -e

SCRIPT_PATH=$(realpath "${BASH_SOURCE[0]}")
SCRIPT_DIR=$(dirname "${SCRIPT_PATH}")
cd "$SCRIPT_DIR"

# Recover from a stale JAVA_HOME, following the json-message release helper.
if [ ! -x "${JAVA_HOME}/bin/java" ]; then
    sdkman_java="${SDKMAN_DIR:-$HOME/.sdkman}/candidates/java/current"
    system_java=$(/usr/libexec/java_home 2>/dev/null || true)
    for candidate in "$sdkman_java" "$system_java"; do
        if [ -n "$candidate" ] && [ -x "${candidate}/bin/java" ]; then
            JAVA_HOME=$(cd "$candidate" && pwd -P)
            export JAVA_HOME
            break
        fi
    done
fi

if [ ! -x "${JAVA_HOME}/bin/java" ]; then
    echo "tag.sh: JAVA_HOME is unset or invalid (${JAVA_HOME:-<unset>}) and no JDK was found." >&2
    echo "tag.sh: set JAVA_HOME to a valid JDK and re-run." >&2
    exit 1
fi

exec ./gradlew --no-configuration-cache tag "$@"
