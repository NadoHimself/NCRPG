@echo off
REM ============================================
REM NCRPG Local Build Script
REM Builds the plugin without requiring Hytale API
REM ============================================

echo.
echo ========================================
echo   NCRPG - Local Build Script
echo   Version 1.0.0
echo ========================================
echo.

REM Check if Java is installed
java -version >nul 2>&1
if %errorlevel% neq 0 (
    echo [ERROR] Java is not installed or not in PATH!
    echo Please install Java 21+ from https://adoptium.net/
    pause
    exit /b 1
)

echo [INFO] Java detected
java -version
echo.

REM Check if we're in the right directory
if not exist "build.gradle" (
    echo [ERROR] build.gradle not found!
    echo Please run this script from the NCRPG root directory.
    pause
    exit /b 1
)

echo [INFO] Project directory confirmed
echo.

REM Clean previous builds
echo [STEP 1/3] Cleaning previous builds...
if exist "build" (
    rmdir /s /q build
    echo [OK] Build directory cleaned
) else (
    echo [OK] No previous build found
)
echo.

REM Option 1: Build with Gradle (ignoring compilation errors)
echo [STEP 2/3] Building with Gradle...
echo [INFO] Note: Compilation errors are expected without Hytale API
echo.

call gradlew.bat clean build -x test --continue 2>nul
if %errorlevel% neq 0 (
    echo [WARN] Build completed with errors (expected)
) else (
    echo [OK] Build successful!
)
echo.

REM Check if JAR was created
echo [STEP 3/3] Checking build output...
if exist "build\libs\NCRPG-1.0.0.jar" (
    echo.
    echo ========================================
    echo   BUILD SUCCESSFUL!
    echo ========================================
    echo.
    echo JAR Location: build\libs\NCRPG-1.0.0.jar
    echo.
    dir /b build\libs\*.jar
    echo.
    echo [NEXT STEPS]
    echo 1. Copy JAR to: %%appdata%%\Hytale\UserData\Mods\
    echo 2. Configure database in config.yml
    echo 3. Start your Hytale server
    echo 4. Test with: /skills
    echo.
    echo See QUICKSTART.md for detailed instructions.
    echo.
) else (
    echo.
    echo ========================================
    echo   BUILD INCOMPLETE
    echo ========================================
    echo.
    echo [INFO] JAR not found - this is expected without Hytale API
    echo.
    echo To build successfully:
    echo 1. Obtain HytaleServer.jar from your Hytale installation
    echo 2. Install it locally:
    echo    mvn install:install-file -Dfile=HytaleServer.jar ^
    echo        -DgroupId=com.hypixel.hytale ^
    echo        -DartifactId=hytale-server ^
    echo        -Dversion=1.0.0 ^
    echo        -Dpackaging=jar
    echo 3. Run this script again
    echo.
    echo See QUICKSTART.md for full instructions.
    echo.
)

pause
