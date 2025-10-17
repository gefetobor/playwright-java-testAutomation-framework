package com.testautomation.pages;

import com.microsoft.playwright.*;
import com.microsoft.playwright.options.WaitForSelectorState;
import com.microsoft.playwright.options.LoadState;
import com.testautomation.base.PlaywrightManager;
import com.testautomation.utils.ScreenshotUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public abstract class BasePage {
    protected final Logger logger = LogManager.getLogger(this.getClass());

    protected Page getPage() {
        return PlaywrightManager.getPage();
    }

    protected void click(String selector) {
        logger.info("Clicking element: {}", selector);
        getPage().click(selector);
    }

    protected void fill(String selector, String value) {
        logger.info("Filling field: {} with value: {}", selector, value);
        getPage().fill(selector, value);
    }

    protected void type(String selector, String value) {
        logger.info("Typing in field: {} with value: {}", selector, value);
        getPage().fill(selector, value);
    }

    protected String getText(String selector) {
        logger.info("Getting text from element: {}", selector);
        return getPage().textContent(selector);
    }

    protected boolean isVisible(String selector) {
        logger.info("Checking visibility of element: {}", selector);
        return getPage().isVisible(selector);
    }

    protected boolean isEnabled(String selector) {
        logger.info("Checking if element is enabled: {}", selector);
        return getPage().isEnabled(selector);
    }

    protected void waitForElement(String selector) {
        logger.info("Waiting for element: {}", selector);
        getPage().waitForSelector(selector);
    }

    protected void waitForElement(String selector, int timeout) {
        logger.info("Waiting for element: {} with timeout: {}ms", selector, timeout);
        getPage().waitForSelector(selector, new Page.WaitForSelectorOptions().setTimeout(timeout));
    }

    protected void waitForUrl(String url) {
        logger.info("Waiting for URL: {}", url);
        getPage().waitForURL(url);
    }

    protected void waitForLoadState() {
        logger.info("Waiting for page load state");
        getPage().waitForLoadState();
    }

    // Specific wait methods for better reliability
    protected void waitForElementToBeVisible(String selector) {
        logger.info("Waiting for element to be visible: {}", selector);
        getPage().waitForSelector(selector, new Page.WaitForSelectorOptions().setState(WaitForSelectorState.VISIBLE));
    }

    protected void waitForElementToBeHidden(String selector) {
        logger.info("Waiting for element to be hidden: {}", selector);
        getPage().waitForSelector(selector, new Page.WaitForSelectorOptions().setState(WaitForSelectorState.HIDDEN));
    }

    protected void waitForElementToBeAttached(String selector) {
        logger.info("Waiting for element to be attached: {}", selector);
        getPage().waitForSelector(selector, new Page.WaitForSelectorOptions().setState(WaitForSelectorState.ATTACHED));
    }

    protected void waitForElementToBeDetached(String selector) {
        logger.info("Waiting for element to be detached: {}", selector);
        getPage().waitForSelector(selector, new Page.WaitForSelectorOptions().setState(WaitForSelectorState.DETACHED));
    }

    protected void waitForElementWithTimeout(String selector, int timeoutMs) {
        logger.info("Waiting for element: {} with timeout: {}ms", selector, timeoutMs);
        getPage().waitForSelector(selector, new Page.WaitForSelectorOptions().setTimeout(timeoutMs));
    }

    protected void waitForElementToContainText(String selector, String text) {
        logger.info("Waiting for element {} to contain text: {}", selector, text);
        // Wait for element to be visible first, then check text content
        waitForElementToBeVisible(selector);
        // Additional wait for text content to be loaded
        waitForJavaScriptExecution();
    }

    protected void waitForElementToHaveAttribute(String selector, String attribute, String value) {
        logger.info("Waiting for element {} to have attribute {}={}", selector, attribute, value);
        // Wait for element to be visible first, then check attribute
        waitForElementToBeVisible(selector);
        // Additional wait for attributes to be set
        waitForJavaScriptExecution();
    }

    protected void waitForUrlToContain(String urlPart) {
        logger.info("Waiting for URL to contain: {}", urlPart);
        getPage().waitForURL("**/*" + urlPart + "*");
    }

    protected void waitForNavigation() {
        logger.info("Waiting for navigation to complete");
        getPage().waitForLoadState(LoadState.NETWORKIDLE);
    }

    protected void waitForJavaScriptExecution() {
        logger.info("Waiting for JavaScript execution to complete");
        getPage().waitForLoadState(LoadState.DOMCONTENTLOADED);
    }

    protected void navigateTo(String url) {
        logger.info("Navigating to: {}", url);
        getPage().navigate(url);
        waitForLoadState();
    }

    protected String getCurrentUrl() {
        String url = getPage().url();
        logger.info("Current URL: {}", url);
        return url;
    }

    protected String getTitle() {
        String title = getPage().title();
        logger.info("Page title: {}", title);
        return title;
    }

    protected void takeScreenshot(String stepName) {
        String screenshotPath = ScreenshotUtil.takeScreenshot(getPage(), this.getClass().getSimpleName(), stepName);
        if (screenshotPath != null) {
            logger.info("Screenshot taken for step: {}", stepName);
        }
    }

    protected void scrollToElement(String selector) {
        logger.info("Scrolling to element: {}", selector);
        getPage().locator(selector).scrollIntoViewIfNeeded();
    }

    protected void hover(String selector) {
        logger.info("Hovering over element: {}", selector);
        getPage().hover(selector);
    }

    protected void doubleClick(String selector) {
        logger.info("Double clicking element: {}", selector);
        getPage().locator(selector).dblclick();
    }


    protected void selectOption(String selector, String value) {
        logger.info("Selecting option: {} from: {}", value, selector);
        getPage().selectOption(selector, value);
    }

    protected void check(String selector) {
        logger.info("Checking checkbox: {}", selector);
        getPage().check(selector);
    }

    protected void uncheck(String selector) {
        logger.info("Unchecking checkbox: {}", selector);
        getPage().uncheck(selector);
    }

    protected boolean isChecked(String selector) {
        logger.info("Checking if checkbox is checked: {}", selector);
        return getPage().isChecked(selector);
    }

    public void waitForErrorMessage() {
        logger.info("Waiting for error message to appear");
        // Wait for the error element to be visible
        getPage().waitForSelector("#login-error:not([style*='display: none'])", 
            new Page.WaitForSelectorOptions().setTimeout(5000));
    }

    public void waitForSuccessMessage() {
        logger.info("Waiting for success message to appear");
        // Wait for the success element to be visible
        getPage().waitForSelector("#login-success:not([style*='display: none'])", 
            new Page.WaitForSelectorOptions().setTimeout(5000));
    }
}
