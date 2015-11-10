:: Runs the three bits of code in this project.
::  - writes key pairs in 'java' and '.net' formats using Java.
::  - writes a message and then writes its signature using private key in Java.
::  - reads the message, signature and public key and verifies it in .Net.

:: Runs in a 'working' directory that is the sibling of the directory from
:: which this batch file is run.

:: Assumes java is on the path.

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
