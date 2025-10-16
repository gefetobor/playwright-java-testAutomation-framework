package com.testautomation.utils;

import com.testautomation.config.ConfigManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;

/**
 * Retry analyzer for TestNG tests to handle transient failures
 */
public class RetryAnalyzer implements IRetryAnalyzer {
    private static final Logger logger = LogManager.getLogger(RetryAnalyzer.class);
    private int retryCount = 0;
    private final int maxRetryCount = ConfigManager.getRetryMaxAttempts();
    private final long retryDelayMs = ConfigManager.getRetryDelayMs();

    @Override
    public boolean retry(ITestResult result) {
        if (retryCount < maxRetryCount) {
            retryCount++;
            logger.warn("Retrying test: {} - Attempt: {}/{}", 
                result.getMethod().getMethodName(), retryCount, maxRetryCount);
            
            // Add delay before retry
            try {
                Thread.sleep(retryDelayMs);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                logger.warn("Retry delay interrupted", e);
            }
            
            return true;
        }
        
        logger.error("Test failed after {} attempts: {}", maxRetryCount, result.getMethod().getMethodName());
        return false;
    }
}
