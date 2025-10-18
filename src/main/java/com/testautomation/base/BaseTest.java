package com.testautomation.base;

import com.testautomation.utils.ExtentReportManager;
import com.testautomation.utils.ScreenshotUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;

import java.lang.reflect.Method;

public class BaseTest {
    protected static final Logger logger = LogManager.getLogger(BaseTest.class);

    @BeforeSuite
    public void setUpSuite() {
        logger.info("Setting up test suite");
        PlaywrightManager.setUp();
    }

    @BeforeMethod
    public void setUp(Method method) {
        logger.info("Setting up test method: {} in thread: {}", method.getName(), Thread.currentThread().getName());
        
        // Ensure Playwright is set up for this thread if not already done
        if (PlaywrightManager.getPage() == null) {
            logger.info("Playwright not initialized for thread {}, setting up now", Thread.currentThread().getName());
            PlaywrightManager.setUp();
        }
        
        ExtentReportManager.createTest(method.getName());
    }

    @AfterMethod
    public void tearDown(Method method, ITestResult result) {
        logger.info("Tearing down test method: {} in thread: {}", method.getName(), Thread.currentThread().getName());
        
        ExtentReportManager.cleanup();
        ExtentReportManager.flush();
    }

    @AfterSuite
    public void tearDownSuite() {
        logger.info("Tearing down test suite");
        PlaywrightManager.tearDown();
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