package com.testautomation.base;

import com.microsoft.playwright.*;
import com.testautomation.config.ConfigManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class PlaywrightManager {
    private static final Logger logger = LogManager.getLogger(PlaywrightManager.class);
    private static final ThreadLocal<Playwright> playwright = new ThreadLocal<>();
    private static final ThreadLocal<Browser> browser = new ThreadLocal<>();
    private static final ThreadLocal<BrowserContext> context = new ThreadLocal<>();
    private static final ThreadLocal<Page> page = new ThreadLocal<>();

    public static void setUp() {
        logger.info("Setting up Playwright in thread: {}", Thread.currentThread().getName());
        
        Playwright pw = Playwright.create();
        String browserName = ConfigManager.getProperty("browser.name", "chromium");
        boolean headless = ConfigManager.getBooleanProperty("browser.headless", false);
        
        Browser br;
        switch (browserName.toLowerCase()) {
            case "firefox":
                br = pw.firefox().launch(new BrowserType.LaunchOptions().setHeadless(headless));
                break;
            case "webkit":
                br = pw.webkit().launch(new BrowserType.LaunchOptions().setHeadless(headless));
                break;
            case "chromium":
            default:
                br = pw.chromium().launch(new BrowserType.LaunchOptions().setHeadless(headless));
                break;
        }
        
        BrowserContext ctx = br.newContext(new Browser.NewContextOptions()
                .setLocale("en-US")
                .setAcceptDownloads(true));
        
        ctx.setDefaultNavigationTimeout(ConfigManager.getIntProperty("app.navigation.timeout", 45000));
        ctx.setDefaultTimeout(ConfigManager.getIntProperty("app.timeout", 15000));
        
        Page pg = ctx.newPage();

        playwright.set(pw);
        browser.set(br);
        context.set(ctx);
        page.set(pg);
        
        logger.info("Playwright setup completed in thread: {} with browser: {} (headless: {})", 
            Thread.currentThread().getName(), browserName, headless);
    }

    public static Page getPage() {
        return page.get();
    }
    
    public static BrowserContext getContext() {
        return context.get();
    }
    
    public static Browser getBrowser() {
        return browser.get();
    }
    
    public static Playwright getPlaywright() {
        return playwright.get();
    }

    public static void tearDown() {
        logger.info("Tearing down Playwright in thread: {}", Thread.currentThread().getName());
        
        try {
            if (page.get() != null) {
                page.get().close();
                page.remove();
            }
        } catch (Exception e) {
            logger.debug("Error closing page in thread {}", Thread.currentThread().getName(), e);
        }
        
        try {
            if (context.get() != null) {
                context.get().close();
                context.remove();
            }
        } catch (Exception e) {
            logger.debug("Error closing context in thread {}", Thread.currentThread().getName(), e);
        }
        
        try {
            if (browser.get() != null) {
                browser.get().close();
                browser.remove();
            }
        } catch (Exception e) {
            logger.debug("Error closing browser in thread {}", Thread.currentThread().getName(), e);
        }
        
        try {
            if (playwright.get() != null) {
                playwright.get().close();
                playwright.remove();
            }
        } catch (Exception e) {
            logger.debug("Error closing playwright in thread {}", Thread.currentThread().getName(), e);
        }
        
        logger.info("Playwright teardown completed in thread: {}", Thread.currentThread().getName());
    }
}
