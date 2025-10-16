package com.testautomation.pages;

import com.microsoft.playwright.Page;
import com.testautomation.utils.ExtentReportManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class LoginPage extends BasePage {
    private static final Logger logger = LogManager.getLogger(LoginPage.class);

    // Page Elements
    private static final String EMAIL_FIELD = "#email";
    private static final String PASSWORD_FIELD = "#password";
    private static final String LOGIN_BUTTON = "form.login-form button[type='submit']";
    private static final String REMEMBER_ME_CHECKBOX = "#remember";
    private static final String FORGOT_PASSWORD_LINK = "a[onclick='showForgotPassword()']";
    private static final String ERROR_MESSAGE = "#login-error";
    private static final String SUCCESS_MESSAGE = ".success, .alert-success, .login-success";
    private static final String PAGE_TITLE = "h1, h2, .page-title, .login-title";
    private static final String BACK_TO_HOME_LINK = "a[onclick='showLanding()']";

    public LoginPage(Page page) {
        super(page);
    }

    public boolean isPageLoaded() {
        logger.info("Checking if login page is loaded");
        boolean isLoaded = isVisible(EMAIL_FIELD) && isVisible(PASSWORD_FIELD) && isVisible(LOGIN_BUTTON);
        ExtentReportManager.logInfo("Login page loaded: " + isLoaded);
        return isLoaded;
    }

    public String getPageTitle() {
        logger.info("Getting login page title");
        String title = getText(PAGE_TITLE);
        ExtentReportManager.logInfo("Login page title: " + title);
        return title;
    }

    public void enterEmail(String email) {
        logger.info("Entering email: {}", email);
        takeScreenshot("before_email_entry");
        fill(EMAIL_FIELD, email);
        takeScreenshot("after_email_entry");
        ExtentReportManager.logInfo("Email entered: " + email);
    }

    public void enterPassword(String password) {
        logger.info("Entering password");
        takeScreenshot("before_password_entry");
        fill(PASSWORD_FIELD, password);
        takeScreenshot("after_password_entry");
        ExtentReportManager.logInfo("Password entered successfully");
    }

    public DashboardPage clickLoginButton() {
        logger.info("Clicking login button");
        takeScreenshot("before_login_submit");
        click(LOGIN_BUTTON);
        
        // Wait for the dashboard section to become visible (since it's a single-page app)
        waitForElementToBeVisible("#dashboard-page:not(.hidden)");
        waitForElementToBeVisible("#dashboard-page h1");
        waitForElementToBeVisible("#dashboard-page .user-menu button");
        
        takeScreenshot("after_login_submit");
        ExtentReportManager.logInfo("Login button clicked successfully");
        return new DashboardPage(page);
    }

    public void clickLoginButtonExpectingError() {
        logger.info("Clicking login button expecting error");
        takeScreenshot("before_login_submit_error");
        click(LOGIN_BUTTON);
        // Wait for either error message or form validation
        waitForJavaScriptExecution();
        takeScreenshot("after_login_submit_error");
        ExtentReportManager.logInfo("Login button clicked (expecting error)");
    }

    public DashboardPage login(String email, String password) {
        logger.info("Performing login with email: {}", email);
        enterEmail(email);
        enterPassword(password);
        return clickLoginButton();
    }

    public void loginExpectingError(String email, String password) {
        logger.info("Performing login expecting error with email: {}", email);
        enterEmail(email);
        enterPassword(password);
        clickLoginButtonExpectingError();
    }

    public boolean isErrorVisible() {
        logger.info("Checking if error message is visible");
        try {
            // Check if the error element is visible and has content
            Object result = page.evaluate("() => { const el = document.querySelector('#login-error'); return el && el.style.display !== 'none' && el.textContent.trim() !== ''; }");
            boolean isVisible = result instanceof Boolean ? (Boolean) result : false;
            ExtentReportManager.logInfo("Error message visible: " + isVisible);
            return isVisible;
        } catch (Exception e) {
            logger.warn("Error checking error message visibility: {}", e.getMessage());
            return false;
        }
    }

    public String getErrorMessage() {
        logger.info("Getting error message");
        String errorMessage = getText(ERROR_MESSAGE);
        ExtentReportManager.logInfo("Error message: " + errorMessage);
        return errorMessage;
    }

    public boolean isSuccessMessageVisible() {
        logger.info("Checking if success message is visible");
        boolean isVisible = isVisible(SUCCESS_MESSAGE);
        ExtentReportManager.logInfo("Success message visible: " + isVisible);
        return isVisible;
    }

    public String getSuccessMessage() {
        logger.info("Getting success message");
        String successMessage = getText(SUCCESS_MESSAGE);
        ExtentReportManager.logInfo("Success message: " + successMessage);
        return successMessage;
    }

    public void clickRememberMe() {
        logger.info("Clicking remember me checkbox");
        takeScreenshot("before_remember_me");
        check(REMEMBER_ME_CHECKBOX);
        takeScreenshot("after_remember_me");
        ExtentReportManager.logInfo("Remember me checkbox clicked");
    }

    public boolean isRememberMeChecked() {
        logger.info("Checking if remember me is checked");
        boolean isChecked = isChecked(REMEMBER_ME_CHECKBOX);
        ExtentReportManager.logInfo("Remember me checked: " + isChecked);
        return isChecked;
    }

    public void clickForgotPassword() {
        logger.info("Clicking forgot password link");
        takeScreenshot("before_forgot_password");
        click(FORGOT_PASSWORD_LINK);
        // Wait for any forgot password modal or page to appear
        waitForJavaScriptExecution();
        takeScreenshot("after_forgot_password");
        ExtentReportManager.logInfo("Forgot password link clicked");
    }

    public LandingPage clickBackToHome() {
        logger.info("Clicking back to home link");
        takeScreenshot("before_back_to_home");
        click(BACK_TO_HOME_LINK);
        
        // Wait for the landing page section to become visible (since it's a single-page app)
        waitForElementToBeVisible("#landing-page:not(.hidden)");
        waitForElementToBeVisible("#landing-page h1");
        waitForElementToBeVisible("#landing-page .login-btn");
        
        takeScreenshot("after_back_to_home");
        ExtentReportManager.logInfo("Back to home link clicked");
        return new LandingPage(page);
    }

    public void clearEmailField() {
        logger.info("Clearing email field");
        click(EMAIL_FIELD);
        page.keyboard().press("Control+a");
        page.keyboard().press("Delete");
        ExtentReportManager.logInfo("Email field cleared");
    }

    public void clearPasswordField() {
        logger.info("Clearing password field");
        click(PASSWORD_FIELD);
        page.keyboard().press("Control+a");
        page.keyboard().press("Delete");
        ExtentReportManager.logInfo("Password field cleared");
    }

    public void waitForPageToLoad() {
        logger.info("Waiting for login page to load completely");
        // Wait for the login page section to be visible (not hidden)
        waitForElementToBeVisible("#login-page:not(.hidden)");
        waitForElementToBeVisible(EMAIL_FIELD);
        waitForElementToBeVisible(PASSWORD_FIELD);
        waitForElementToBeVisible(LOGIN_BUTTON);
        waitForElementToBeVisible(REMEMBER_ME_CHECKBOX);
        takeScreenshot("login_page_loaded");
        ExtentReportManager.logInfo("Login page loaded completely");
    }
}
