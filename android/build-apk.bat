@echo off
setlocal EnableExtensions
set "ROOT=%~dp0"
cd /d "%ROOT%"

echo.
echo  ZAKOU KERNEL - Android debug APK builder
echo  ----------------------------------------
echo.

rem --- Android SDK discovery -------------------------------------------------
if not defined ANDROID_HOME if exist "%LOCALAPPDATA%\Android\Sdk" set "ANDROID_HOME=%LOCALAPPDATA%\Android\Sdk"
if not defined ANDROID_SDK_ROOT if defined ANDROID_HOME set "ANDROID_SDK_ROOT=%ANDROID_HOME%"
if not defined ANDROID_HOME if not defined ANDROID_SDK_ROOT (
  echo [!] Android SDK not found.
  echo     Install Android Studio and SDK Platform 34, or set ANDROID_HOME.
  echo.
  pause
  exit /b 1
)

rem --- JDK discovery ---------------------------------------------------------
if not defined JAVA_HOME if exist "%ProgramFiles%\Android\Android Studio\jbr\bin\java.exe" set "JAVA_HOME=%ProgramFiles%\Android\Android Studio\jbr"
if defined JAVA_HOME (
  if not exist "%JAVA_HOME%\bin\java.exe" set "JAVA_HOME="
)
if not defined JAVA_HOME (
  where java >nul 2>nul
  if errorlevel 1 (
    echo [!] JDK 17 not found.
    echo     Install Android Studio, or install JDK 17 and set JAVA_HOME.
    echo.
    pause
    exit /b 1
  )
)

rem --- Prefer a local Gradle, otherwise download Gradle 8.7 once ------------
where gradle >nul 2>nul
if not errorlevel 1 (
  echo [*] Using Gradle from PATH...
  call gradle -p "%ROOT%" assembleDebug
  goto :finish
)

set "GRADLE_VERSION=8.7"
set "CACHE=%USERPROFILE%\.gradle\zakou-gradle"
set "DIST=%CACHE%\gradle-%GRADLE_VERSION%"

if not exist "%DIST%\bin\gradle.bat" (
  echo [*] Gradle %GRADLE_VERSION% not found. Downloading it once...
  if not exist "%CACHE%" mkdir "%CACHE%"
  powershell -NoProfile -ExecutionPolicy Bypass -Command "$ErrorActionPreference='Stop'; $zip=Join-Path '%CACHE%' 'gradle-%GRADLE_VERSION%-bin.zip'; if(-not (Test-Path $zip)){ Invoke-WebRequest -Uri 'https://services.gradle.org/distributions/gradle-%GRADLE_VERSION%-bin.zip' -OutFile $zip }; Expand-Archive -Path $zip -DestinationPath '%CACHE%' -Force"
  if errorlevel 1 (
    echo [!] Gradle download failed. Check your network, or install Gradle 8.7.
    echo.
    pause
    exit /b 1
  )
)

if not exist "%DIST%\bin\gradle.bat" (
  echo [!] Gradle extraction failed.
  echo.
  pause
  exit /b 1
)

call "%DIST%\bin\gradle.bat" -p "%ROOT%" assembleDebug

:finish
if errorlevel 1 (
  echo.
  echo [!] Build failed. Read the Gradle output above.
  echo     Common causes: missing SDK 34, JDK not 17, or no network for dependencies.
  echo.
  pause
  exit /b 1
)

echo.
echo [OK] APK created:
echo      %ROOT%app\build\outputs\apk\debug\app-debug.apk
echo.
pause
