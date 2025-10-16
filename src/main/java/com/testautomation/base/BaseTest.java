package com.testautomation.base;

import com.microsoft.playwright.*;
import com.testautomation.config.ConfigManager;
import com.testautomation.utils.ScreenshotUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.ITestResult;
import org.testng.annotations.*;

public class BaseTest {
    protected static final Logger logger = LogManager.getLogger(BaseTest.class);
    protected Playwright playwright;
    protected Browser browser;
    protected BrowserContext context;
    protected Page page;

    @BeforeMethod
    public void setUpMethod() {
        logger.info("Setting up test method");

        // Create a new playwright and browser instance for each test method (like the working project)
        playwright = Playwright.create();
        String browserName = ConfigManager.getProperty("browser.name");
        boolean headless = ConfigManager.getBooleanProperty("browser.headless");
        
        try {
            switch (browserName.toLowerCase()) {
                case "firefox":
                    browser = playwright.firefox().launch(new BrowserType.LaunchOptions().setHeadless(headless));
                    break;
                case "webkit":
                    browser = playwright.webkit().launch(new BrowserType.LaunchOptions().setHeadless(headless));
                    break;
                case "chromium":
                default:
                    browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(headless));
            }
            
           /* context = browser.newContext(new Browser.NewContextOptions()
                    .setViewportSize(ConfigManager.getIntProperty("browser.width", 1920), 
                                   ConfigManager.getIntProperty("browser.height", 1080)));*/

            context = browser.newContext(new Browser.NewContextOptions());
            page = context.newPage();
            
            // Set default timeout
            page.setDefaultTimeout(ConfigManager.getIntProperty("app.timeout", 30000));
            page.setDefaultNavigationTimeout(ConfigManager.getIntProperty("app.timeout", 30000));

            logger.info("Browser launched: {} (headless: {})", browserName, headless);
           // logger.info("New page created with viewport: {}x{}", ConfigManager.getIntProperty("browser.width", 1920), ConfigManager.getIntProperty("browser.height", 1080));
        } catch (Exception e) {
            logger.error("Failed to create browser: {}", e.getMessage());
            throw new RuntimeException("Failed to create browser", e);
        }
    }

    @AfterMethod
    public void tearDownMethod(ITestResult result) {
        logger.info("Tearing down test method");
        
        // Take screenshot on failure
        if (result.getStatus() == ITestResult.FAILURE) {
            String screenshotPath = ScreenshotUtil.takeScreenshot(page, result.getMethod().getMethodName());
            logger.error("Test failed. Screenshot saved: {}", screenshotPath);
        }
        
        // Close resources in the same order as the working project
        if (browser != null) {
            browser.close();
            browser = null;
        }
        if (playwright != null) {
            playwright.close();
            playwright = null;
        }
        if (context != null) {
            context = null;
        }
        if (page != null) {
            page = null;
        }
        logger.info("Test method teardown completed");
    }

    protected void navigateToUrl(String url) {
        logger.info("Navigating to URL: {}", url);
        page.navigate(url);
        page.waitForLoadState();
    }

    protected void navigateToApp() {
        String appUrl = ConfigManager.getProperty("app.url");
        navigateToUrl(appUrl);
    }
}
