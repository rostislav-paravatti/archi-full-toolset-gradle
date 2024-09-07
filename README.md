# Prerequirements and statements

* The best way to clean build - run inside WSL Ubuntu 22.04
* In clear WSL Ubuntu do 
    - change apt sources lists to your nexus or jfrog server
    - apt update && apt install mc openjdk-17-jdk openjdk-17-jre openjdk-11-jdk openjdk-11-jre wget unzip maven
    - mount your REPOS folder to some directory inside WSL, as example run in user home folder
       
       ```ln -s /mnt/d/REPOS REPOS```
    - set the JAVA_HOME environment variable to ```/usr/lib/jvm/java-11-openjdk-amd64/```
    - install && update the kotlin & groovy packages & gradle - install by gradle bin archive - 

       ```gradle-8.10-all.zip and add foler which placed extracted gradle to /etc/environment variable PATH to the end```
    - set in maven configuration your proxy if you have a outgoing squid or other proxy server
    - update gradle to latest (tested at 8.10), after running ```gradle --version``` you must see:

```
	------------------------------------------------------------
	Gradle 8.10
	------------------------------------------------------------

	Build time:    2024-08-14 11:07:45 UTC
	Revision:      fef2edbed8af1022cefaf44d4c0514c5f89d7b78

	Kotlin:        1.9.24
	Groovy:        3.0.22
	Ant:           Apache Ant(TM) version 1.10.14 compiled on August 16 2023
	Launcher JVM:  11.0.24 (Ubuntu 11.0.24+8-post-Ubuntu-1ubuntu322.04)
	Daemon JVM:    /usr/lib/jvm/java-11-openjdk-amd64 (no JDK specified, using current Java home)
	OS:            Linux 5.15.153.1-microsoft-standard-WSL2+ amd64
```

    - run the ./BUILDER.sh

# Build all by script

* Simply run the ./BUILDER.sh
* After build all compiled components placed to ```ALL_BUILDED_JARS``` folder
* After build all shared dependencies components placed to ```SHARED_JAR_LIBS``` folder
# Build gradle all

gradle clean build -Dskip.tests  -Dorg.gradle.java.home=/usr/lib/jvm/java-17-openjdk-amd64/



# Availiable plugins 

 * Archi -consoleLog -nosplash -application com.archimatetool.commandline.app
   --createEmptyModel
   --importFromCSV "/elements.csv"

 * Archi -consoleLog -nosplash -application com.archimatetool.commandline.app
   --loadModel "/pathToModel/model.archimate"
   --exportToCSV "/pathToOutputFolder"

 * CArchi.exe -consoleLog -nosplash -application com.archimatetool.commandline.app --grafico.import "C:\Users\user\REPOS\ARCHIMATE_TEST_ROOT\test2"  --createEmptyModel --saveModel "C:\Users\user\REPOS\ARCHIMATE_TEST_ROOT\test2.archimate" --pause

 * Archi.exe -consoleLog -nosplash -application com.archimatetool.commandline.app --loadModel "C:\Users\user\REPOS\ARCHIMATE_TEST_ROOT\test1.archimate"  --grafico.export "C:\Users\user\REPOS\ARCHIMATE_TEST_ROOT" --pause