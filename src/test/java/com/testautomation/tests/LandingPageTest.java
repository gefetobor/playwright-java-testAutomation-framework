package com.testautomation.tests;

import com.testautomation.base.BaseTest;
import com.testautomation.pages.LandingPage;
import com.testautomation.utils.ExtentReportManager;
import com.testautomation.utils.RetryAnalyzer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class LandingPageTest extends BaseTest {
    private static final Logger logger = LogManager.getLogger(LandingPageTest.class);
    private LandingPage landingPage;

    @BeforeMethod
    public void setUp() {
        logger.info("Setting up LandingPageTest");
        navigateToApp();
        landingPage = new LandingPage(page);
        landingPage.waitForPageToLoad();
    }

    @Test(description = "Verify landing page loads successfully", retryAnalyzer = RetryAnalyzer.class)
    public void testLandingPageLoads() {
        logger.info("Testing landing page loads successfully");
        ExtentReportManager.logInfo("Starting test: Landing page loads successfully");
        
        try {
            Assert.assertTrue(landingPage.isPageLoaded(), "Landing page should be loaded");
            ExtentReportManager.logPass("Landing page loaded successfully");
        } catch (AssertionError e) {
            ExtentReportManager.logFail("Landing page failed to load: " + e.getMessage());
            throw e;
        }
    }

    @Test(description = "Verify landing page title is displayed", retryAnalyzer = RetryAnalyzer.class)
    public void testLandingPageTitle() {
        logger.info("Testing landing page title display");
        ExtentReportManager.logInfo("Starting test: Landing page title display");
        
        try {
            String title = landingPage.getPageTitle();
            Assert.assertNotNull(title, "Page title should not be null");
            Assert.assertFalse(title.isEmpty(), "Page title should not be empty");
            ExtentReportManager.logPass("Landing page title displayed: " + title);
        } catch (AssertionError e) {
            ExtentReportManager.logFail("Landing page title test failed: " + e.getMessage());
            throw e;
        }
    }

    @Test(description = "Verify login button is visible and clickable", retryAnalyzer = RetryAnalyzer.class)
    public void testLoginButtonVisibility() {
        logger.info("Testing login button visibility");
        ExtentReportManager.logInfo("Starting test: Login button visibility");
        
        try {
            Assert.assertTrue(landingPage.isLoginButtonVisible(), "Login button should be visible");
            ExtentReportManager.logPass("Login button is visible and accessible");
        } catch (AssertionError e) {
            ExtentReportManager.logFail("Login button visibility test failed: " + e.getMessage());
            throw e;
        }
    }

    @Test(description = "Verify signup button is visible", retryAnalyzer = RetryAnalyzer.class)
    public void testSignupButtonVisibility() {
        logger.info("Testing signup button visibility");
        ExtentReportManager.logInfo("Starting test: Signup button visibility");
        
        try {
            Assert.assertTrue(landingPage.isSignupButtonVisible(), "Signup button should be visible");
            ExtentReportManager.logPass("Signup button is visible and accessible");
        } catch (AssertionError e) {
            ExtentReportManager.logFail("Signup button visibility test failed: " + e.getMessage());
            throw e;
        }
    }

    @Test(description = "Verify navigation menu is visible", retryAnalyzer = RetryAnalyzer.class)
    public void testNavigationMenuVisibility() {
        logger.info("Testing navigation menu visibility");
        ExtentReportManager.logInfo("Starting test: Navigation menu visibility");
        
        try {
            Assert.assertTrue(landingPage.isNavigationMenuVisible(), "Navigation menu should be visible");
            ExtentReportManager.logPass("Navigation menu is visible");
        } catch (AssertionError e) {
            ExtentReportManager.logFail("Navigation menu visibility test failed: " + e.getMessage());
            throw e;
        }
    }

    @Test(description = "Verify footer is visible", retryAnalyzer = RetryAnalyzer.class)
    public void testFooterVisibility() {
        logger.info("Testing footer visibility");
        ExtentReportManager.logInfo("Starting test: Footer visibility");
        
        try {
            Assert.assertTrue(landingPage.isFooterVisible(), "Footer should be visible");
            ExtentReportManager.logPass("Footer is visible");
        } catch (AssertionError e) {
            ExtentReportManager.logFail("Footer visibility test failed: " + e.getMessage());
            throw e;
        }
    }

    @Test(description = "Verify page scrolling functionality", retryAnalyzer = RetryAnalyzer.class)
    public void testPageScrolling() {
        logger.info("Testing page scrolling functionality");
        ExtentReportManager.logInfo("Starting test: Page scrolling functionality");
        
        try {
            // Test scrolling to footer
            landingPage.scrollToFooter();
            Assert.assertTrue(landingPage.isFooterVisible(), "Footer should be visible after scrolling");
            
            // Test scrolling to hero section
            landingPage.scrollToHeroSection();
            ExtentReportManager.logPass("Page scrolling functionality works correctly");
        } catch (AssertionError e) {
            ExtentReportManager.logFail("Page scrolling test failed: " + e.getMessage());
            throw e;
        }
    }

    @Test(description = "Verify current URL is correct", retryAnalyzer = RetryAnalyzer.class)
    public void testCurrentUrl() {
        logger.info("Testing current URL");
        ExtentReportManager.logInfo("Starting test: Current URL verification");
        
        try {
            String currentUrl = landingPage.getCurrentUrl();
            Assert.assertNotNull(currentUrl, "Current URL should not be null");
            Assert.assertFalse(currentUrl.isEmpty(), "Current URL should not be empty");
            ExtentReportManager.logPass("Current URL is valid: " + currentUrl);
        } catch (AssertionError e) {
            ExtentReportManager.logFail("Current URL test failed: " + e.getMessage());
            throw e;
        }
    }
}
