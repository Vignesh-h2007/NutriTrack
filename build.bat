@echo off
setlocal enabledelayedexpansion
if not exist "target\classes" mkdir "target\classes"
dir /s /b src\main\java\*.java > sources.txt
javac -encoding UTF-8 -cp "lib\javax.servlet-api-4.0.1.jar;lib\mysql-connector-j-8.0.33.jar" -d target\classes @sources.txt
if %ERRORLEVEL% equ 0 (
    echo Compilation successful!
    if exist sources.txt del sources.txt
) else (
    echo Compilation failed!
    exit /b %ERRORLEVEL%
)
