#!/bin/bash

echo "### CLEANING ALL_BUILDED_JARS CLEAN BUILD..."
rm ./ALL_BUILDED_JARS/*

echo "### STARTING GRADLE CLEAN BUILD..."
gradle clean build -Dskip.tests  -Dorg.gradle.java.home=/usr/lib/jvm/java-17-openjdk-amd64/

echo "### DONE GRADLE CLEAN BUILD..."
jars_compiled=`find ./ | grep 'build/libs' | grep jar | grep -v 'archimatetool-' | grep -v 'build-logic.jar' | grep -v 'buildSrc.jar'`

echo "### MOVING JARS TO ALL BUILDED JARS DIR..."
for i in ${jars_compiled[@]}; do
    echo $i;
    mv $i ./ALL_BUILDED_JARS
done

echo "### COPY SHARED DEPENDENCIES JARS TO SHARED LIBS JARS DIR..."
components_list=$(ls -la ALL_BUILDED_JARS/ | awk '{print $9}' | sed -e 's\-.*\\g' | tail -n +4)

for i in ${components_list[@]}; do
    cp -r $i/lib/*.jar ./SHARED_JAR_LIBS/ 2>/dev/null
done
