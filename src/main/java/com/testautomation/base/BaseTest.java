package com.testautomation.base;

import com.testautomation.utils.ExtentReportManager;
import com.testautomation.utils.ScreenshotUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

import java.lang.reflect.Method;

public class BaseTest {
    protected static final Logger logger = LogManager.getLogger(BaseTest.class);

    @BeforeMethod
    public void setUp(Method method) {
        logger.info("Setting up test method: {} in thread: {}", method.getName(), Thread.currentThread().getName());
        
        PlaywrightManager.setUp();
        ExtentReportManager.createTest(method.getName());
        ExtentReportManager.getTest().info("Starting test: " + method.getName());
    }

    @AfterMethod
    public void tearDown(Method method, ITestResult result) {
        logger.info("Tearing down test method: {} in thread: {}", method.getName(), Thread.currentThread().getName());
        
        // Take screenshot on failure
        if (result.getStatus() == ITestResult.FAILURE) {
            String screenshotPath = ScreenshotUtil.takeScreenshot(PlaywrightManager.getPage(), method.getName());
            logger.error("Test failed. Screenshot saved: {}", screenshotPath);
            ExtentReportManager.addScreenshot(screenshotPath);
        }
        
        ExtentReportManager.getTest().info("Finished test: " + method.getName());
        PlaywrightManager.tearDown();
        ExtentReportManager.cleanup();
        ExtentReportManager.flush();
    }

    protected void navigateToUrl(String url) {
        logger.info("Navigating to URL: {}", url);
        PlaywrightManager.getPage().navigate(url);
        PlaywrightManager.getPage().waitForLoadState();
    }

    protected void navigateToApp() {
        String appUrl = com.testautomation.config.ConfigManager.getProperty("app.url");
        navigateToUrl(appUrl);
    }
}