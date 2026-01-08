#!/bin/bash
DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"
# Assuming jar is in Resources/Java
java -jar "$DIR/../Resources/Java/password-manager-java-2.0-SNAPSHOT.jar"
