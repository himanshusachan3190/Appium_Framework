#!/usr/bin/env sh
basedir=$(dirname "$0")
if [ -z "$MAVEN_PROJECTBASEDIR" ]; then
  MAVEN_PROJECTBASEDIR="$basedir"
fi
if [ ! -d "$MAVEN_PROJECTBASEDIR" ]; then
  MAVEN_PROJECTBASEDIR="."
fi
MAVEN_PROJECTBASEDIR=$(cd "$MAVEN_PROJECTBASEDIR" >/dev/null && pwd)
if [ -f "$MAVEN_PROJECTBASEDIR/.mvn/wrapper/maven-wrapper.jar" ]; then
  exec java -Dmaven.multiModuleProjectDirectory="$MAVEN_PROJECTBASEDIR" -cp "$MAVEN_PROJECTBASEDIR/.mvn/wrapper/maven-wrapper.jar" org.apache.maven.wrapper.MavenWrapperMain "$@"
else
  echo "The Maven Wrapper JAR cannot be found." >&2
  exit 1
fi
