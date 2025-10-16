package com.testautomation.utils;

import com.microsoft.playwright.Page;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ScreenshotUtil {
    private static final Logger logger = LogManager.getLogger(ScreenshotUtil.class);
    private static final String SCREENSHOT_DIR = "test-output/screenshots/";

    public static String takeScreenshot(Page page, String testName) {
        try {
            // Create screenshots directory if it doesn't exist
            File screenshotDir = new File(SCREENSHOT_DIR);
            if (!screenshotDir.exists()) {
                screenshotDir.mkdirs();
            }

            // Generate unique filename with timestamp
            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
            String fileName = testName + "_" + timestamp + ".png";
            String filePath = SCREENSHOT_DIR + fileName;

            // Take screenshot
            page.screenshot(new Page.ScreenshotOptions().setPath(Paths.get(filePath)));

            logger.info("Screenshot saved: {}", filePath);
            return filePath;
        } catch (Exception e) {
            logger.error("Failed to take screenshot for test: {}", testName, e);
            return null;
        }
    }

    public static String takeScreenshot(Page page, String testName, String stepName) {
        try {
            // Create screenshots directory if it doesn't exist
            File screenshotDir = new File(SCREENSHOT_DIR);
            if (!screenshotDir.exists()) {
                screenshotDir.mkdirs();
            }

            // Generate unique filename with timestamp
            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
            String fileName = testName + "_" + stepName + "_" + timestamp + ".png";
            String filePath = SCREENSHOT_DIR + fileName;

            // Take screenshot
            page.screenshot(new Page.ScreenshotOptions().setPath(Paths.get(filePath)));

            logger.info("Screenshot saved: {}", filePath);
            return filePath;
        } catch (Exception e) {
            logger.error("Failed to take screenshot for test: {}, step: {}", testName, stepName, e);
            return null;
        }
    }
}
