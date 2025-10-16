package com.testautomation.listeners;

import com.testautomation.utils.ExtentReportManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

public class ExtentReportListener implements ITestListener {
    private static final Logger logger = LogManager.getLogger(ExtentReportListener.class);

    @Override
    public void onTestStart(ITestResult result) {
        logger.info("ExtentReport - Test started: {}", result.getMethod().getMethodName());
        // Test creation is handled in TestListener
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        logger.info("ExtentReport - Test passed: {}", result.getMethod().getMethodName());
        ExtentReportManager.logPass("Test execution completed successfully");
    }

    @Override
    public void onTestFailure(ITestResult result) {
        logger.error("ExtentReport - Test failed: {}", result.getMethod().getMethodName());
        ExtentReportManager.logFail("Test execution failed");
        
        // Add failure details
        if (result.getThrowable() != null) {
            ExtentReportManager.logFail("Exception: " + result.getThrowable().getClass().getSimpleName());
            ExtentReportManager.logFail("Message: " + result.getThrowable().getMessage());
        }
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        logger.warn("ExtentReport - Test skipped: {}", result.getMethod().getMethodName());
        ExtentReportManager.logSkip("Test execution was skipped");
    }

    @Override
    public void onTestFailedButWithinSuccessPercentage(ITestResult result) {
        logger.warn("ExtentReport - Test failed but within success percentage: {}", result.getMethod().getMethodName());
        ExtentReportManager.logFail("Test failed but within success percentage");
    }

    @Override
    public void onStart(ITestContext context) {
        logger.info("ExtentReport - Test suite started: {}", context.getName());
        // ExtentReport instance creation is handled in TestListener
    }

    @Override
    public void onFinish(ITestContext context) {
        logger.info("ExtentReport - Test suite finished: {}", context.getName());
        // Flush is handled in TestListener
    }
}
