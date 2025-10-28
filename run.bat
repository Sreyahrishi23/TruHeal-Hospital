@echo off
echo Starting Hospital Management System...

REM Set Java environment
set JAVA_HOME=C:\Program Files\Java\jdk-25
set PATH=%JAVA_HOME%\bin;%PATH%

REM Compile sources (output to current directory to create hospital/*.class)
javac -cp ".;lib\mysql-connector-java-8.1.0.jar" -d . *.java

REM Run the application with MySQL driver in classpath
java -cp ".;lib\mysql-connector-java-8.1.0.jar" hospital.LoginPage

pause

