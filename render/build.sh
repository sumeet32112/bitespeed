#!/bin/bash

set -e  # Exit on error

# Install Java 17
apt-get update
apt-get install -y openjdk-17-jdk

# Set JAVA_HOME and update PATH
export JAVA_HOME=/usr/lib/jvm/java-17-openjdk-amd64
export PATH=$JAVA_HOME/bin:$PATH

# Optional: verify Java
java -version
echo "JAVA_HOME is set to $JAVA_HOME"

# Build your Maven project
./mvnw clean install -DskipTests
