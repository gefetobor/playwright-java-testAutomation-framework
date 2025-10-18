# Playwright Test Automation Framework

A comprehensive Java test automation framework using Playwright, TestNG, ExtentReport, and Log4j for testing web applications with landing page, login page, and dashboard functionality.

## Features

- **Playwright**: Modern web automation with cross-browser support
- **TestNG**: Test framework with parallel execution and reporting
- **ExtentReport**: Beautiful HTML reports with screenshots
- **Log4j**: Comprehensive logging with multiple appenders
- **Page Object Model**: Maintainable and reusable page classes
- **Screenshot Capture**: Automatic screenshots on test failures
- **Configuration Management**: Centralized configuration using properties files

## Project Structure

```
testFramework/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/testautomation/
│   │   │       ├── base/
│   │   │       │   └── BaseTest.java
│   │   │       ├── config/
│   │   │       │   └── ConfigManager.java
│   │   │       ├── pages/
│   │   │       │   ├── BasePage.java
│   │   │       │   ├── LandingPage.java
│   │   │       │   ├── LoginPage.java
│   │   │       │   └── DashboardPage.java
│   │   │       └── utils/
│   │   │           ├── ExtentReportManager.java
│   │   │           └── ScreenshotUtil.java
│   │   └── resources/
│   │       ├── config.properties
│   │       └── log4j2.xml
│   └── test/
│       ├── java/
│       │   └── com/testautomation/
│       │       ├── listeners/
│       │       │   ├── TestListener.java
│       │       │   └── ExtentReportListener.java
│       │       └── tests/
│       │           ├── LandingPageTest.java
│       │           ├── LoginPageTest.java
│       │           ├── DashboardPageTest.java
│       │           └── E2ETest.java
│       └── resources/
│           └── testdata.json
├── testng.xml
├── pom.xml
└── README.md
```

## Prerequisites

- Java 11 or higher
- Maven 3.6 or higher
- Chrome, Firefox, or Safari browser

## Setup

1. **Clone the repository**
   ```bash
   git clone <repository-url>
   cd testFramework
   ```

2. **Install dependencies**
   ```bash
   mvn clean install
   ```

3. **Configure the application**
   - Update `src/main/resources/config.properties` with your application URL and test data
   - Modify browser settings, timeouts, and other configurations as needed

4. **Update test data**
   - Edit `src/test/resources/testdata.json` with your test user credentials
   - Update `src/main/resources/config.properties` with valid test data

## Running Tests

### Run all tests
```bash
mvn test
```

### Run specific test class
```bash
mvn test -Dtest=LandingPageTest
```

### Run with specific browser
```bash
mvn test -Dbrowser.name=firefox
```

### Run in headless mode
```bash
mvn test -Dbrowser.headless=true
```

### Run with custom test suite
```bash
mvn test -DsuiteXmlFile=testng.xml
```
### Run smoke only
```bash
mvn test -Dgroups=smoke
```
# Run regression only
```bash
mvn test -Dgroups=regression
```
# Run everything in testng.xml
```bash
mvn test -Dsurefire.suiteXmlFiles=testng.xml
```

## Test Classes

### LandingPageTest
- Tests landing page functionality
- Verifies page elements visibility
- Tests navigation and scrolling

### LoginPageTest
- Tests login form functionality
- Validates input fields
- Tests error handling
- Verifies remember me and forgot password features

### DashboardPageTest
- Tests dashboard functionality after login
- Verifies user menu and navigation
- Tests search functionality
- Validates page elements

### E2ETest
- Complete user journey tests
- End-to-end workflow validation
- Multiple login attempts testing
- Navigation between pages

## Configuration

### Browser Configuration
```properties
browser.name=chromium
browser.headless=false
browser.width=1920
browser.height=1080
```

### Application Configuration
```properties
app.url=https://example.com
app.timeout=30000
app.implicit.wait=5000
```

### Test Data
```properties
test.user.email=test@example.com
test.user.password=password123
test.user.invalid.email=invalid@example.com
test.user.invalid.password=wrongpassword
```

## Reports

### ExtentReport
- Location: `test-output/ExtentReport.html`
- Includes test results, screenshots, and detailed logs
- Shows test execution timeline and statistics

### Logs
- Console output with colored logs
- File logs: `logs/test-automation.log`
- Rolling file logs: `logs/test-automation-rolling.log`

### Screenshots
- Location: `test-output/screenshots/`
- Captured on test failures
- Named with test method and timestamp

## Page Object Model

The framework uses Page Object Model pattern for maintainable and reusable code:

- **BasePage**: Common functionality for all pages
- **LandingPage**: Landing page specific methods
- **LoginPage**: Login page specific methods
- **DashboardPage**: Dashboard page specific methods

## Logging

The framework uses Log4j2 for comprehensive logging:

- **Console Appender**: Colored output to console
- **File Appender**: Logs saved to files
- **Rolling File Appender**: Automatic log rotation

Log levels:
- `DEBUG`: Detailed information for debugging
- `INFO`: General information about test execution
- `WARN`: Warning messages
- `ERROR`: Error messages and exceptions

## Best Practices

1. **Page Object Model**: Use page classes for all page interactions
2. **Wait Strategies**: Use explicit waits instead of thread.sleep()
3. **Screenshot Capture**: Screenshots are automatically captured on failures
4. **Logging**: Log important steps and assertions
5. **Configuration**: Use configuration files for environment-specific settings
6. **Test Data**: Keep test data separate from test code

## Troubleshooting

### Common Issues

1. **Browser not launching**
   - Check if browser is installed
   - Verify browser configuration in properties file

2. **Tests failing with timeout**
   - Increase timeout values in config.properties
   - Check if application is accessible

3. **Screenshots not captured**
   - Ensure test-output directory exists
   - Check file permissions

4. **Reports not generated**
   - Verify ExtentReport dependencies
   - Check test-output directory permissions

### Debug Mode

Run tests with debug logging:
```bash
mvn test -Dlog4j.configurationFile=src/main/resources/log4j2.xml
```

## Contributing

1. Follow the existing code structure
2. Add proper logging to new methods
3. Include screenshots for UI changes
4. Update documentation for new features
5. Follow naming conventions

## License

This project is licensed under the MIT License.
