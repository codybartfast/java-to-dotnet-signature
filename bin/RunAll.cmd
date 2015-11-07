@echo off
echo Hello
set binDir="%~dp0"
set workingDir=%binDir%..\working\

if not exist %workingDir% md %workingDir%
cd %workingDir%

echo About to run CreateKeysJ
pause
java -cp ..\bin\;%CLASSPATH% CreateKeysJ

echo About to run SignMessageJ
pause
java -cp ..\bin\;%CLASSPATH% SignMessageJ

echo About to run VerifyMessageN
pause
..\bin\VerifyMessageN.exe
