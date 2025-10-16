package com.testautomation.listeners;

import com.testautomation.utils.ExtentReportManager;
import com.testautomation.utils.ScreenshotUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

public class TestListener implements ITestListener {
    private static final Logger logger = LogManager.getLogger(TestListener.class);

    @Override
    public void onTestStart(ITestResult result) {
        logger.info("Test started: {}", result.getMethod().getMethodName());
        ExtentReportManager.createTest(result.getMethod().getMethodName(), 
                                     result.getMethod().getDescription());
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        logger.info("Test passed: {}", result.getMethod().getMethodName());
        ExtentReportManager.logPass("Test passed successfully: " + result.getMethod().getMethodName());
    }

    @Override
    public void onTestFailure(ITestResult result) {
        logger.error("Test failed: {}", result.getMethod().getMethodName());
        logger.error("Failure reason: {}", result.getThrowable().getMessage());
        
        ExtentReportManager.logFail("Test failed: " + result.getMethod().getMethodName());
        ExtentReportManager.logFail("Failure reason: " + result.getThrowable().getMessage());
        
        // Add stack trace to report
        if (result.getThrowable() != null) {
            ExtentReportManager.logFail("Stack trace: " + 
                java.util.Arrays.toString(result.getThrowable().getStackTrace()));
        }
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        logger.warn("Test skipped: {}", result.getMethod().getMethodName());
        ExtentReportManager.logSkip("Test skipped: " + result.getMethod().getMethodName());
        
        if (result.getThrowable() != null) {
            ExtentReportManager.logSkip("Skip reason: " + result.getThrowable().getMessage());
        }
    }

    @Override
    public void onTestFailedButWithinSuccessPercentage(ITestResult result) {
        logger.warn("Test failed but within success percentage: {}", result.getMethod().getMethodName());
        ExtentReportManager.logFail("Test failed but within success percentage: " + result.getMethod().getMethodName());
    }

    @Override
    public void onStart(ITestContext context) {
        logger.info("Test suite started: {}", context.getName());
        ExtentReportManager.getInstance();
    }

    @Override
    public void onFinish(ITestContext context) {
        logger.info("Test suite finished: {}", context.getName());
        ExtentReportManager.flush();
    }
}
