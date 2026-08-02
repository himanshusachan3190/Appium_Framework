#!/usr/bin/env sh
basedir=$(dirname "$0")
if [ -z "$MAVEN_PROJECTBASEDIR" ]; then
  MAVEN_PROJECTBASEDIR="$basedir"
fi
if [ -x "$MAVEN_PROJECTBASEDIR/.mvn/wrapper/maven-wrapper.jar" ]; then
  exec java -jar "$MAVEN_PROJECTBASEDIR/.mvn/wrapper/maven-wrapper.jar" "$@"
else
  echo "The Maven Wrapper JAR cannot be found." >&2
  exit 1
fi
