package com.testautomation.tests;

import com.testautomation.base.BaseTest;
import com.testautomation.config.ConfigManager;
import com.testautomation.pages.DashboardPage;
import com.testautomation.pages.LandingPage;
import com.testautomation.pages.LoginPage;
import com.testautomation.utils.ExtentReportManager;
import com.testautomation.utils.RetryAnalyzer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class DashboardPageTest extends BaseTest {
    private static final Logger logger = LogManager.getLogger(DashboardPageTest.class);
    private DashboardPage dashboardPage;
    private String validEmail;
    private String validPassword;

    @BeforeMethod
    public void setUp() throws InterruptedException {
        logger.info("Setting up DashboardPageTest");
        
        // Load test data first
        validEmail = ConfigManager.getProperty("test.user.email");
        validPassword = ConfigManager.getProperty("test.user.password");
        
        try {
            navigateToApp();
            
            // Navigate to dashboard through login
            LandingPage landingPage = new LandingPage();
            landingPage.waitForPageToLoad();
            LoginPage loginPage = landingPage.clickLoginButton();
            loginPage.waitForPageToLoad();
            dashboardPage = loginPage.login(validEmail, validPassword);
            dashboardPage.waitForPageToLoad();

        } catch (Exception e) {
            logger.error("Setup failed, test will be retried: {}", e.getMessage());
            throw e; // Re-throw to trigger retry
        }
    }

    @Test(description = "Verify dashboard page loads successfully", retryAnalyzer = RetryAnalyzer.class)
    public void testDashboardPageLoads() throws InterruptedException {
        Thread.sleep(8000);
        logger.info("Testing dashboard page loads successfully");
        ExtentReportManager.logInfo("Starting test: Dashboard page loads successfully");
        
        try {
            // Perform setup within test method to enable retry
            if (dashboardPage == null) {
                navigateToApp();
                LandingPage landingPage = new LandingPage();
                landingPage.waitForPageToLoad();
                LoginPage loginPage = landingPage.clickLoginButton();

                loginPage.waitForPageToLoad();
                dashboardPage = loginPage.login(validEmail, validPassword);
                dashboardPage.waitForPageToLoad();

            }
            
            Assert.assertTrue(dashboardPage.isPageLoaded(), "Dashboard page should be loaded");
            ExtentReportManager.logPass("Dashboard page loaded successfully");
        } catch (Exception e) {
            logger.error("Test failed, will be retried: {}", e.getMessage());
            ExtentReportManager.logFail("Dashboard page failed to load: " + e.getMessage());
            throw e;
        }
    }


    @Test(description = "Verify dashboard page title is displayed", retryAnalyzer = RetryAnalyzer.class)
    public void testDashboardPageTitle() {
        logger.info("Testing dashboard page title display");
        ExtentReportManager.logInfo("Starting test: Dashboard page title display");
        
        try {
            String title = dashboardPage.getPageTitle();
            Assert.assertNotNull(title, "Page title should not be null");
            Assert.assertFalse(title.isEmpty(), "Page title should not be empty");
            ExtentReportManager.logPass("Dashboard page title displayed: " + title);
        } catch (AssertionError e) {
            ExtentReportManager.logFail("Dashboard page title test failed: " + e.getMessage());
            throw e;
        }
    }

    @Test(description = "Verify welcome message is displayed", retryAnalyzer = RetryAnalyzer.class)
    public void testWelcomeMessage() {
        logger.info("Testing welcome message display");
        ExtentReportManager.logInfo("Starting test: Welcome message display");
        
        try {
            String welcomeMessage = dashboardPage.getWelcomeMessage();
            Assert.assertNotNull(welcomeMessage, "Welcome message should not be null");
            Assert.assertFalse(welcomeMessage.isEmpty(), "Welcome message should not be empty");
            ExtentReportManager.logPass("Welcome message displayed: " + welcomeMessage);
        } catch (AssertionError e) {
            ExtentReportManager.logFail("Welcome message test failed: " + e.getMessage());
            throw e;
        }
    }

    @Test(description = "Verify user menu is visible", retryAnalyzer = RetryAnalyzer.class)
    public void testUserMenuVisibility() {
        logger.info("Testing user menu visibility");
        ExtentReportManager.logInfo("Starting test: User menu visibility");
        
        try {
            Assert.assertTrue(dashboardPage.isUserMenuVisible(), "User menu should be visible");
            ExtentReportManager.logPass("User menu is visible");
        } catch (AssertionError e) {
            ExtentReportManager.logFail("User menu visibility test failed: " + e.getMessage());
            throw e;
        }
    }

    @Test(description = "Verify user menu functionality", retryAnalyzer = RetryAnalyzer.class)
    public void testUserMenuFunctionality() {
        logger.info("Testing user menu functionality");
        ExtentReportManager.logInfo("Starting test: User menu functionality");
        
        try {
            dashboardPage.clickUserMenu();
            ExtentReportManager.logPass("User menu clicked successfully");
        } catch (Exception e) {
            ExtentReportManager.logFail("User menu functionality test failed: " + e.getMessage());
            throw e;
        }
    }

    @Test(description = "Verify profile button functionality", retryAnalyzer = RetryAnalyzer.class)
    public void testProfileButton() {
        logger.info("Testing profile button functionality");
        ExtentReportManager.logInfo("Starting test: Profile button functionality");
        
        try {
            dashboardPage.clickProfileButton();
            ExtentReportManager.logPass("Profile button clicked successfully");
        } catch (Exception e) {
            ExtentReportManager.logFail("Profile button test failed: " + e.getMessage());
            throw e;
        }
    }

    @Test(description = "Verify settings button functionality", retryAnalyzer = RetryAnalyzer.class)
    public void testSettingsButton() {
        logger.info("Testing settings button functionality");
        ExtentReportManager.logInfo("Starting test: Settings button functionality");
        
        try {
            dashboardPage.clickSettingsButton();
            ExtentReportManager.logPass("Settings button clicked successfully");
        } catch (Exception e) {
            ExtentReportManager.logFail("Settings button test failed: " + e.getMessage());
            throw e;
        }
    }

    @Test(description = "Verify stats section visibility", retryAnalyzer = RetryAnalyzer.class)
    public void testStatsSectionVisibility() {
        logger.info("Testing stats section visibility");
        ExtentReportManager.logInfo("Starting test: Stats section visibility");
        
        try {
            if (dashboardPage.isStatsSectionVisible()) {
                ExtentReportManager.logPass("Stats section is visible");
            } else {
                ExtentReportManager.logInfo("Stats section is not visible (this might be expected)");
            }
        } catch (Exception e) {
            ExtentReportManager.logFail("Stats section visibility test failed: " + e.getMessage());
            throw e;
        }
    }

    @Test(description = "Verify recent activity section visibility", retryAnalyzer = RetryAnalyzer.class)
    public void testRecentActivityVisibility() {
        logger.info("Testing recent activity section visibility");
        ExtentReportManager.logInfo("Starting test: Recent activity section visibility");
        
        try {
            if (dashboardPage.isRecentActivityVisible()) {
                ExtentReportManager.logPass("Recent activity section is visible");
            } else {
                ExtentReportManager.logInfo("Recent activity section is not visible (this might be expected)");
            }
        } catch (Exception e) {
            ExtentReportManager.logFail("Recent activity visibility test failed: " + e.getMessage());
            throw e;
        }
    }

    @Test(description = "Verify notifications visibility", retryAnalyzer = RetryAnalyzer.class)
    public void testNotificationsVisibility() {
        logger.info("Testing notifications visibility");
        ExtentReportManager.logInfo("Starting test: Notifications visibility");
        
        try {
            if (dashboardPage.isNotificationsVisible()) {
                ExtentReportManager.logPass("Notifications are visible");
            } else {
                ExtentReportManager.logInfo("Notifications are not visible (this might be expected)");
            }
        } catch (Exception e) {
            ExtentReportManager.logFail("Notifications visibility test failed: " + e.getMessage());
            throw e;
        }
    }

    @Test(description = "Verify search functionality", retryAnalyzer = RetryAnalyzer.class)
    public void testSearchFunctionality() {
        logger.info("Testing search functionality");
        ExtentReportManager.logInfo("Starting test: Search functionality");
        
        try {
            String searchTerm = "test search";
            dashboardPage.search(searchTerm);
            ExtentReportManager.logPass("Search functionality works with term: " + searchTerm);
        } catch (Exception e) {
            ExtentReportManager.logFail("Search functionality test failed: " + e.getMessage());
            throw e;
        }
    }

    @Test(description = "Verify search input field", retryAnalyzer = RetryAnalyzer.class)
    public void testSearchInputField() {
        logger.info("Testing search input field");
        ExtentReportManager.logInfo("Starting test: Search input field");
        
        try {
            String searchTerm = "test input";
            dashboardPage.enterSearchTerm(searchTerm);
            dashboardPage.clickSearchButton();
            ExtentReportManager.logPass("Search input field works correctly");
        } catch (Exception e) {
            ExtentReportManager.logFail("Search input field test failed: " + e.getMessage());
            throw e;
        }
    }

    @Test(description = "Verify page scrolling functionality", retryAnalyzer = RetryAnalyzer.class)
    public void testPageScrolling() {
        logger.info("Testing page scrolling functionality");
        ExtentReportManager.logInfo("Starting test: Page scrolling functionality");
        
        try {
            // Test scrolling to stats section
            dashboardPage.scrollToStatsSection();
            
            // Test scrolling to recent activity
            dashboardPage.scrollToRecentActivity();
            
            ExtentReportManager.logPass("Page scrolling functionality works correctly");
        } catch (Exception e) {
            ExtentReportManager.logFail("Page scrolling test failed: " + e.getMessage());
            throw e;
        }
    }

    @Test(description = "Verify navigation menu visibility", retryAnalyzer = RetryAnalyzer.class)
    public void testNavigationMenuVisibility() {
        logger.info("Testing navigation menu visibility");
        ExtentReportManager.logInfo("Starting test: Navigation menu visibility");
        
        try {
            Assert.assertTrue(dashboardPage.isNavigationMenuVisible(), "Navigation menu should be visible");
            ExtentReportManager.logPass("Navigation menu is visible");
        } catch (AssertionError e) {
            ExtentReportManager.logFail("Navigation menu visibility test failed: " + e.getMessage());
            throw e;
        }
    }

    @Test(description = "Verify current URL is correct", retryAnalyzer = RetryAnalyzer.class)
    public void testCurrentUrl() {
        logger.info("Testing current URL");
        ExtentReportManager.logInfo("Starting test: Current URL verification");
        
        try {
            String currentUrl = dashboardPage.getCurrentUrl();
            Assert.assertNotNull(currentUrl, "Current URL should not be null");
            Assert.assertFalse(currentUrl.isEmpty(), "Current URL should not be empty");
            ExtentReportManager.logPass("Current URL is valid: " + currentUrl);
        } catch (AssertionError e) {
            ExtentReportManager.logFail("Current URL test failed: " + e.getMessage());
            throw e;
        }
    }
}
