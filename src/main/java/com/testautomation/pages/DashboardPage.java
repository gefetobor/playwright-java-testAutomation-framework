package com.testautomation.pages;

import com.microsoft.playwright.Page;
import com.testautomation.utils.ExtentReportManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class DashboardPage extends BasePage {
    private static final Logger logger = LogManager.getLogger(DashboardPage.class);

    // Page Elements
    private static final String DASHBOARD_TITLE = "#dashboard-page h1";
    private static final String USER_MENU = "#dashboard-page .user-menu button";
    private static final String LOGOUT_BUTTON = "#dashboard-page a[onclick='logout()']";
    private static final String PROFILE_BUTTON = "#dashboard-page a[onclick='showProfile()']";
    private static final String SETTINGS_BUTTON = "#dashboard-page a[onclick='showSettings()']";
    private static final String WELCOME_MESSAGE = "#dashboard-page .welcome-message";
    private static final String NAVIGATION_MENU = "#dashboard-page nav";
    private static final String DASHBOARD_CONTENT = "#dashboard-page .dashboard-content";
    private static final String STATS_SECTION = "#dashboard-page .stats";
    private static final String RECENT_ACTIVITY = "#dashboard-page .recent-activity";
    private static final String NOTIFICATIONS = "#dashboard-page .notifications";
    private static final String SEARCH_BOX = "#dashboard-page #search";
    private static final String SEARCH_BUTTON = "#dashboard-page .search-form button[type='submit']";

    public DashboardPage(Page page) {
        super(page);
    }

    public boolean isPageLoaded() {
        logger.info("Checking if dashboard page is loaded");
        boolean isLoaded = isVisible(DASHBOARD_TITLE) || isVisible(DASHBOARD_CONTENT);
        ExtentReportManager.logInfo("Dashboard page loaded: " + isLoaded);
        return isLoaded;
    }

    public String getPageTitle() {
        logger.info("Getting dashboard page title");
        String title = getText(DASHBOARD_TITLE);
        ExtentReportManager.logInfo("Dashboard page title: " + title);
        return title;
    }

    public String getWelcomeMessage() {
        logger.info("Getting welcome message");
        String welcomeMessage = getText(WELCOME_MESSAGE);
        ExtentReportManager.logInfo("Welcome message: " + welcomeMessage);
        return welcomeMessage;
    }

    public boolean isUserMenuVisible() {
        logger.info("Checking if user menu is visible");
        boolean isVisible = isVisible(USER_MENU);
        ExtentReportManager.logInfo("User menu visible: " + isVisible);
        return isVisible;
    }

    public void clickUserMenu() {
        logger.info("Clicking user menu");
        takeScreenshot("before_user_menu_click");
        click(USER_MENU);
        takeScreenshot("after_user_menu_click");
        ExtentReportManager.logInfo("User menu clicked successfully");
    }

    public LandingPage clickLogoutButton() {
        logger.info("Clicking logout button");
        takeScreenshot("before_logout_click");
        click(LOGOUT_BUTTON);
        
        // Wait for the landing page section to become visible (since it's a single-page app)
        try {
            page.waitForSelector("#landing-page:not(.hidden)", new Page.WaitForSelectorOptions().setTimeout(10000));
            logger.info("Landing page section is now visible");
        } catch (Exception e) {
            logger.warn("Landing page visibility timeout, continuing with page load state wait");
        }
        
        waitForLoadState();
        takeScreenshot("after_logout_click");
        ExtentReportManager.logInfo("Logout button clicked successfully");
        return new LandingPage(page);
    }

    public void clickProfileButton() {
        logger.info("Clicking profile button");
        takeScreenshot("before_profile_click");
        
        // First open the user menu dropdown
        click(USER_MENU);
        waitForElement(PROFILE_BUTTON);
        
        // Then click the profile button
        click(PROFILE_BUTTON);
        waitForLoadState();
        takeScreenshot("after_profile_click");
        ExtentReportManager.logInfo("Profile button clicked successfully");
    }

    public void clickSettingsButton() {
        logger.info("Clicking settings button");
        takeScreenshot("before_settings_click");
        
        // First open the user menu dropdown
        click(USER_MENU);
        waitForElement(SETTINGS_BUTTON);
        
        // Then click the settings button
        click(SETTINGS_BUTTON);
        waitForLoadState();
        takeScreenshot("after_settings_click");
        ExtentReportManager.logInfo("Settings button clicked successfully");
    }

    public boolean isStatsSectionVisible() {
        logger.info("Checking if stats section is visible");
        boolean isVisible = isVisible(STATS_SECTION);
        ExtentReportManager.logInfo("Stats section visible: " + isVisible);
        return isVisible;
    }

    public boolean isRecentActivityVisible() {
        logger.info("Checking if recent activity section is visible");
        boolean isVisible = isVisible(RECENT_ACTIVITY);
        ExtentReportManager.logInfo("Recent activity visible: " + isVisible);
        return isVisible;
    }

    public boolean isNotificationsVisible() {
        logger.info("Checking if notifications are visible");
        boolean isVisible = isVisible(NOTIFICATIONS);
        ExtentReportManager.logInfo("Notifications visible: " + isVisible);
        return isVisible;
    }

    public void clickNotifications() {
        logger.info("Clicking notifications");
        takeScreenshot("before_notifications_click");
        click(NOTIFICATIONS);
        takeScreenshot("after_notifications_click");
        ExtentReportManager.logInfo("Notifications clicked successfully");
    }

    public void search(String searchTerm) {
        logger.info("Searching for: {}", searchTerm);
        takeScreenshot("before_search");
        fill(SEARCH_BOX, searchTerm);
        click(SEARCH_BUTTON);
        waitForLoadState();
        takeScreenshot("after_search");
        ExtentReportManager.logInfo("Search performed for: " + searchTerm);
    }

    public void enterSearchTerm(String searchTerm) {
        logger.info("Entering search term: {}", searchTerm);
        takeScreenshot("before_search_entry");
        fill(SEARCH_BOX, searchTerm);
        takeScreenshot("after_search_entry");
        ExtentReportManager.logInfo("Search term entered: " + searchTerm);
    }

    public void clickSearchButton() {
        logger.info("Clicking search button");
        takeScreenshot("before_search_button_click");
        click(SEARCH_BUTTON);
        waitForLoadState();
        takeScreenshot("after_search_button_click");
        ExtentReportManager.logInfo("Search button clicked successfully");
    }

    public boolean isNavigationMenuVisible() {
        logger.info("Checking if navigation menu is visible");
        boolean isVisible = isVisible(NAVIGATION_MENU);
        ExtentReportManager.logInfo("Navigation menu visible: " + isVisible);
        return isVisible;
    }

    public void scrollToStatsSection() {
        logger.info("Scrolling to stats section");
        scrollToElement(STATS_SECTION);
        takeScreenshot("stats_section_scrolled");
        ExtentReportManager.logInfo("Scrolled to stats section successfully");
    }

    public void scrollToRecentActivity() {
        logger.info("Scrolling to recent activity section");
        scrollToElement(RECENT_ACTIVITY);
        takeScreenshot("recent_activity_scrolled");
        ExtentReportManager.logInfo("Scrolled to recent activity section successfully");
    }

    public String getCurrentUrl() {
        String url = super.getCurrentUrl();
        ExtentReportManager.logInfo("Current URL: " + url);
        return url;
    }

    public void waitForPageToLoad() {
        logger.info("Waiting for dashboard page to load completely");
        // Wait for the dashboard page section to be visible (not hidden)
        waitForElement("#dashboard-page:not(.hidden)");
        waitForElement(DASHBOARD_TITLE);
        waitForLoadState();
        takeScreenshot("dashboard_page_loaded");
        ExtentReportManager.logInfo("Dashboard page loaded completely");
    }

    public LandingPage performLogout() {
        logger.info("Performing logout process");
        if (isUserMenuVisible()) {
            clickUserMenu();
        }
        return clickLogoutButton();
    }
}
