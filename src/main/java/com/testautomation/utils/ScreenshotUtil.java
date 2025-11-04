package com.testautomation.utils;

import com.microsoft.playwright.Page;
import com.testautomation.config.ConfigManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class ScreenshotUtil {
    private static final Logger logger = LogManager.getLogger(ScreenshotUtil.class);
    private static final String SCREENSHOT_DIR = ConfigManager.getProperty("screenshot.path", "test-output/screenshots/");
    private static final String BASELINE_DIR = ConfigManager.getProperty("screenshot.baseline.dir", "src/test/resources/baseline-screenshots/");
    private static final String DIFF_DIR = "test-output/screenshot-diffs/";

    public static String takeScreenshot(Page page, String testName, String stepName) {
        try {
            Path screenshotDirectory = ensureDirectory(Paths.get(SCREENSHOT_DIR));
            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
            String fileName = buildFileName(testName, stepName, timestamp);
            Path filePath = screenshotDirectory.resolve(fileName);
            page.screenshot(new Page.ScreenshotOptions().setPath(filePath));

            logger.info("Screenshot saved: {}", filePath);
            return filePath.toString();
        } catch (Exception e) {
            if (stepName != null && !stepName.trim().isEmpty()) {
                logger.error("Failed to take screenshot for test: {}, step: {}", testName, stepName, e);
            } else {
                logger.error("Failed to take screenshot for test: {}", testName, e);
            }
            return null;
        }
    }

    public static void assertScreenshotMatchesBaseline(String actualScreenshotPath,
                                                       String baselineKey,
                                                       double allowedDiffPercentage) {
        if (actualScreenshotPath == null) {
            throw new AssertionError("Actual screenshot path is null; cannot compare against baseline.");
        }

        Path baselinePath = resolveBaselinePath(baselineKey);
        if (!Files.exists(baselinePath)) {
            throw new AssertionError(String.format(Locale.ENGLISH,
                    "Baseline screenshot not found for '%s'. Expected at %s. " +
                            "Capture and store a baseline image before rerunning the test.",
                    baselineKey, baselinePath.toAbsolutePath()));
        }

        try {
            BufferedImage baselineImage = ImageIO.read(baselinePath.toFile());
            Path path = Paths.get(actualScreenshotPath);
            BufferedImage actualImage = ImageIO.read(path.toFile());

            if (baselineImage == null || actualImage == null) {
                throw new AssertionError(String.format(Locale.ENGLISH,
                        "Unable to read screenshots for comparison. Baseline: %s, Actual: %s",
                        baselinePath, actualScreenshotPath));
            }

            if (baselineImage.getWidth() != actualImage.getWidth() ||
                    baselineImage.getHeight() != actualImage.getHeight()) {
                String diffMessage = String.format(Locale.ENGLISH,
                        "Screenshot dimensions mismatch for '%s'. Baseline: %dx%d, Actual: %dx%d. " +
                                "Baseline: %s, Actual: %s",
                        baselineKey,
                        baselineImage.getWidth(), baselineImage.getHeight(),
                        actualImage.getWidth(), actualImage.getHeight(),
                        baselinePath, actualScreenshotPath);
                throw new AssertionError(diffMessage);
            }

            ComparisonResult comparisonResult = compareImages(baselineImage, actualImage);
            double diffRatio = comparisonResult.getDiffRatio();

            if (diffRatio > allowedDiffPercentage) {
                Path diffPath = comparisonResult.getDiffImage() != null
                        ? writeDiffImage(comparisonResult.getDiffImage(), baselineKey)
                        : null;
                String message = String.format(Locale.ENGLISH,
                        "Visual regression detected for '%s'. Diff ratio: %.4f%% (allowed: %.4f%%). " +
                                "Baseline: %s, Actual: %s, Diff: %s",
                        baselineKey,
                        diffRatio * 100,
                        allowedDiffPercentage * 100,
                        baselinePath.toAbsolutePath(),
                        path.toAbsolutePath(),
                        diffPath != null ? diffPath.toAbsolutePath() : "n/a");
                throw new AssertionError(message);
            }

            logger.info("Screenshot matches baseline '{}'. Diff ratio: {}%", baselineKey,
                    String.format(Locale.ENGLISH, "%.4f", diffRatio * 100));
        } catch (IOException e) {
            throw new RuntimeException(String.format(Locale.ENGLISH,
                    "Failed to compare screenshots for '%s'. Baseline: %s, Actual: %s",
                    baselineKey, baselinePath, actualScreenshotPath), e);
        }
    }

    private static ComparisonResult compareImages(BufferedImage baselineImage,
                                                  BufferedImage actualImage) {
        int width = baselineImage.getWidth();
        int height = baselineImage.getHeight();
        long mismatchedPixels = 0;

        BufferedImage diffImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int baselineRgb = baselineImage.getRGB(x, y);
                int actualRgb = actualImage.getRGB(x, y);
                if (baselineRgb != actualRgb) {
                    mismatchedPixels++;
                    diffImage.setRGB(x, y, 0xFFFF0000);
                } else {
                    diffImage.setRGB(x, y, baselineRgb);
                }
            }
        }

        double diffRatio = (double) mismatchedPixels / (width * height);

        if (diffRatio == 0) {
            diffImage = null;
        }

        return new ComparisonResult(diffRatio, diffImage);
    }

    private static Path writeDiffImage(BufferedImage diffImage, String baselineKey) throws IOException {
        Path diffDirectory = ensureDirectory(Paths.get(DIFF_DIR));
        String sanitizedKey = baselineKey.replaceAll("[^a-zA-Z0-9_/.-]", "_").replace('/', '_').replace('\\', '_');
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
        Path diffPath = diffDirectory.resolve(String.format("%s_diff_%s.png", sanitizedKey, timestamp));
        ImageIO.write(diffImage, "png", diffPath.toFile());
        logger.warn("Diff image generated for '{}': {}", baselineKey, diffPath.toAbsolutePath());
        return diffPath;
    }

    private static Path resolveBaselinePath(String baselineKey) {
        String sanitizedKey = baselineKey.replace("\\", "/");
        Path baselineDirectory = Paths.get(BASELINE_DIR);
        if (sanitizedKey.endsWith(".png")) {
            return baselineDirectory.resolve(sanitizedKey);
        }
        return baselineDirectory.resolve(sanitizedKey + ".png");
    }

    private static Path ensureDirectory(Path directory) throws IOException {
        if (!Files.exists(directory)) {
            Files.createDirectories(directory);
            logger.info("Created directory: {}", directory.toAbsolutePath());
        }
        return directory;
    }

    private static String buildFileName(String testName, String stepName, String timestamp) {
        String safeTestName = testName != null ? testName : "screenshot";
        if (stepName != null && !stepName.trim().isEmpty()) {
            return String.format("%s_%s_%s.png", safeTestName, stepName, timestamp);
        }
        return String.format("%s_%s.png", safeTestName, timestamp);
    }

    public static String takeScreenshot(Page page, String testName) {
        return takeScreenshot(page, testName, null);
    }

    @SuppressWarnings("LombokGetterMayBeUsed")
    private static class ComparisonResult {
        private final double diffRatio;
        private final BufferedImage diffImage;

        private ComparisonResult(double diffRatio, BufferedImage diffImage) {
            this.diffRatio = diffRatio;
            this.diffImage = diffImage;
        }

        public double getDiffRatio() {
            return diffRatio;
        }

        public BufferedImage getDiffImage() {
            return diffImage;
        }

    }
}
