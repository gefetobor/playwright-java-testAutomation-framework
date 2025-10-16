#!/bin/bash

# Playwright Test Automation Framework - Test Runner Script

echo "=========================================="
echo "Playwright Test Automation Framework"
echo "=========================================="

# Check if Java is installed
if ! command -v java &> /dev/null; then
    echo "Error: Java is not installed. Please install Java 11 or higher."
    exit 1
fi

# Check if Maven is installed
if ! command -v mvn &> /dev/null; then
    echo "Error: Maven is not installed. Please install Maven 3.6 or higher."
    exit 1
fi

# Create necessary directories
mkdir -p test-output/screenshots
mkdir -p logs

echo "Setting up test environment..."

# Clean and compile
echo "Cleaning and compiling project..."
mvn clean compile

if [ $? -ne 0 ]; then
    echo "Error: Compilation failed. Please check the code."
    exit 1
fi

echo "Compilation successful!"

# Run tests
echo "Running tests..."
echo "=========================================="

# Check for command line arguments
if [ "$1" = "headless" ]; then
    echo "Running tests in headless mode..."
    mvn test -Dbrowser.headless=true
elif [ "$1" = "firefox" ]; then
    echo "Running tests with Firefox browser..."
    mvn test -Dbrowser.name=firefox
elif [ "$1" = "webkit" ]; then
    echo "Running tests with WebKit browser..."
    mvn test -Dbrowser.name=webkit
elif [ "$1" = "chrome" ]; then
    echo "Running tests with Chrome browser..."
    mvn test -Dbrowser.name=chromium
else
    echo "Running tests with default browser (Chrome)..."
    mvn test
fi

# Check test results
if [ $? -eq 0 ]; then
    echo "=========================================="
    echo "✅ All tests completed successfully!"
    echo "=========================================="
    echo "📊 Test Report: test-output/ExtentReport.html"
    echo "📸 Screenshots: test-output/screenshots/"
    echo "📝 Logs: logs/"
else
    echo "=========================================="
    echo "❌ Some tests failed!"
    echo "=========================================="
    echo "📊 Test Report: test-output/ExtentReport.html"
    echo "📸 Screenshots: test-output/screenshots/"
    echo "📝 Logs: logs/"
    exit 1
fi

echo ""
echo "Usage:"
echo "  ./run-tests.sh           - Run with default browser"
echo "  ./run-tests.sh headless  - Run in headless mode"
echo "  ./run-tests.sh firefox   - Run with Firefox"
echo "  ./run-tests.sh webkit    - Run with WebKit"
echo "  ./run-tests.sh chrome    - Run with Chrome"
