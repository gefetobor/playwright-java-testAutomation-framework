package com.testautomation.pages;

import com.microsoft.playwright.Page;
import com.testautomation.utils.ExtentReportManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class LandingPage extends BasePage {
    private static final Logger logger = LogManager.getLogger(LandingPage.class);

    // Page Elements
    private static final String LOGIN_BUTTON = "a[href*='login'], button:has-text('Login'), .login-btn";
    private static final String SIGNUP_BUTTON = "a[href*='signup'], button:has-text('Sign Up'), .signup-btn";
    private static final String PAGE_TITLE = "h1, .page-title, .hero-title";
    private static final String NAVIGATION_MENU = ".nav-menu, .navigation, nav";
    private static final String HERO_SECTION = ".hero, .landing-hero, .banner";
    private static final String FOOTER = "footer, .footer";

    public LandingPage() {
        // No constructor needed - using PlaywrightManager
    }

    public boolean isPageLoaded() {
        logger.info("Checking if landing page is loaded");
        boolean isLoaded = isVisible(PAGE_TITLE) || isVisible(HERO_SECTION);
        ExtentReportManager.logInfo("Landing page loaded: " + isLoaded);
        return isLoaded;
    }

    public String getPageTitle() {
        logger.info("Getting landing page title");
        String title = getText(PAGE_TITLE);
        ExtentReportManager.logInfo("Landing page title: " + title);
        return title;
    }

    public boolean isLoginButtonVisible() {
        logger.info("Checking if login button is visible");
        boolean isVisible = isVisible(LOGIN_BUTTON);
        ExtentReportManager.logInfo("Login button visible: " + isVisible);
        return isVisible;
    }

    public boolean isSignupButtonVisible() {
        logger.info("Checking if signup button is visible");
        boolean isVisible = isVisible(SIGNUP_BUTTON);
        ExtentReportManager.logInfo("Signup button visible: " + isVisible);
        return isVisible;
    }

    public LoginPage clickLoginButton() {
        logger.info("Clicking login button");
        takeScreenshot("before_login_click");
        click(LOGIN_BUTTON);
        
        // Wait for the login form to become visible (since it's a single-page app)
        waitForElementToBeVisible("#login-page:not(.hidden)");
        waitForElementToBeVisible("#email");
        waitForElementToBeVisible("#password");
        
        takeScreenshot("after_login_click");
        ExtentReportManager.logInfo("Login button clicked successfully");
        return new LoginPage();
    }

    public void clickSignupButton() {
        logger.info("Clicking signup button");
        takeScreenshot("before_signup_click");
        click(SIGNUP_BUTTON);
        // Wait for any signup-related elements to appear
        waitForJavaScriptExecution();
        takeScreenshot("after_signup_click");
        ExtentReportManager.logInfo("Signup button clicked successfully");
    }

    public boolean isNavigationMenuVisible() {
        logger.info("Checking if navigation menu is visible");
        boolean isVisible = isVisible(NAVIGATION_MENU);
        ExtentReportManager.logInfo("Navigation menu visible: " + isVisible);
        return isVisible;
    }

    public boolean isFooterVisible() {
        logger.info("Checking if footer is visible");
        boolean isVisible = isVisible(FOOTER);
        ExtentReportManager.logInfo("Footer visible: " + isVisible);
        return isVisible;
    }

    public void scrollToFooter() {
        logger.info("Scrolling to footer");
        scrollToElement(FOOTER);
        takeScreenshot("footer_scrolled");
        ExtentReportManager.logInfo("Scrolled to footer successfully");
    }

    public void scrollToHeroSection() {
        logger.info("Scrolling to hero section");
        scrollToElement(HERO_SECTION);
        takeScreenshot("hero_section_scrolled");
        ExtentReportManager.logInfo("Scrolled to hero section successfully");
    }

    public String getCurrentUrl() {
        String url = super.getCurrentUrl();
        ExtentReportManager.logInfo("Current URL: " + url);
        return url;
    }

    public void waitForPageToLoad() {
        logger.info("Waiting for landing page to load completely");
        // Wait for the landing page section to be visible (not hidden)
        waitForElementToBeVisible("#landing-page:not(.hidden)");
        waitForElementToBeVisible(PAGE_TITLE);
        waitForElementToBeVisible(LOGIN_BUTTON);
        waitForElementToBeVisible(SIGNUP_BUTTON);
        takeScreenshot("landing_page_loaded");
        ExtentReportManager.logInfo("Landing page loaded completely");
    }
}
