@echo off
REM Lightweight bootstrapper: downloads Gradle distribution once and runs it.
SET scriptDir=%~dp0
powershell -NoProfile -ExecutionPolicy Bypass -File "%scriptDir%bootstrap-gradle.ps1" %*
exit /b %ERRORLEVEL%
