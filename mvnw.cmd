@echo off
setlocal
set MAVEN_PROJECTBASEDIR=%~dp0
if exist "%MAVEN_PROJECTBASEDIR%.mvn\wrapper\maven-wrapper.jar" (
  java -jar "%MAVEN_PROJECTBASEDIR%.mvn\wrapper\maven-wrapper.jar" %*
) else (
  echo The Maven Wrapper JAR cannot be found.
  exit /b 1
)
