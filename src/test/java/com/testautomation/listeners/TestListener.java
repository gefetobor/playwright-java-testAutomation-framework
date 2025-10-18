package com.testautomation.listeners;

import com.testautomation.base.PlaywrightManager;
import com.testautomation.utils.ExtentReportManager;
import com.testautomation.utils.ScreenshotUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

import java.nio.file.Files;
import java.nio.file.Paths;

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
        ExtentReportManager.logPass("Test execution completed successfully");
        
        // Add screenshot for successful tests
        try {
            String screenshotPath = ScreenshotUtil.takeScreenshot(PlaywrightManager.getPage(), result.getMethod().getMethodName() + "_success");
            if (screenshotPath != null) {
                ExtentReportManager.addScreenshot(screenshotPath);
                logger.info("Success screenshot added to report: {}", screenshotPath);
            }
        } catch (Exception e) {
            logger.warn("Could not capture success screenshot: {}", e.getMessage());
        }
    }

    @Override
    public void onTestFailure(ITestResult result) {
        logger.error("Test failed: {}", result.getMethod().getMethodName());
        ExtentReportManager.logFail("Test execution failed");
        
        // Add comprehensive failure details
        if (result.getThrowable() != null) {
            Throwable throwable = result.getThrowable();
            ExtentReportManager.logFail("Exception Type: " + throwable.getClass().getSimpleName());
            ExtentReportManager.logFail("Exception Message: " + throwable.getMessage());
            
            // Add full stack trace
            String stackTrace = getStackTrace(throwable);
            ExtentReportManager.logFail("Stack Trace: " + stackTrace);
        }
        
        // Add failure screenshot
        try {
            String screenshotPath = ScreenshotUtil.takeScreenshot(PlaywrightManager.getPage(), result.getMethod().getMethodName() + "_failure");
            if (screenshotPath != null) {
                ExtentReportManager.addScreenshot(screenshotPath);
                logger.error("Failure screenshot added to report: {}", screenshotPath);
            }
        } catch (Exception e) {
            logger.error("Could not capture failure screenshot: {}", e.getMessage());
        }
        
        // Add HTML page source on failure
        try {
            String pageSource = PlaywrightManager.getPage().content();
            String pageSourcePath = "test-output/page-sources/" + result.getMethod().getMethodName() + "_failure.html";
            
            // Create directory if it doesn't exist
            Files.createDirectories(Paths.get("test-output/page-sources"));
            
            // Save page source
            Files.write(Paths.get(pageSourcePath), pageSource.getBytes());
            
            // Add to report
            ExtentReportManager.logFail("Page source saved: " + pageSourcePath);
            logger.error("Page source saved: {}", pageSourcePath);
        } catch (Exception e) {
            logger.error("Could not capture page source: {}", e.getMessage());
        }
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        logger.warn("Test skipped: {}", result.getMethod().getMethodName());
        ExtentReportManager.logSkip("Test execution was skipped");
        
        // Add reason for skipping
        if (result.getThrowable() != null) {
            ExtentReportManager.logSkip("Skip reason: " + result.getThrowable().getMessage());
        }
    }

    @Override
    public void onTestFailedButWithinSuccessPercentage(ITestResult result) {
        logger.warn("Test failed but within success percentage: {}", result.getMethod().getMethodName());
        ExtentReportManager.logFail("Test failed but within success percentage");
        
        // Add failure details for partial success
        if (result.getThrowable() != null) {
            ExtentReportManager.logFail("Exception: " + result.getThrowable().getClass().getSimpleName());
            ExtentReportManager.logFail("Message: " + result.getThrowable().getMessage());
        }
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
    
    private String getStackTrace(Throwable throwable) {
        StringBuilder sb = new StringBuilder();
        sb.append(throwable.toString()).append("\n");
        
        for (StackTraceElement element : throwable.getStackTrace()) {
            sb.append("    at ").append(element.toString()).append("\n");
        }
        
        // Add cause if present
        Throwable cause = throwable.getCause();
        if (cause != null) {
            sb.append("Caused by: ").append(cause.toString()).append("\n");
            for (StackTraceElement element : cause.getStackTrace()) {
                sb.append("    at ").append(element.toString()).append("\n");
            }
        }
        
        return sb.toString();
    }
}
