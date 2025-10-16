package com.testautomation.tests;

import com.testautomation.base.BaseTest;
import com.testautomation.utils.ExtentReportManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.Test;

public class SimpleTest extends BaseTest {
    private static final Logger logger = LogManager.getLogger(SimpleTest.class);

    @Test(description = "Simple test without browser")
    public void testSimpleAssertion() {
        logger.info("Running simple test without browser");
        ExtentReportManager.logInfo("Starting simple test");
        
        // Simple assertion test
        String testString = "Hello World";
        Assert.assertEquals(testString, "Hello World", "String should match");
        
        ExtentReportManager.logPass("Simple test passed successfully");
        logger.info("Simple test completed successfully");
    }

    @Test(description = "Test configuration loading")
    public void testConfigurationLoading() {
        logger.info("Testing configuration loading");
        ExtentReportManager.logInfo("Testing configuration loading");
        
        // Test that configuration is loaded
        String appUrl = com.testautomation.config.ConfigManager.getProperty("app.url");
        Assert.assertNotNull(appUrl, "App URL should not be null");
        Assert.assertFalse(appUrl.isEmpty(), "App URL should not be empty");
        
        ExtentReportManager.logPass("Configuration loaded successfully: " + appUrl);
        logger.info("Configuration test completed successfully");
    }
}
