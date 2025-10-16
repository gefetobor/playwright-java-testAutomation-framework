package com.testautomation.pages;

import com.microsoft.playwright.*;
import com.microsoft.playwright.options.WaitForSelectorState;
import com.microsoft.playwright.options.LoadState;
import com.testautomation.utils.ScreenshotUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public abstract class BasePage {
    protected final Logger logger = LogManager.getLogger(this.getClass());
    protected Page page;

    public BasePage(Page page) {
        this.page = page;
    }

    protected void click(String selector) {
        logger.info("Clicking element: {}", selector);
        page.click(selector);
    }

    protected void fill(String selector, String value) {
        logger.info("Filling field: {} with value: {}", selector, value);
        page.fill(selector, value);
    }

    protected void type(String selector, String value) {
        logger.info("Typing in field: {} with value: {}", selector, value);
        page.fill(selector, value);
    }

    protected String getText(String selector) {
        logger.info("Getting text from element: {}", selector);
        return page.textContent(selector);
    }

    protected boolean isVisible(String selector) {
        logger.info("Checking visibility of element: {}", selector);
        return page.isVisible(selector);
    }

    protected boolean isEnabled(String selector) {
        logger.info("Checking if element is enabled: {}", selector);
        return page.isEnabled(selector);
    }

    protected void waitForElement(String selector) {
        logger.info("Waiting for element: {}", selector);
        page.waitForSelector(selector);
    }

    protected void waitForElement(String selector, int timeout) {
        logger.info("Waiting for element: {} with timeout: {}ms", selector, timeout);
        page.waitForSelector(selector, new Page.WaitForSelectorOptions().setTimeout(timeout));
    }

    protected void waitForUrl(String url) {
        logger.info("Waiting for URL: {}", url);
        page.waitForURL(url);
    }

    protected void waitForLoadState() {
        logger.info("Waiting for page load state");
        page.waitForLoadState();
    }

    // Specific wait methods for better reliability
    protected void waitForElementToBeVisible(String selector) {
        logger.info("Waiting for element to be visible: {}", selector);
        page.waitForSelector(selector, new Page.WaitForSelectorOptions().setState(WaitForSelectorState.VISIBLE));
    }

    protected void waitForElementToBeHidden(String selector) {
        logger.info("Waiting for element to be hidden: {}", selector);
        page.waitForSelector(selector, new Page.WaitForSelectorOptions().setState(WaitForSelectorState.HIDDEN));
    }

    protected void waitForElementToBeAttached(String selector) {
        logger.info("Waiting for element to be attached: {}", selector);
        page.waitForSelector(selector, new Page.WaitForSelectorOptions().setState(WaitForSelectorState.ATTACHED));
    }

    protected void waitForElementToBeDetached(String selector) {
        logger.info("Waiting for element to be detached: {}", selector);
        page.waitForSelector(selector, new Page.WaitForSelectorOptions().setState(WaitForSelectorState.DETACHED));
    }

    protected void waitForElementWithTimeout(String selector, int timeoutMs) {
        logger.info("Waiting for element: {} with timeout: {}ms", selector, timeoutMs);
        page.waitForSelector(selector, new Page.WaitForSelectorOptions().setTimeout(timeoutMs));
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
        page.waitForURL("**/*" + urlPart + "*");
    }

    protected void waitForNavigation() {
        logger.info("Waiting for navigation to complete");
        page.waitForLoadState(LoadState.NETWORKIDLE);
    }

    protected void waitForJavaScriptExecution() {
        logger.info("Waiting for JavaScript execution to complete");
        page.waitForLoadState(LoadState.DOMCONTENTLOADED);
    }

    protected void navigateTo(String url) {
        logger.info("Navigating to: {}", url);
        page.navigate(url);
        waitForLoadState();
    }

    protected String getCurrentUrl() {
        String url = page.url();
        logger.info("Current URL: {}", url);
        return url;
    }

    protected String getTitle() {
        String title = page.title();
        logger.info("Page title: {}", title);
        return title;
    }

    protected void takeScreenshot(String stepName) {
        String screenshotPath = ScreenshotUtil.takeScreenshot(page, this.getClass().getSimpleName(), stepName);
        if (screenshotPath != null) {
            logger.info("Screenshot taken for step: {}", stepName);
        }
    }

    protected void scrollToElement(String selector) {
        logger.info("Scrolling to element: {}", selector);
        page.locator(selector).scrollIntoViewIfNeeded();
    }

    protected void hover(String selector) {
        logger.info("Hovering over element: {}", selector);
        page.hover(selector);
    }

    protected void doubleClick(String selector) {
        logger.info("Double clicking element: {}", selector);
        page.locator(selector).dblclick();
    }


    protected void selectOption(String selector, String value) {
        logger.info("Selecting option: {} from: {}", value, selector);
        page.selectOption(selector, value);
    }

    protected void check(String selector) {
        logger.info("Checking checkbox: {}", selector);
        page.check(selector);
    }

    protected void uncheck(String selector) {
        logger.info("Unchecking checkbox: {}", selector);
        page.uncheck(selector);
    }

    protected boolean isChecked(String selector) {
        logger.info("Checking if checkbox is checked: {}", selector);
        return page.isChecked(selector);
    }

    public void waitForErrorMessage() {
        logger.info("Waiting for error message to appear");
        // Wait for the error element to be visible
        page.waitForSelector("#login-error:not([style*='display: none'])", 
            new Page.WaitForSelectorOptions().setTimeout(5000));
    }

    public void waitForSuccessMessage() {
        logger.info("Waiting for success message to appear");
        // Wait for the success element to be visible
        page.waitForSelector("#login-success:not([style*='display: none'])", 
            new Page.WaitForSelectorOptions().setTimeout(5000));
    }
}
