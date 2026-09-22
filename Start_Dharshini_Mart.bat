@echo off
title Dharshini Mart
echo.
echo Starting Dharshini Mart
echo =======================
echo.

REM Change directory to E:\DharshiniMart\mart (works from Explorer, handles spaces)
cd /d "%~dp0"
if errorlevel 1 (
    echo [ERROR] Failed to change directory to "%~dp0"
    pause
    exit /b 1
)
echo Folder: %CD%
echo.

REM Check Java
where java >nul 2>&1
if errorlevel 1 (
    echo [ERROR] Java not found in PATH.
    echo Please install Java 21 from https://adoptium.net/temurin/releases/?version=21
    pause
    exit /b 1
)
echo [OK] Java found:
java -version 2>&1
echo.

REM Check mvnw.cmd
if not exist "%~dp0mvnw.cmd" (
    echo [ERROR] mvnw.cmd not found in "%~dp0"
    echo Expected: E:\DharshiniMart\mart\mvnw.cmd
    pause
    exit /b 1
)
echo [OK] Found mvnw.cmd
echo.

REM If already running, just open browser
powershell -NoProfile -ExecutionPolicy Bypass -Command "try { $null=Invoke-WebRequest -Uri 'http://localhost:8080' -UseBasicParsing -TimeoutSec 3; exit 0 } catch { exit 1 }" >nul 2>&1
if not errorlevel 1 (
    echo [INFO] Server already running at http://localhost:8080
    echo Opening browser...
    start "" "http://localhost:8080"
    echo Done. Keep the existing window open.
    pause
    exit /b 0
)

echo [INFO] Starting Spring Boot via mvnw.cmd spring-boot:run
echo Server logs will appear below. Keep this window open while using the app.
echo Browser will open automatically at http://localhost:8080 once ready.
echo.

REM Background waiter to open browser when ready
start "" /b powershell -NoProfile -ExecutionPolicy Bypass -Command "$c=0; while($c -lt 60){ try{ $r=Invoke-WebRequest -Uri 'http://localhost:8080' -UseBasicParsing -TimeoutSec 2; if($r.StatusCode -eq 200){ Start-Process 'http://localhost:8080'; break } }catch{}; Start-Sleep -Seconds 2; $c++ }"

REM Start existing Spring Boot application - KEEP running, visible
call "%~dp0mvnw.cmd" spring-boot:run

echo.
echo [INFO] Server process has stopped.
echo If it failed, check errors above.
echo.
pause
exit /b 0