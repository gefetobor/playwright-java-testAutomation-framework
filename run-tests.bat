@echo off
REM Playwright Test Automation Framework - Test Runner Script (Windows)

echo ==========================================
echo Playwright Test Automation Framework
echo ==========================================

REM Check if Java is installed
java -version >nul 2>&1
if %errorlevel% neq 0 (
    echo Error: Java is not installed. Please install Java 11 or higher.
    pause
    exit /b 1
)

REM Check if Maven is installed
mvn -version >nul 2>&1
if %errorlevel% neq 0 (
    echo Error: Maven is not installed. Please install Maven 3.6 or higher.
    pause
    exit /b 1
)

REM Create necessary directories
if not exist "test-output\screenshots" mkdir "test-output\screenshots"
if not exist "logs" mkdir "logs"

echo Setting up test environment...

REM Clean and compile
echo Cleaning and compiling project...
mvn clean compile

if %errorlevel% neq 0 (
    echo Error: Compilation failed. Please check the code.
    pause
    exit /b 1
)

echo Compilation successful!

REM Run tests
echo Running tests...
echo ==========================================

REM Check for command line arguments
if "%1"=="headless" (
    echo Running tests in headless mode...
    mvn test -Dbrowser.headless=true
) else if "%1"=="firefox" (
    echo Running tests with Firefox browser...
    mvn test -Dbrowser.name=firefox
) else if "%1"=="webkit" (
    echo Running tests with WebKit browser...
    mvn test -Dbrowser.name=webkit
) else if "%1"=="chrome" (
    echo Running tests with Chrome browser...
    mvn test -Dbrowser.name=chromium
) else (
    echo Running tests with default browser (Chrome)...
    mvn test
)

REM Check test results
if %errorlevel% equ 0 (
    echo ==========================================
    echo ✅ All tests completed successfully!
    echo ==========================================
    echo 📊 Test Report: test-output\ExtentReport.html
    echo 📸 Screenshots: test-output\screenshots\
    echo 📝 Logs: logs\
) else (
    echo ==========================================
    echo ❌ Some tests failed!
    echo ==========================================
    echo 📊 Test Report: test-output\ExtentReport.html
    echo 📸 Screenshots: test-output\screenshots\
    echo 📝 Logs: logs\
    pause
    exit /b 1
)

echo.
echo Usage:
echo   run-tests.bat           - Run with default browser
echo   run-tests.bat headless  - Run in headless mode
echo   run-tests.bat firefox   - Run with Firefox
echo   run-tests.bat webkit    - Run with WebKit
echo   run-tests.bat chrome    - Run with Chrome

pause
