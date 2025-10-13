@echo off
setlocal

:: ===============================
:: CONFIGURATION
:: ===============================
set APP_NAME=JavaFx-Login
set MAIN_CLASS=com.authenticate.util.Launcher
set MAIN_JAR=javafx-springcore-login-1.0.0.jar
set TARGET_DIR=%~dp0target
set INPUT_DIR=%~dp0build
set RUNTIME_DIR=%~dp0my-runtime
set ICON_FILE=%~dp0app.ico
set LICENSE_FILE=%~dp0license_desc.key
set VENDOR="A & M Techies Pvt. Ltd."
set COPYRIGHT="© 2025 A & M Techies Pvt. Ltd. All rights reserved."
set USER_LICENSE_DIR=%LOCALAPPDATA%\JavaFx-Login

echo.
echo =====================================================
echo  Building JavaFX Runtime and Installer for %APP_NAME%
echo =====================================================
echo.

:: ===============================
:: 0. DETECT JAVAFX SDK FOLDER
:: ===============================
echo [STEP 0] Detecting installed JavaFX SDK...
for /d %%D in ("C:\Program Files\javafx-sdk-*") do set JAVAFX_SDK=%%D
if not defined JAVAFX_SDK (
    echo ❌ JavaFX SDK not found in "C:\Program Files\". Please install it first.
    pause
    exit /b 1
)
echo Detected JavaFX SDK at: "%JAVAFX_SDK%"
echo.

:: ===============================
:: 1. CLEAN OLD FILES
:: ===============================
echo [STEP 1] Cleaning previous runtime and installer...

if exist "%RUNTIME_DIR%" (
    echo Found existing runtime directory "%RUNTIME_DIR%". Deleting...
    rmdir /s /q "%RUNTIME_DIR%"
    if errorlevel 1 (
        echo ❌ Failed to delete existing runtime directory.
        pause
        exit /b 1
    )
    echo Old runtime deleted successfully.
) else (
    echo No existing runtime found.
)

if exist "%APP_NAME%-1.0.exe" (
    echo Found old installer "%APP_NAME%-1.0.exe". Deleting...
    del /q "%APP_NAME%-1.0.exe"
)
echo Done.
echo.

:: ===============================
:: 2. PREPARE INPUT FOLDER (COPY MAIN JAR)
:: ===============================
echo [STEP 2] Preparing input folder...
if not exist "%INPUT_DIR%" (
    mkdir "%INPUT_DIR%"
)
copy /y "%TARGET_DIR%\%MAIN_JAR%" "%INPUT_DIR%\" >nul
if errorlevel 1 (
    echo ❌ Failed to copy main JAR to input folder.
    pause
    exit /b 1
)
echo Main JAR copied to input folder successfully.
echo.

:: ===============================
:: 3. CREATE CUSTOM RUNTIME IMAGE
:: ===============================
echo [STEP 3] Creating runtime image using jlink...
"%JAVA_HOME%\bin\jlink.exe" ^
  --module-path "%JAVA_HOME%\jmods;%JAVAFX_SDK%\lib" ^
  --add-modules java.base,javafx.controls,javafx.fxml,javafx.graphics,java.desktop,java.sql,java.naming,java.xml,java.logging,jdk.unsupported ^
  --output "%RUNTIME_DIR%" ^
  --strip-debug ^
  --compress=2 ^
  --no-header-files ^
  --no-man-pages

if errorlevel 1 (
  echo ❌ ERROR: jlink failed.
  pause
  exit /b 1
)
echo Runtime image created successfully.
echo.

:: ===============================
:: 3.1 COPY JAVAFX DLLs INTO RUNTIME
:: ===============================
echo [STEP 3.1] Copying JavaFX DLL files to runtime bin folder...
xcopy "%JAVAFX_SDK%\bin\*.dll" "%RUNTIME_DIR%\bin\" /Y /Q
if errorlevel 1 (
    echo ⚠️ Warning: Failed to copy some DLL files.
) else (
    echo DLL files copied successfully.
)
echo.

:: ===============================
:: 4. PACKAGE INSTALLER WITH JPACKAGE
:: ===============================
echo [STEP 4] Packaging installer using jpackage...
"%JAVA_HOME%\bin\jpackage.exe" ^
  --type exe ^
  --input build ^
  --name "%APP_NAME%" ^
  --main-jar "%MAIN_JAR%" ^
  --main-class "%MAIN_CLASS%" ^
  --runtime-image "%RUNTIME_DIR%" ^
  --icon "%ICON_FILE%" ^
  --win-menu ^
  --win-shortcut ^
  --win-dir-chooser ^
  --install-dir "%APP_NAME%" ^
  --vendor %VENDOR% ^
  --copyright %COPYRIGHT% ^
  --license-file "license_desc.txt" ^
  --java-options "-Dprism.order=sw -splash:images/splash.png"

if errorlevel 1 (
  echo ❌ ERROR: jpackage failed.
  pause
  exit /b 1
)
echo Installer created successfully!
echo.

:: ===============================
:: 5. PREPARE LICENSE FOLDER (USER APPDATA)
:: ===============================
echo [STEP 5] Preparing user license folder...
if not exist "%USER_LICENSE_DIR%" (
    mkdir "%USER_LICENSE_DIR%"
    echo License folder created at "%USER_LICENSE_DIR%"
)
if exist "%LICENSE_FILE%" (
    copy /y "%LICENSE_FILE%" "%USER_LICENSE_DIR%" >nul
)
echo Done.
echo.

:: ===============================
:: 6. RUN TEST (OPTIONAL)
:: ===============================
echo [STEP 6] Launching built JAR for testing...
"%RUNTIME_DIR%\bin\java.exe" ^
  --module-path "%JAVA_HOME%\jmods;%JAVAFX_SDK%\lib" ^
  --add-modules javafx.controls,javafx.fxml,javafx.graphics,java.desktop,java.sql,java.naming ^
  -Dprism.verbose=true ^
  -jar "%INPUT_DIR%\%MAIN_JAR%" > "%~dp0startup-log.txt" 2>&1

if errorlevel 1 (
  echo App failed to launch. Check startup-log.txt for details.
) else (
  echo App launched successfully.
)

echo.
echo =====================================================
echo   Build Completed. Check installer and startup-log.txt
echo =====================================================
pause
endlocal
