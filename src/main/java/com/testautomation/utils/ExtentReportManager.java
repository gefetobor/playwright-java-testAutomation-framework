package com.testautomation.utils;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;
import com.testautomation.config.ConfigManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;

public class ExtentReportManager {
    private static final Logger logger = LogManager.getLogger(ExtentReportManager.class);
    private static ExtentReports extent;
    private static ExtentTest test;

    public static ExtentReports getInstance() {
        if (extent == null) {
            createInstance();
        }
        return extent;
    }

    private static void createInstance() {
        try {
            String reportPath = ConfigManager.getProperty("report.path", "test-output/ExtentReport.html");
            
            // Create test-output directory if it doesn't exist
            File reportDir = new File("test-output");
            if (!reportDir.exists()) {
                reportDir.mkdirs();
            }

            ExtentSparkReporter sparkReporter = new ExtentSparkReporter(reportPath);
            sparkReporter.config().setDocumentTitle(ConfigManager.getProperty("report.title", "Playwright Test Automation Report"));
            sparkReporter.config().setReportName(ConfigManager.getProperty("report.name", "Test Execution Report"));
            sparkReporter.config().setTheme(Theme.STANDARD);
            sparkReporter.config().setTimeStampFormat("yyyy-MM-dd HH:mm:ss");

            extent = new ExtentReports();
            extent.attachReporter(sparkReporter);
            extent.setSystemInfo("OS", System.getProperty("os.name"));
            extent.setSystemInfo("Java Version", System.getProperty("java.version"));
            extent.setSystemInfo("Browser", ConfigManager.getProperty("browser.name", "chromium"));
            extent.setSystemInfo("Environment", "Test");

            logger.info("ExtentReport instance created successfully");
        } catch (Exception e) {
            logger.error("Failed to create ExtentReport instance", e);
            throw new RuntimeException("Failed to initialize ExtentReport", e);
        }
    }

    public static ExtentTest createTest(String testName) {
        test = getInstance().createTest(testName);
        logger.info("Created test: {}", testName);
        return test;
    }

    public static ExtentTest createTest(String testName, String description) {
        test = getInstance().createTest(testName, description);
        logger.info("Created test: {} - {}", testName, description);
        return test;
    }

    public static ExtentTest getTest() {
        return test;
    }

    public static void flush() {
        if (extent != null) {
            extent.flush();
            logger.info("ExtentReport flushed successfully");
        }
    }

    public static void addScreenshot(String screenshotPath) {
        if (test != null && screenshotPath != null) {
            test.addScreenCaptureFromPath(screenshotPath);
            logger.info("Screenshot added to report: {}", screenshotPath);
        }
    }

    public static void logInfo(String message) {
        if (test != null) {
            test.info(message);
            logger.info("ExtentReport - INFO: {}", message);
        }
    }

    public static void logPass(String message) {
        if (test != null) {
            test.pass(message);
            logger.info("ExtentReport - PASS: {}", message);
        }
    }

    public static void logFail(String message) {
        if (test != null) {
            test.fail(message);
            logger.error("ExtentReport - FAIL: {}", message);
        }
    }

    public static void logSkip(String message) {
        if (test != null) {
            test.skip(message);
            logger.warn("ExtentReport - SKIP: {}", message);
        }
    }
}
